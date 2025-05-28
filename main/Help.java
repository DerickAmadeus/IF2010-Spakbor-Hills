package main;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import javax.imageio.ImageIO;

public class Help {
    GamePanel gp;
    
    Font helpFont;
    
    public int commandNumber = 0;
    private final String back = "BACK";
    private final String title = "GAME GUIDE";
    private final String subtitle = "SPAKBOR HILLS ADVENTURE";

    BufferedImage helpBackground;

    private final String[] lines = {
        "=== BASIC CONTROLS ===",
        "W/A/S/D : Move character",
        "TAB     : Switch focus between options",
        "ENTER   : Confirm selection",
        "ESC     : Back/Cancel",
        "I       : Open Inventory",
        "F1      : Toggle Debug Mode",
        "",
        "=== GAME OBJECTIVES ===",
        "- Earn 17,209g through farming and fishing",
        "- Get married to one of the NPCs",
        "",
        "=== ENERGY SYSTEM ===",
        "- Starting energy: 100",
        "- Actions cost 5 energy each (tilling, planting, etc)",
        "- Sleep to restore energy",
        "- Energy can go to -20 before forced sleep",
        "",
        "=== FARMING ===",
        "- Till land, plant seeds, water crops",
        "- Crops grow in season-specific time",
        "- Rainy days automatically water crops",
        "- Harvest when ready to earn gold",
        "",
        "=== FISHING ===",
        "- Fish at Pond, River, Lake or Ocean",
        "- Different fish available by season/time/weather",
        "- Three fish types: Common, Regular, Legendary",
        "- Legendary fish are rare and valuable",
        "",
        "=== NPC RELATIONSHIPS ===",
        "- Gift items to increase friendship",
        "- Each NPC loves/likes/hates different items",
        "- Reach 150 hearts to propose marriage",
        "- Marriage unlocks special benefits",
    };


    public Help(GamePanel gp){
        this.gp = gp;
        loadFont();
        loadHelpScreen();
    }

     private void loadFont() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("PressStart2PRegular.ttf");
            helpFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (FontFormatException | IOException e) {
            System.out.println("Failed to load font: " + e.getMessage());
            helpFont = new Font("Arial", Font.BOLD, 24);
        }
    }

    public void loadHelpScreen(){
        try{
            InputStream inputStream = getClass().getResourceAsStream("Summer1.png");

            if (inputStream != null){
                helpBackground = ImageIO.read(inputStream);
            }
            else{
                System.err.println("Farm input screen can't be load!");
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void draw(Graphics2D g2){
        // bg 
        if (helpBackground != null){
            g2.drawImage(helpBackground, 0, 0, gp.screenWidth, gp.screenHeight, null);
        }
        else{
            g2.setColor(new Color(7, 150, 255));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        }

        //overlay biar agak gelap
        g2.setColor(new Color(0,0,0,160));      
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
    
        //title
        g2.setFont(helpFont.deriveFont(Font.BOLD, 36F));
        g2.setColor(new Color(255, 215, 0));
        g2.drawString(title, getX(title, g2), gp.tileSize * 2);

        //subtitle
        g2.setFont(helpFont.deriveFont(Font.BOLD, 24F));
        g2.setColor(Color.WHITE);
        g2.drawString(subtitle, getX(subtitle, g2), gp.tileSize * 3);

        //konten
        g2.setFont(helpFont.deriveFont(Font.PLAIN, 18F));
        int y = gp.tileSize * 4;
        int lineGap = gp.tileSize;

        for (String txt : lines) {
            if (txt.startsWith("===")) {
                g2.setColor(new Color(100, 255, 100)); 
            } else {
                g2.setColor(Color.WHITE);
            }
            g2.drawString(txt, gp.tileSize, y);
            y += lineGap;
        }

        // BACK
        g2.setFont(helpFont.deriveFont(Font.BOLD, 28F));
        int margin = 20;
        int backY  = gp.screenHeight - margin;
        int backX  = margin + (commandNumber == 0 ? gp.tileSize : 0);

        g2.setColor(Color.BLACK);
        g2.drawString(back, backX + 5, backY + 5);
        g2.setColor(Color.WHITE);
        
        if (commandNumber == 0){
            g2.drawString(">", margin, backY);
        }

        g2.drawString(back, backX, backY);
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_ESCAPE){          // ESC auto back
                gp.gameState = gp.titleState;
                return;
        } else if (keyCode == KeyEvent.VK_TAB){
            commandNumber = (commandNumber == 0) ? 1 : 0;
            gp.keyHandler.upPressed = false;
            gp.keyHandler.downPressed = false;
        } else if (keyCode == KeyEvent.VK_ENTER) {
            if (commandNumber == 0) {
                gp.gameState = gp.titleState;
                gp.keyHandler.enterPressed = false;
            }
        } 
    }

    public int getX(String text, Graphics2D g2){
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }
    
}
