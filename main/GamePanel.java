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

public class GamePanel extends JPanel implements Runnable {

    //Game State
    public final int titleState = -2;
    public final int farmNameInputState = -1;
    public final int playState = 0;
    public final int pauseState = 1;
    public final int dialogState = 2;
    public final int inventoryState = 3;
    public final int itemOptionState = 4;
    public final int sleepingFadeOutState = 5; // New state for fading to black
    public final int sleepingFadeInState = 6;  // New state for fading back from black
    public int gameState = titleState;

    public String[] initialSeason = {"Spring", "Summer", "Fall", "Winter"};
    public int currentSeasonIndex = 0;
    public String currentSeason = initialSeason[currentSeasonIndex];
    public int gameHour = 6;
    public int gameMinute = 0;
    public int gameDay = 1;
    public int daysPlayed = 0;
    public int lastUpdateMinute = -1;

    private long lastRealTime = System.currentTimeMillis();
    private static final int REAL_TIME_INTERVAL = 1000;

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

    // Fade effect variables
    private int fadeAlpha = 0;
    private final int FADE_SPEED = 5; // Adjust for faster/slower fade

    public GamePanel() {
        this.setPreferredSize(new java.awt.Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        gameState = titleState;
        this.player = new Player(this, keyHandler, "initial");
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
                    player.solidArea.height);

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
                        if (otherTransition.sourceMapID == map.currentMapID &&
                                otherTransition.targetMapID == previousMapID) {
                            Rectangle playerSpawnSolidArea = new Rectangle(
                                    player.x + player.solidArea.x,
                                    player.y + player.solidArea.y,
                                    player.solidArea.width,
                                    player.solidArea.height);
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

    public void setupGame() {
        gameState = titleState;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    public void startSleepingSequence() {
        if (gameState == playState) { // Ensure we can only sleep from playState
            gameState = sleepingFadeOutState;
            fadeAlpha = 0; // Start fully transparent, will fade to black
        }
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
                    if (gameDay > 10) { // Season Change Logic
                        currentSeasonIndex = (currentSeasonIndex + 1) % initialSeason.length;
                        currentSeason = initialSeason[currentSeasonIndex];
                        gameDay = 1; // Reset day for new season
                    }
                }
            }
            map.updateTiles();
        }
    }

