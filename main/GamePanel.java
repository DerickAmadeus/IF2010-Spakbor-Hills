package main;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import Items.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import java.awt.image.BufferedImage;
import player.Player;
import Map.Map;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.Font;
import java.awt.AlphaComposite; // Untuk efek fade

public class GamePanel extends JPanel implements Runnable {

    public final int titleState = -2;
    public final int farmNameInputState = -1;
    public final int playState = 0;
    public final int pauseState = 1;
    public final int dialogState = 2;
    public final int inventoryState = 3;
    public final int itemOptionState = 4;
    public final int sleepFadeOutState = 5; // State baru untuk fade out tidur
    public final int sleepFadeInState = 6;  // State baru untuk fade in tidur
    public int gameState = titleState;
    private long lastMapUpdateTime = 0;
    private static final long MAP_UPDATE_INTERVAL = 10_000; // 10 detik

    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    public final int worldCol = 32;
    public final int worldRow = 32;
    public final int worldWidth = tileSize * worldCol;
    public final int worldHeight = tileSize * worldRow;

    public List<TransitionData> transitions;

    public Map map = new Map(this);
    public KeyHandler keyHandler = new KeyHandler(this);
    public final TitlePage titlePage = new TitlePage(this);
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player;
    private BufferedImage backgroundImage;

    public boolean debugMode = false;

    // Variabel untuk efek fade
    private float alphaFade = 0f;
    private final float FADE_SPEED = 0.02f; // Kecepatan fade (0.01f - 0.05f disarankan)

    public GamePanel() {
        this.setPreferredSize(new java.awt.Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        gameState = titleState;

        this.player = new Player(this, keyHandler);
        initializeTransitions();

        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/main/cloud.png"));
            System.out.println("Gambar latar belakang game berhasil dimuat.");
        } catch (IOException e) {
            System.err.println("Gagal memuat gambar latar belakang game: " + e.getMessage());
            e.printStackTrace();
            backgroundImage = null;
        }
    }

    private void initializeTransitions() {
        transitions = new ArrayList<>();
        transitions.add(new TransitionData(0, 0, 10, 1, 3, 1, 12, 11, false, tileSize));
        transitions.add(new TransitionData(1, 15, 10, 1, 3, 0, 1, 11, false, tileSize));
        transitions.add(new TransitionData(0, 15, 0, 3, 1, 2, 10, 9, false, tileSize));
        transitions.add(new TransitionData(2, 10, 10, 3, 1, 0, 16, 1, false, tileSize));
        transitions.add(new TransitionData(0, 5, 10, 1, 1, 3, 7, 12, false, tileSize));
        transitions.add(new TransitionData(3, 7, 13, 1, 1, 0, 5, 11, false, tileSize));
    }

