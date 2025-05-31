package main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class PlayerInput {
    GamePanel gp;
    String playerNameInput = "";
    Font inputFont;
    private final String nameMessage = "Enter your name: ";
    private final String genderMessage = "Select your gender: ";
    private final String back = "BACK";
    private final int maxLength = 15;  
    
    public int commandNumber = 0; // 0 = name input, 1 = male, 2 = female, 3 = back
    String selectedGender = "";
    
    BufferedImage playerInputBackground;

    public PlayerInput(GamePanel gp) {
        this.gp = gp;
        try {
            InputStream inputStream = getClass().getResourceAsStream("PressStart2PRegular.ttf");
            inputFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (FontFormatException | IOException e) {
            System.out.println("Failed to load font: " + e.getMessage());
            inputFont = new Font("Arial", Font.BOLD, 30);
        }
        
        loadInputScreen();
    }

    public void loadInputScreen() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("Summer1.png");
            if (inputStream != null) {
                playerInputBackground = ImageIO.read(inputStream);
            } else {
                System.err.println("Player input screen can't be loaded!");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void draw(Graphics2D g2) {
        // Background
        if (playerInputBackground != null) {
            g2.drawImage(playerInputBackground, 0, 0, gp.screenWidth, gp.screenHeight, null);
        } else {
            g2.setColor(new Color(7, 150, 255));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        }

        // Overlay
        g2.setColor(new Color(0, 0, 0, 160));      
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Name message
        g2.setFont(inputFont.deriveFont(Font.BOLD, 24F));
        g2.setColor(Color.white);
        int x = getX(nameMessage, g2);
        int y = gp.screenHeight / 2 - gp.tileSize * 2;
        g2.drawString(nameMessage, x, y);

        // Name input
        String displayText = playerNameInput;
        if (commandNumber == 0 && (System.currentTimeMillis() / 500) % 2 == 0) {
            displayText += "_"; // Cursor blink
        }
        
        g2.setFont(inputFont.deriveFont(Font.PLAIN, 18F));
        x = getX(displayText, g2);
        y += gp.tileSize;
        g2.drawString(displayText, x, y);

        // Gender selection
        g2.setFont(inputFont.deriveFont(Font.BOLD, 24F));
        x = getX(genderMessage, g2);
        y += gp.tileSize * 2;
        g2.drawString(genderMessage, x, y);
        
        // Male option
        g2.setFont(inputFont.deriveFont(Font.PLAIN, 20F));
        String maleText = "Male";
        x = getX(maleText, g2);
        y += gp.tileSize;
        if (commandNumber == 1) {
            // g2.setColor(Color.yellow);
            g2.drawString(">", x - gp.tileSize/2, y);
        }
        g2.setColor(Color.white);
        g2.drawString(maleText, x, y);
        
        // Female option
        String femaleText = "Female";
        x = getX(femaleText, g2);
        y += gp.tileSize;
        if (commandNumber == 2) {
            // g2.setColor(Color.yellow);
            g2.drawString(">", x - gp.tileSize/2, y);
        }
        g2.setColor(Color.white);
        g2.drawString(femaleText, x, y);

        // BACK
        g2.setFont(inputFont.deriveFont(Font.BOLD, 30F));
        g2.setColor(commandNumber == 3 ? Color.yellow : Color.white);
        int margin = 20;
        int backY  = gp.screenHeight - margin;
        int backX  = margin + (commandNumber==3 ? gp.tileSize : 0);

        // shadow
        g2.setColor(Color.black);
        g2.drawString(back, backX + 5, backY + 5);
        g2.setColor(Color.white);
        
        if (commandNumber == 3){
            g2.setColor(Color.yellow);
            g2.drawString(">", margin, backY);
        }

        g2.drawString(back, backX, backY);
    }


    public int getX(String text, Graphics2D g2){
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }

    public void reset() {
        playerNameInput = "";
        selectedGender  = "";
        commandNumber = 0;
    }

    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();
        char ch  = e.getKeyChar();

        /* ESC – balik ke FarmNameInputState */
        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.farmNameInputState;
            gp.farmName.commandNumber = 0;
            return;
        }

        /* TAB – pindah fokus siklis 0→1→2→3→0 */
        if (code == KeyEvent.VK_TAB) {
            commandNumber = (commandNumber + 1) % 4;
            return;
        }

        /* ENTER */
        if (code == KeyEvent.VK_ENTER) {
            switch (commandNumber) {
                case 1:
                    selectedGender = "Male";
                    break;
                case 2:
                    selectedGender = "Female";
                    break;
                case 3:
                    gp.gameState = gp.farmNameInputState;
                    return;              
            }
            /* validasi keseluruhan */
            if (!playerNameInput.trim().isEmpty() && (selectedGender=="Male"||selectedGender=="Female")) {
                gp.player.setPlayerName(playerNameInput.trim());
                gp.player.setGender(selectedGender);
                gp.setRainDaysForSeason();
                gp.gameState = gp.playState;
            }
            return;
        }

        /* EDIT TEKS – hanya jika fokus di nama */
        if (commandNumber != 0) return;

        if (code == KeyEvent.VK_BACK_SPACE) {
            if (!nameMessage.isEmpty()){
                playerNameInput = playerNameInput.substring(0, playerNameInput.length()-1);
            return;
            }
        }

        if (playerNameInput.length() < maxLength) {
            if (ch >= 32 && ch <= 126) {
                playerNameInput += ch;
            } 
        }
    }
}