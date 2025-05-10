package main;

import javax.swing.JFrame;  

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame();
        window.setTitle("2D Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false); // Prevent resizing the window

        GamePanel gamePanel = new GamePanel(); // Create an instance of GamePanel
        window.add(gamePanel); // Add the GamePanel to the JFrame
        window.pack(); // Pack the window to fit the preferred size of the GamePanel

        window.setLocationRelativeTo(null); // Center the window on the screen
        window.setVisible(true); // Make the window visible
        gamePanel.startGameThread(); // Start the game loop thread
    }
    
}
