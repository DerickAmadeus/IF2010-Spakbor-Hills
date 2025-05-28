package main;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Items.*;

import java.awt.Graphics2D;
import java.awt.Rectangle; // Tambahkan import ini
import java.io.IOException;
import java.awt.image.BufferedImage;

import player.Player; // Importing player class from player package
import Map.Map; // Importing map class from Map package

import java.util.ArrayList; // Tambahkan import ini
import java.util.List;    // Tambahkan import ini
import java.awt.Color;    // Tambahkan import ini

import java.awt.Font; // Tambahkan import ini

public class GamePanel extends JPanel implements Runnable {
    
    //Game State
    public final int titleState = -2;
    public final int farmNameInputState = -1;
    public final int playState = 0;
    public final int pauseState = 1;
    public final int dialogState = 2;
    public final int inventoryState = 3;
    public final int itemOptionState = 4;
    public int gameState = titleState;
    public String[] initialSeason = {"Spring", "Summer", "Fall", "Winter"};
    public int currentSeasonIndex = 0;
    public String currentSeason = initialSeason[currentSeasonIndex];
    // Game Time
    public int gameHour = 6; // Mulai dari jam 6 pagi
    public int gameMinute = 0;
    public int gameDay = 1;
    public int daysPlayed = 0;
    public int lastUpdateMinute = -1;



    private long lastRealTime = System.currentTimeMillis();
    private static final int REAL_TIME_INTERVAL = 1000; // 1 detik

    final int originalTileSize = 16; // Original tile size in pixels
    final int scale = 3; // Scale factor

    public final int tileSize = originalTileSize * scale; // Scaled tile size
    public final int maxScreenCol = 16; // Maximum number of columns on the screen
    public final int maxScreenRow = 12; // Maximum number of rows on the screen
    public final int screenWidth = tileSize * maxScreenCol; // Screen width in pixels
    public final int screenHeight = tileSize * maxScreenRow; // Screen height in pixels


    //WorldMap Parameters
    public final int worldCol = 32; // Number of columns in the world map
    public final int worldRow = 32; // Number of rows in the world map
    public final int worldWidth = tileSize * worldCol; // World map width in pixels
    public final int worldHeight = tileSize * worldRow; // World map height in pixels

    public List<TransitionData> transitions;


    public Map map = new Map(this);
    public KeyHandler keyHandler = new KeyHandler(this); // Key handler for keyboard input 
    public final TitlePage  titlePage  = new TitlePage(this);
    Thread gameThread; // Thread for the game loop
    public CollisionChecker cChecker = new CollisionChecker(this); // Collision checker for player movement
    public Player player; // Player object
    private BufferedImage backgroundImage; // Background image for the game\

    public boolean debugMode = false;


    int playerX = 100; // Player's X position
    int playerY = 100; // Player's Y position
    int playerSpeed = 4; // Player's speed

    

