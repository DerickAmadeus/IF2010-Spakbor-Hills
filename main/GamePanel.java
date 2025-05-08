package main;

import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.RenderingHints.Key;
import player.player; // Importing player class from player package

public class GamePanel extends JPanel implements Runnable {

    final int originalTileSize = 16; // Original tile size in pixels
    final int scale = 3; // Scale factor

    public final int tileSize = originalTileSize * scale; // Scaled tile size
    final int maxScreenCol = 16; // Maximum number of columns on the screen
    final int maxScreenRow = 12; // Maximum number of rows on the screen
    final int screenWidth = tileSize * maxScreenCol; // Screen width in pixels
    final int screenHeight = tileSize * maxScreenRow; // Screen height in pixels

    KeyHandler keyHandler = new KeyHandler(); // Key handler for keyboard input 
    Thread gameThread; // Thread for the game loop
    player player; // Player object


    int playerX = 100; // Player's X position
    int playerY = 100; // Player's Y position
    int playerSpeed = 4; // Player's speed

    public GamePanel() {
        this.setPreferredSize(new java.awt.Dimension(screenWidth, screenHeight));
        this.setBackground(java.awt.Color.black);
        this.setDoubleBuffered(true); // Enable double buffering for smoother rendering
        this.addKeyListener(keyHandler); // Add key listener for keyboard input
        this.setFocusable(true); // Make the panel focusable to receive key events
        this.player = new player(this, keyHandler); // Initialize player object
    }




    public void startGameThread() {
        gameThread = new Thread(this); // Create a new thread for the game loop
        gameThread.start(); // Start the game loop thread
    }

    @Override
    public void run() {
        // Game loop logic here
        while (gameThread != null) {
            

            System.out.println("Game loop running..."); // Debug message
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
        
    }

    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g); // Call the superclass method to clear the screen
        // Draw game elements here
        // Example: g.drawRect(0, 0, tileSize, tileSize); // Draw a rectangle at (0, 0) with size tileSize
        Graphics2D g2 = (Graphics2D) g; // Cast Graphics to Graphics2D for advanced drawing
        g2.setColor(java.awt.Color.white); // Set color to white

        player.drawPlayer(g2);
    }




}
