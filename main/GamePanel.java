package main;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Items.Equipment;
import Items.Item;

import java.awt.Graphics2D;
import java.io.IOException;
import java.awt.image.BufferedImage;

import player.Player; // Importing player class from player package
import Map.Map; // Importing map class from Map package
import player.Inventory;

public class GamePanel extends JPanel implements Runnable {
    
    //Game State
    public final int playState = 0;
    public final int pauseState = 1;
    public final int dialogState = 2;
    public final int inventoryState = 3;
    public final int itemOptionState = 4;
    public int gameState = playState;

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


    public Map map = new Map(this);
    KeyHandler keyHandler = new KeyHandler(); // Key handler for keyboard input 
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
        this.player = new Player(this, keyHandler); // Initialize player object
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/main/cloud.png"));
            System.out.println("Gambar latar belakang game berhasil dimuat.");
        } catch (IOException e) {
            System.err.println("Gagal memuat gambar latar belakang game: " + e.getMessage());
            e.printStackTrace();
            backgroundImage = null; // Atur ke null jika gagal, paintComponent akan menangani ini
        }
    }




    public void startGameThread() {
        gameThread = new Thread(this); // Create a new thread for the game loop
        gameThread.start(); // Start the game loop thread
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
        player.update();
        // Potentially update other game entities or systems here
        // e.g., map.update(), npcs.update(), etc.

        // Toggle debug mode with a key (e.g., F1) - OPTIONAL
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
        }
        if (gameState == inventoryState) {
            player.getInventory().updateInventoryCursor(
                keyHandler.upPressed,
                keyHandler.downPressed,
                keyHandler.leftPressed,
                keyHandler.rightPressed
            );

            // Reset arah tombol agar tidak repeat terus
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
                    if (selected instanceof Equipment eq) {
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
                    }
                    keyHandler.enterPressed = false;
                }
            }

        }

    }

    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g); // Call the superclass method to clear the screen
        // Draw game elements here
        // Example: g.drawRect(0, 0, tileSize, tileSize); // Draw a rectangle at (0, 0) with size tileSize
        Graphics2D g2 = (Graphics2D) g; // Cast Graphics to Graphics2D for advanced drawing
        g2.setColor(java.awt.Color.white); // Set color to white
                // Draw background image if available
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight, null);
        } else {
            g2.setColor(java.awt.Color.cyan); // Set color to cyan if no image
            g2.fillRect(0, 0, screenWidth, screenHeight); // Fill the background with cyan
        }

        map.draw(g2); // Draw the map


        player.drawPlayer(g2);
        player.drawEnergyBar(g2);

        if(gameState == inventoryState) {
             player.openInventory(g2);
        }
        if (gameState == itemOptionState) {
            player.getInventory().drawItemOptionWindow(g2);
        }

    }

}