    public GamePanel() {
        this.setPreferredSize(new java.awt.Dimension(screenWidth, screenHeight));
        // this.setBackground(java.awt.Color.cyan);
        this.setDoubleBuffered(true); // Enable double buffering for smoother rendering
        this.addKeyListener(keyHandler); // Add key listener for keyboard input
        this.setFocusable(true); // Make the panel focusable to receive key events
        
        gameState = titleState; // start dari title dulu

        this.player = new Player(this, keyHandler, "initial"); // Initialize player object
        initializeTransitions(); // Panggil setelah tileSize dan player siap

        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/main/cloud.png"));
            System.out.println("Gambar latar belakang game berhasil dimuat.");
        } catch (IOException e) {
            System.err.println("Gagal memuat gambar latar belakang game: " + e.getMessage());
            e.printStackTrace();
            backgroundImage = null; // Atur ke null jika gagal, paintComponent akan menangani ini
        }
    }

    private void initializeTransitions() {
        transitions = new ArrayList<>();

        // Contoh Transisi:
        // Dari Farm Map (ID 0) ke Forest Map (ID 1)
        // Area pemicu di Farm Map: kolom 0, baris 10 sampai 12 (lebar 1 tile, tinggi 3 tiles)
        // Pemain muncul di Forest Map pada tile (15, 10) (misalnya, di sisi kanan forest map)
        transitions.add(new TransitionData(0, 0, 10, 1, 3, 1, 12, 11, false, tileSize));

        // Dari Forest Map (ID 1) kembali ke Farm Map (ID 0)
        // Area pemicu di Forest Map: kolom 16 (misal), baris 10 sampai 12
        // Pemain muncul di Farm Map pada tile (1, 11) (misalnya, dekat sisi kiri farm map)
        transitions.add(new TransitionData(1, 15, 10, 1, 3, 0, 1, 11, false, tileSize));

        // Dari Farm Map (ID 0) ke Mountain Lake Map (ID 2)
        // Misal, dari sisi atas farm map: kolom 15-17, baris 0
        // Muncul di Mountain Lake Map di tile (10, 15) (misal, di sisi bawah mountain map)
        transitions.add(new TransitionData(0, 15, 0, 3, 1, 2, 10, 9, false, tileSize));

        // Dari Mountain Lake Map (ID 2) kembali ke Farm Map (ID 0)
        // Area pemicu di Mountain Lake Map: kolom 10-12, baris 16
        // Muncul di Farm Map pada tile (16, 1)
        transitions.add(new TransitionData(2, 10, 10, 3, 1, 0, 16, 1, false, tileSize));
        
        // Dari Farm Map (ID 0) ke HouseMap (ID 3)
        // Area pemicu, Door
        //muncul di depan door rumah
        transitions.add(new TransitionData(0, 5, 10, 1, 1, 3, 7, 12, false, tileSize));
        // No additional transitions needed here for background color change.

        transitions.add(new TransitionData(3, 7, 13, 1, 1, 0, 5, 11, false, tileSize));


        // Tambahkan transisi lain sesuai kebutuhan Anda
    }

    public void checkMapTransitions() {
        for (TransitionData transition : transitions) {
            transition.updateCooldown(); // Selalu update cooldown

            // Buat Rectangle absolut dari solidArea pemain untuk pengecekan
            Rectangle absolutePlayerSolidArea = new Rectangle(
                player.x + player.solidArea.x,
                player.y + player.solidArea.y,
                player.solidArea.width,
                player.solidArea.height
            );

            if (transition.isTriggered(absolutePlayerSolidArea, map.currentMapID)) {
                if (!transition.requiresInteraction) { // Untuk transisi otomatis (injak)
                    System.out.println("Transition triggered: From Map ID " + map.currentMapID +
                                       " To Map ID " + transition.targetMapID +
                                       " at player pos (" + transition.targetPlayerX / tileSize + ", " +
                                       transition.targetPlayerY / tileSize + ")");

                    int previousMapID = map.currentMapID; // Simpan ID map sebelumnya

                    map.loadMapByID(transition.targetMapID);
                    player.x = transition.targetPlayerX;
                    player.y = transition.targetPlayerY;

                    // Reset status penting pemain jika perlu
                    player.collisionOn = false; 
                    player.direction = "down"; // Atur arah default
                    // player.isActuallyMoving = false; // Jika Anda memiliki variabel ini di Player
                    player.setLocation(transition.targetMapID);
                    if (map.currentMapID != 3 && map.currentMapID != 0) {
                        addMinutes(15);
                        player.setEnergy(player.getEnergy() - 10);

                    }
                    transition.startCooldown(); // Mulai cooldown untuk transisi yang baru saja digunakan

                    // Mencegah langsung kembali: terapkan cooldown pada transisi yang mengarah kembali
                    // jika pemain spawn di atasnya.
                    for (TransitionData otherTransition : transitions) {
                        if (otherTransition.sourceMapID == map.currentMapID && // Jika transisi lain ada di map baru
                            otherTransition.targetMapID == previousMapID) {   // Dan mengarah kembali ke map lama

                            Rectangle playerSpawnSolidArea = new Rectangle( // Area solid pemain di posisi spawn baru
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
                    break; // Proses satu transisi per frame untuk menghindari masalah
                }
                
            }
        }
    }




    public void setupGame(){
        gameState = titleState;
    }


    public void startGameThread() {
        gameThread = new Thread(this); // Create a new thread for the game loop
        gameThread.start(); // Start the game loop thread
    }

    public void addMinutes(int minutesToAdd) {
        for (int i = 0; i < minutesToAdd; i += 5) {
            gameMinute += 5;
            if (gameMinute >= 60) {
                gameMinute -= 60;
                gameHour++;
                if (gameHour >= 24) {
                    gameHour = 0;
                    gameDay++;
                    daysPlayed++;
                }
            }
            map.updateTiles(); // hanya update tiap 5 menit, sesuai
        }
    }



    @Override
    public void run() {
        // Game loop logic here
        while (gameThread != null) {
            

            // Update game state
            update();
            // Repaint screen
            repaint();

            // Delay to simulate FPS
            try {
                Thread.sleep(16); // roughly 60 FPS
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
        }
        else if (gameState == farmNameInputState){
            if(keyHandler.enterPressed){
              gameState = playState;
            }
        }   
        
        player.update();

        // Potentially update other game entities or systems here
        // e.g., map.update(), npcs.update(), etc.


        if (keyHandler.f1Pressed) { // Assuming you add f1Pressed to KeyHandler
            debugMode = !debugMode;
            keyHandler.f1Pressed = false; // Consume the press to avoid rapid toggling
            System.out.println("Debug mode: " + (debugMode ? "ON" : "OFF"));
        }
        if (keyHandler.invPressed) {
            if (gameState == playState) {
                gameState = inventoryState;
            } else if (gameState == inventoryState || gameState == itemOptionState) {
                gameState = playState;
            }
            keyHandler.invPressed = false;
        }
        if (gameState == playState) {
            player.tiling();
            player.recoverLand();
            player.planting();
            checkMapTransitions();
            player.watering();
            player.harvesting();
            player.sleeping();

        }
        if (gameState == inventoryState) {
            player.getInventory().updateInventoryCursor(
                keyHandler.upPressed,
                keyHandler.downPressed,
                keyHandler.leftPressed,
                keyHandler.rightPressed
            );

            keyHandler.upPressed = false;
            keyHandler.downPressed = false;
            keyHandler.leftPressed = false;
            keyHandler.rightPressed = false;

            // Saat tekan Enter, buka opsi untuk item yang dipilih
            if (keyHandler.enterPressed) {
                player.getInventory().selectCurrentItem();
                keyHandler.enterPressed = false;
            }
        }
        else if (gameState == itemOptionState) {
            if (keyHandler.upPressed) {
                player.getInventory().optionCommandNum = (player.getInventory().optionCommandNum - 1 + 3) % 3;
                keyHandler.upPressed = false;
            }
            if (keyHandler.downPressed) {
                player.getInventory().optionCommandNum = (player.getInventory().optionCommandNum + 1) % 3;
                keyHandler.downPressed = false;
            }

            if (gameState == itemOptionState) {
                if (keyHandler.enterPressed) {
                    Item selected = player.getInventory().getSelectedItem();
                    if (selected instanceof Equipment) {
                        Equipment eq = (Equipment) selected;
                        if (player.getInventory().optionCommandNum == 0) {
                            if (player.getEquippedItem() == eq) {
                                player.equipItem(null);
                            } else {
                                player.equipItem(eq);
                            }
                            gameState = inventoryState;
                        } else if (player.getInventory().optionCommandNum == 1) {
                            gameState = inventoryState; // Cancel
                        }
                    }  else if (selected instanceof Seeds) {
                        Seeds eq = (Seeds) selected;
                        if (player.getInventory().optionCommandNum == 0) {
                            if (player.getEquippedItem() == eq) {
                                player.equipItem(null);
                            } else {
                                player.equipItem(eq);
                            }
                            gameState = inventoryState;
                        } else if (player.getInventory().optionCommandNum == 1) {
                            gameState = inventoryState; // Cancel
                        }
                    } else if (selected instanceof Fish || selected instanceof Crops || selected instanceof Food) {
                        if (player.getInventory().optionCommandNum == 0) {
                            player.eating();
                            gameState = inventoryState;
                        } else if (player.getInventory().optionCommandNum == 1) {
                            gameState = inventoryState; // Cancel
                        }
                    }
                    keyHandler.enterPressed = false;
                }
            }

        }
        long now = System.currentTimeMillis();
        if (now - lastRealTime >= REAL_TIME_INTERVAL) {
            gameMinute += 5;
            if (gameMinute >= 60) {
                gameMinute = 0;
                gameHour++;
                if (gameHour >= 24) {
                    gameHour = 0;
                    gameDay++;
                    daysPlayed++;
                }
            }
            map.updateTiles();
            lastRealTime = now;
        }

        if (gameDay > 10) {
            currentSeasonIndex = (currentSeasonIndex + 1) % 4;
            currentSeason = initialSeason[currentSeasonIndex];
            gameDay %= 10;
        }

    }

    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;


        if (gameState == titleState) {
            titlePage.draw(g2);

            return; // Keluar setelah menggambar title state
        } else if (gameState == farmNameInputState) {
            g2.setColor(java.awt.Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            g2.setColor(java.awt.Color.white);
            g2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 30));
            g2.drawString("Farm Name Input State", 100, screenHeight / 2);
            // Sebaiknya jangan panggil g2.dispose() di sini
            return; // Keluar setelah menggambar farm name input state
        }

        if (map.currentMapID == 3) { // Ganti angka 3 jika ID peta rumah Anda berbeda
            g2.setColor(java.awt.Color.black); // Atur latar belakang menjadi hitam untuk rumah
            g2.fillRect(0, 0, screenWidth, screenHeight);
        } else {
            // Jika bukan peta rumah, gambar latar belakang luar ruangan seperti biasa
            if (backgroundImage != null) {
                g2.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight, null);
            } else {
                g2.setColor(java.awt.Color.cyan); // Latar belakang fallback jika gambar awan gagal dimuat
                g2.fillRect(0, 0, screenWidth, screenHeight);
            }
        }

        map.draw(g2); // Draw the map
        g2.setColor(java.awt.Color.white);
        g2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
        String timeString = String.format("Day %d - %02d:%02d", gameDay, gameHour, gameMinute);
        g2.drawString(timeString, 500, 30);
        g2.drawString(currentSeason, 500, 50);
        g2.drawString(player.getLocation(), 500, 70);



        player.drawPlayer(g2);
        player.drawEnergyBar(g2);

        if (debugMode) {
            for (TransitionData transition : transitions) {
                if (transition.sourceMapID == map.currentMapID) {
                    g2.setColor(new Color(0, 0, 255, 80)); // Biru transparan
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

        if (gameState == inventoryState) {
            player.openInventory(g2);
        }
        if (gameState == itemOptionState) {
            player.getInventory().drawItemOptionWindow(g2);
        }
    }

    public void startSleepingSequence() {
        // Mengatur jam permainan ke 6 pagi
        gameHour = 5;
        gameMinute = 55;

        // Mengatur hari permainan ke hari berikutnya
        gameDay++;
        daysPlayed++;
        // Memastikan pemain tidak dalam keadaan dialog atau interaksi lainnya
        gameState = playState;

        // Update peta dan transisi jika diperlukan
        map.updateTiles(); // Update tiles setelah tidur

        for (int i = 0; i <= 200; i += 10) {
            try {
            java.awt.Graphics g = this.getGraphics();
            if (g != null) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, i));
                g2.fillRect(0, 0, screenWidth, screenHeight);
                g2.dispose();
                Thread.sleep(30); // jeda animasi
            }
            } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            }
        }
        repaint(); // Memanggil repaint agar tampilan map diperbarui setelah tidur
    }
}