    @Override
    public void run() {
        while (gameThread != null) {
            update();
            repaint();
            try {
                Thread.sleep(16); // roughly 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (gameState == titleState) {
            if (keyHandler.enterPressed) {
                if (titlePage.commandNumber == 0) {
                    gameState = farmNameInputState;
                } else if (titlePage.commandNumber == 2) {
                    System.exit(0);
                }
                keyHandler.enterPressed = false;
            }
        } else if (gameState == farmNameInputState) {
            if (keyHandler.enterPressed) {
                // Assuming farm name is set here somehow before transitioning
                // player.setFarmName(inputFarmName);
                gameState = playState;
                keyHandler.enterPressed = false; // Consume press
            }
        } else if (gameState == playState) {
            player.update();
            player.tiling();
            player.recoverLand();
            player.planting();
            checkMapTransitions();
            player.watering();
            player.harvesting();
            player.sleeping();
            player.WatchTV();
<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> fb525b28b82c427a37f31a1f9f1e0809b32dc757
            // Time progression
            long now = System.currentTimeMillis();
            if (now - lastRealTime >= REAL_TIME_INTERVAL) {
                gameMinute += 5;
                if (gameMinute >= 60) {
                    gameMinute = 0;
                    gameHour++;
                    if (gameHour >= 24) {
                        gameHour = 0; // Midnight
                        gameDay++;
                        daysPlayed++;
                        // Season Change Logic on natural day turnover
                        if (gameDay > 10) {
                            currentSeasonIndex = (currentSeasonIndex + 1) % initialSeason.length;
                            currentSeason = initialSeason[currentSeasonIndex];
                            gameDay = 1; // Reset day for new season
                        }
                    }
                }
                map.updateTiles();
                lastRealTime = now;
            }
<<<<<<< HEAD
            player.fishing();
<<<<<<< HEAD
>>>>>>> fb525b28b82c427a37f31a1f9f1e0809b32dc757
=======
>>>>>>> fb525b28b82c427a37f31a1f9f1e0809b32dc757
        }
        if (gameState == inventoryState) {
            player.getInventory().updateInventoryCursor(
                keyHandler.upPressed,
                keyHandler.downPressed,
                keyHandler.leftPressed,
                keyHandler.rightPressed
            );
=======
>>>>>>> parent of d0d269f (Merge branch 'debug' into Derick2)

        } else if (gameState == inventoryState) {
            player.getInventory().updateInventoryCursor(
                    keyHandler.upPressed, keyHandler.downPressed, keyHandler.leftPressed, keyHandler.rightPressed);
            keyHandler.upPressed = false;
            keyHandler.downPressed = false;
            keyHandler.leftPressed = false;
            keyHandler.rightPressed = false;

            if (keyHandler.enterPressed) {
                player.getInventory().selectCurrentItem();
                // gameState might change to itemOptionState by selectCurrentItem()
                keyHandler.enterPressed = false;
            }
        } else if (gameState == itemOptionState) {
            if (keyHandler.upPressed) {
                player.getInventory().optionCommandNum = (player.getInventory().optionCommandNum - 1 + 3) % 3;
                keyHandler.upPressed = false;
            }
            if (keyHandler.downPressed) {
                player.getInventory().optionCommandNum = (player.getInventory().optionCommandNum + 1) % 3;
                keyHandler.downPressed = false;
            }
            if (keyHandler.enterPressed) {
                Item selected = player.getInventory().getSelectedItem();
                if (selected instanceof Equipment) {
                    Equipment eq = (Equipment) selected;
                    if (player.getInventory().optionCommandNum == 0) { // Equip/Unequip
                        if (player.getEquippedItem() == eq) player.equipItem(null);
                        else player.equipItem(eq);
                        gameState = inventoryState;
                    } else if (player.getInventory().optionCommandNum == 1) { // Cancel
                        gameState = inventoryState;
                    }
                } else if (selected instanceof Seeds) {
                     Seeds sd = (Seeds) selected;
                    if (player.getInventory().optionCommandNum == 0) { // Equip/Unequip
                        if (player.getEquippedItem() == sd) player.equipItem(null);
                        else player.equipItem(sd);
                        gameState = inventoryState;
                    } else if (player.getInventory().optionCommandNum == 1) { // Cancel
                        gameState = inventoryState;
                    }
                } else if (selected instanceof Fish || selected instanceof Crops || selected instanceof Food) {
                     if (player.getInventory().optionCommandNum == 0) { // Eat
                        player.eating(); // Assumes eating handles item removal & energy
                        gameState = inventoryState;
                    } else if (player.getInventory().optionCommandNum == 1) { // Cancel
                        gameState = inventoryState;
                    }
                }
                keyHandler.enterPressed = false;
            }
        } else if (gameState == sleepingFadeOutState) {
            fadeAlpha += FADE_SPEED;
            if (fadeAlpha >= 255) {
                fadeAlpha = 255; // Fully black
                // --- Perform sleep actions ---
                gameHour = 6; // Wake up at 6 AM
                gameMinute = 0;
                gameDay++;
                daysPlayed++;
                player.setEnergy(Player.MAX_ENERGY); // Restore energy

                // Season Change Logic
                if (gameDay > 10) { // Assuming 10 days per season
                    currentSeasonIndex = (currentSeasonIndex + 1) % initialSeason.length;
                    currentSeason = initialSeason[currentSeasonIndex];
                    gameDay = 1; // Reset day for new season
                }
                map.updateTiles(); // Update tiles for the new day/time
                // --- End sleep actions ---
                gameState = sleepingFadeInState; // Start fading back in
            }
        } else if (gameState == sleepingFadeInState) {
            fadeAlpha -= FADE_SPEED;
            if (fadeAlpha <= 0) {
                fadeAlpha = 0; // Fully transparent
                gameState = playState; // Back to game
            }
        }

        // Global key listeners (can be accessed from multiple states)
        if (keyHandler.f1Pressed) {
            debugMode = !debugMode;
            keyHandler.f1Pressed = false;
            System.out.println("Debug mode: " + (debugMode ? "ON" : "OFF"));
        }
        if (keyHandler.invPressed) {
            if (gameState == playState) {
                gameState = inventoryState;
            } else if (gameState == inventoryState || gameState == itemOptionState) {
                // Close inventory/options and return to play state
                gameState = playState;
            }
            keyHandler.invPressed = false; // Consume the press
        }
    }

    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (gameState == titleState) {
            titlePage.draw(g2);
            g2.dispose(); // dispose was here in original, but typically not at the end of paintComponent
            return;
        } else if (gameState == farmNameInputState) {
            g2.setColor(java.awt.Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            g2.setColor(java.awt.Color.white);
            g2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 30));
            g2.drawString("Enter Farm Name:", 100, screenHeight / 2 - 40);
            // Add input field drawing here if you have one for farm name
            g2.drawString("Press Enter to Continue", 100, screenHeight / 2 + 40);
            // No g2.dispose() here in original for this state
            return;
        }

        // Draw game world (visible during play, pause, dialog, inventory, options, and fades)
        if (map.currentMapID == 3) { // House map
            g2.setColor(java.awt.Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
        } else { // Other maps
            if (backgroundImage != null) {
                g2.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight, null);
            } else {
                g2.setColor(java.awt.Color.cyan);
                g2.fillRect(0, 0, screenWidth, screenHeight);
            }
        }
        map.draw(g2);
        player.drawPlayer(g2);

        // UI Elements (Time, Season, Energy) - drawn on top of game world
        g2.setColor(java.awt.Color.white);
        g2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
        String timeString = String.format("Day %d - %02d:%02d", gameDay, gameHour, gameMinute);
<<<<<<< HEAD
        g2.drawString(timeString, 500, 30);
        g2.drawString(currentSeason, 500, 50);
        g2.drawString(player.getLocation(), 500, 70);
        g2.drawString(currentSeason + " - " + currentWeather, 500, 50);



        player.drawPlayer(g2);
        player.drawEnergyBar(g2);
=======
        g2.drawString(timeString, screenWidth - 180, 30); // Adjusted position
        g2.drawString(currentSeason, screenWidth - 180, 55); // Adjusted position
        player.drawEnergyBar(g2); // Energy bar
>>>>>>> parent of d0d269f (Merge branch 'debug' into Derick2)

        // Debug transition areas
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

        // Inventory and Item Options UI (drawn on top of game world and basic UI)
        if (gameState == inventoryState) {
            player.openInventory(g2);
        }
        if (gameState == itemOptionState) {
            player.getInventory().drawItemOptionWindow(g2); // Assumes inventory is drawn first or handled by this method
        }

        // Fade Effect Overlay (drawn on top of almost everything)
        if (gameState == sleepingFadeOutState || gameState == sleepingFadeInState) {
            g2.setColor(new Color(0, 0, 0, fadeAlpha)); // Black color with current alpha
            g2.fillRect(0, 0, screenWidth, screenHeight);
        }
        // g2.dispose(); // Generally not called at the end of paintComponent by convention.
    }
}