    public void checkMapTransitions() {
        for (TransitionData transition : transitions) {
            transition.updateCooldown();
            Rectangle absolutePlayerSolidArea = new Rectangle(
                player.x + player.solidArea.x,
                player.y + player.solidArea.y,
                player.solidArea.width,
                player.solidArea.height
            );

            if (transition.isTriggered(absolutePlayerSolidArea, map.currentMapID)) {
                if (!transition.requiresInteraction) {
                    System.out.println("Transition triggered: From Map ID " + map.currentMapID +
                                       " To Map ID " + transition.targetMapID +
                                       " at player pos (" + transition.targetPlayerX / tileSize + ", " +
                                       transition.targetPlayerY / tileSize + ")");
                    int previousMapID = map.currentMapID;
                    map.loadMapByID(transition.targetMapID);
                    player.x = transition.targetPlayerX;
                    player.y = transition.targetPlayerY;
                    player.collisionOn = false;
                    player.direction = "down";
                    transition.startCooldown();
                    for (TransitionData otherTransition : transitions) {
                        if (otherTransition.sourceMapID == map.currentMapID &&
                            otherTransition.targetMapID == previousMapID) {
                            Rectangle playerSpawnSolidArea = new Rectangle(
                                player.x + player.solidArea.x,
                                player.y + player.solidArea.y,
                                player.solidArea.width,
                                player.solidArea.height
                            );
                            if (otherTransition.sourceArea.intersects(playerSpawnSolidArea)) {
                                otherTransition.startCooldown();
                                System.out.println("Applied return cooldown to transition from map " + otherTransition.sourceMapID + " to " + otherTransition.targetMapID);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    public void startSleepSequence() {
        if (gameState == playState) { // Hanya mulai tidur dari playState
            gameState = sleepFadeOutState;
            alphaFade = 0f; // Mulai fade out dari transparan
            System.out.println("Starting sleep fade out...");
        }
    }

    public void setupGame(){
        gameState = titleState;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (gameThread != null) {
            update();
            repaint();
            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (gameState == titleState){
            if(keyHandler.enterPressed){
                if(titlePage.commandNumber == 0){
                    gameState = farmNameInputState;
                }
                else if (titlePage.commandNumber == 2){
                    System.exit(0);
                }
                keyHandler.enterPressed = false;
            }
        } else if (gameState == farmNameInputState){
            if(keyHandler.enterPressed){
             gameState = playState;
            }
        } else if (gameState == playState) {
            player.update();
            checkMapTransitions();
            player.tiling();
            player.recoverLand();
            player.planting();
            player.watering();
            player.harvesting();
        } else if (gameState == inventoryState) {
            player.getInventory().updateInventoryCursor(
                keyHandler.upPressed, keyHandler.downPressed, keyHandler.leftPressed, keyHandler.rightPressed
            );
            keyHandler.upPressed = false; keyHandler.downPressed = false;
            keyHandler.leftPressed = false; keyHandler.rightPressed = false;
            if (keyHandler.enterPressed) {
                player.getInventory().selectCurrentItem();
                keyHandler.enterPressed = false;
            }
        } else if (gameState == itemOptionState) {
            if (keyHandler.upPressed) {
                player.getInventory().optionCommandNum = (player.getInventory().optionCommandNum - 1 + 3) % 3; // Assuming 3 options
                keyHandler.upPressed = false;
            }
            if (keyHandler.downPressed) {
                player.getInventory().optionCommandNum = (player.getInventory().optionCommandNum + 1) % 3; // Assuming 3 options
                keyHandler.downPressed = false;
            }
            if (keyHandler.enterPressed) {
                Item selected = player.getInventory().getSelectedItem();
                int command = player.getInventory().optionCommandNum;

                if (selected instanceof Equipment || selected instanceof Seeds) {
                    if (command == 0) { // Equip/Unequip
                        if (player.getEquippedItem() == selected) {
                            player.equipItem(null);
                        } else {
                            player.equipItem(selected);
                        }
                        gameState = inventoryState;
                    } else if (command == 1) { // Cancel
                        gameState = inventoryState;
                    }
                } else if (selected instanceof Fish || selected instanceof Crops || selected instanceof Food) {
                    if (command == 0) { // Eat
                        player.eating(); // Player.eating() will handle energy and item removal
                        gameState = inventoryState; // Kembali ke inventory setelah makan
                    } else if (command == 1) { // Cancel
                        gameState = inventoryState;
                    }
                }
                player.getInventory().optionCommandNum = 0; // Reset option command
                keyHandler.enterPressed = false;
            }
        } else if (gameState == sleepFadeOutState) {
            alphaFade += FADE_SPEED;
            if (alphaFade >= 1f) {
                alphaFade = 1f;
                player.completeSleep(); // Isi energi, dll.
                // Tambahkan logika untuk memajukan hari jika perlu di sini atau di completeSleep()
                // map.advanceDay(); // Contoh jika ada fungsi memajukan hari
                System.out.println("Finished fade out, proceeding to fade in.");
                gameState = sleepFadeInState;
            }
        } else if (gameState == sleepFadeInState) {
            alphaFade -= FADE_SPEED;
            if (alphaFade <= 0f) {
                alphaFade = 0f;
                System.out.println("Finished fade in, returning to play state.");
                gameState = playState;
                player.interactionCooldown = 30; // Cooldown setelah bangun tidur
            }
        }

        // Update Peta berkala (di luar cek gameState playState agar tetap berjalan jika relevan)
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMapUpdateTime >= MAP_UPDATE_INTERVAL && (gameState == playState || gameState == sleepFadeInState || gameState == sleepFadeOutState) ) {
             // Hanya update tiles jika game berjalan atau sedang transisi tidur
            map.updateTiles();
            lastMapUpdateTime = currentTime;
        }


        if (keyHandler.f1Pressed) {
            debugMode = !debugMode;
            keyHandler.f1Pressed = false;
            System.out.println("Debug mode: " + (debugMode ? "ON" : "OFF"));
        }
        if (keyHandler.invPressed) {
            // Hanya buka/tutup inventory jika tidak sedang tidur/fade
            if (gameState == playState) {
                gameState = inventoryState;
            } else if (gameState == inventoryState || gameState == itemOptionState) {
                gameState = playState;
                 player.getInventory().optionCommandNum = 0; // Reset pilihan opsi saat keluar inventory
            }
            keyHandler.invPressed = false;
        }
    }

    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (gameState == titleState) {
            titlePage.draw(g2);
            g2.dispose(); // Dispose di akhir title state drawing
            return;
        } else if (gameState == farmNameInputState) {
            g2.setColor(java.awt.Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            g2.setColor(java.awt.Color.white);
            g2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 30));
            g2.drawString("Farm Name Input State", 100, screenHeight / 2);
            g2.dispose(); // Dispose di akhir farm name input state drawing
            return;
        }

        // Gambar game utama (latar belakang, peta, pemain)
        if (map.currentMapID == 3) { // House map
            g2.setColor(java.awt.Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
        } else {
            if (backgroundImage != null) {
                g2.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight, null);
            } else {
                g2.setColor(java.awt.Color.cyan);
                g2.fillRect(0, 0, screenWidth, screenHeight);
            }
        }
        map.draw(g2);
        player.drawPlayer(g2);
        player.drawEnergyBar(g2); // Selalu gambar energy bar

        // Gambar UI di atas game utama
        if (gameState == inventoryState) {
            player.openInventory(g2);
        } else if (gameState == itemOptionState) {
            player.openInventory(g2); // Tetap gambar inventory di belakang
            player.getInventory().drawItemOptionWindow(g2);
        }

        // Gambar efek fade untuk tidur
        if (gameState == sleepFadeOutState || gameState == sleepFadeInState) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaFade));
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // Reset alpha composite
        }


        if (debugMode) {
            for (TransitionData transition : transitions) {
                if (transition.sourceMapID == map.currentMapID) {
                    g2.setColor(new Color(0, 0, 255, 80));
                    int screenAreaX = transition.sourceArea.x - player.x + player.screenX;
                    int screenAreaY = transition.sourceArea.y - player.y + player.screenY;
                    g2.fillRect(screenAreaX, screenAreaY, transition.sourceArea.width, transition.sourceArea.height);
                    if (transition.cooldownFrames > 0) {
                        g2.setColor(Color.YELLOW);
                        g2.setFont(new Font("Arial", Font.BOLD, 12));
                        g2.drawString("COOLDOWN: " + transition.cooldownFrames, screenAreaX, screenAreaY - 5);
                    }
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Arial", Font.BOLD, 10));
                    g2.drawString("ToMap:" + transition.targetMapID, screenAreaX, screenAreaY + 12);
                }
            }
        }
        g2.dispose(); // Dispose di akhir paintComponent jika tidak ada return sebelumnya
    }
}