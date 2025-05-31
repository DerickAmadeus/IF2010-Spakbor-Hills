package main.menu;

import main.GamePanel;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.awt.event.KeyEvent;

public class InGameHelp {
        GamePanel gp;

        Font helpFont;
    
        public int commandNumber = 0;

        private final String back = "BACK";
        private final String title = "GAME GUIDE";
        private final String subtitle = "HOW TO PLAY SPAKBOR HILLS";

        private String[] lines = {
        "=== BASIC CONTROLS ===",
        "W/A/S/D : Move character",
        "TAB     : Switch focus between options",
        "ENTER   : Confirm selection",
        "ESC     : Back/Cancel",
        "I       : Open Inventory",
        "E       : Sleep on Bed",
        "R       : Interact with Shipping Bin",
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
        "",
        "=== SEASONS ===",
        "- Each season lasts 10 days",
        "- Different crops grow in different seasons",
        "- Some fish only available in certain seasons",
        "",
        "=== WEATHER ===",
        "- Sunny: Normal day, water crops manually",
        "- Rainy: Crops get watered automatically",
        "- Some fish only appear in certain weather",
        "",
        "=== TIME SYSTEM ===",
        "- Day starts at 6:00 AM",
        "- Each in-game minute = 1 real second",
        "- At 02:00 AM, player will fall asleep",
        "",
        "=== INVENTORY ===",
        "- Press I to open inventory",
        "- Use items, equip tools, or consume food",
        "- Food restores energy",
        "- Tools help with farming and fishing"
        };
        
        private int scrollOffset = 0;
        private final int lineHeight = 18;
        private final int visibleLines = 6; 
                
        public InGameHelp(GamePanel gp) {
            this.gp = gp;
            loadFont();
        }     

        private void loadFont() {
            try {
                InputStream inputStream = getClass().getResourceAsStream("/main/PressStart2PRegular.ttf");
                helpFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            } catch (FontFormatException | IOException e) {
                System.out.println("Failed to load font: " + e.getMessage());
                helpFont = new Font("Arial", Font.BOLD, 24);
            }
        }

        public void draw(Graphics2D g2) {
            int frameWidth = gp.tileSize * 12;
            int frameHeight = gp.tileSize * 10;
            int frameX = (gp.screenWidth  - frameWidth)  / 2;
            int frameY = (gp.screenHeight - frameHeight) / 2;

            // kotak
            Color c = new Color(0,0,0, 210);
            g2.setColor(c);
            g2.fillRoundRect(frameX, frameY, frameWidth, frameHeight, 35, 35);
            c = new Color(255,255,255);
            g2.setColor(c);
            g2.setStroke(new BasicStroke(5));
            g2.drawRoundRect(frameX+5, frameY+5, frameWidth-10, frameHeight-10, 25, 25);


            //title
            g2.setFont(helpFont.deriveFont(Font.BOLD, 22F));
            g2.setColor(new Color(255, 215, 0));
            int titleX = getX(title, g2);
            int titleY = frameY + gp.tileSize * 2 - 50;
            g2.drawString(title, titleX, titleY);

            //subtitle
            g2.setFont(helpFont.deriveFont(Font.BOLD, 12F));
            g2.setColor(Color.WHITE);
            g2.drawString(subtitle, getX(subtitle, g2), frameY + gp.tileSize * 3 - 70);
            
            //konten
            g2.setFont(helpFont.deriveFont(Font.PLAIN, 8F));

            // scroll
            Shape oldClip = g2.getClip();
            int contentX = frameX + gp.tileSize;
            int contentY = frameY + 80;
            int contentWidth = frameWidth - 40;
            int contentHeight = frameHeight - 110;
                
            g2.clipRect(contentX, contentY - lineHeight, contentWidth, contentHeight);

            int startY = contentY - scrollOffset;
            
            for (String txt : lines) {
                if (startY > contentY - lineHeight && startY < contentY + contentHeight) {
                    if (txt.startsWith("===")) {
                        g2.setColor(new Color(100, 255, 100)); 
                    } else {
                        g2.setColor(Color.WHITE);
                    }
                    g2.drawString(txt, contentX, startY + 20);
                }
                startY += lineHeight;
            }

            g2.setClip(oldClip);
               
            // BACK 
            g2.setFont(helpFont.deriveFont(Font.BOLD, 20F));
            int backX  = frameX + gp.tileSize + (commandNumber == 0 ? gp.tileSize : 0);
            int backY  = frameY + frameHeight - 20;  

            g2.setColor(Color.BLACK);
            g2.drawString(back, backX + 2, backY + 2);
            g2.setColor(commandNumber == 0 ? Color.yellow : Color.WHITE);
            
            if (commandNumber == 0){
                g2.setColor(Color.yellow);
                g2.drawString(">", backX - 50, backY);
            }

            g2.drawString(back, backX, backY);  
        }

        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();

            if (keyCode == KeyEvent.VK_ESCAPE){          // ESC auto back
                    gp.gameState = gp.menuState;
                    return;
            } else if (keyCode == KeyEvent.VK_TAB){
                commandNumber = (commandNumber == 0) ? 1 : 0;
                gp.keyHandler.upPressed = false;
                gp.keyHandler.downPressed = false;
            } else if (keyCode == KeyEvent.VK_ENTER) {
                if (commandNumber == 0) {
                    gp.gameState = gp.menuState;
                    gp.keyHandler.enterPressed = false;
                }
            } else if (keyCode == KeyEvent.VK_W) {
                // Scroll up
                scrollOffset = Math.max(0, scrollOffset - lineHeight);
            } else if (keyCode == KeyEvent.VK_S) {
                // Scroll down
                int maxScroll = (lines.length * lineHeight) - (visibleLines * lineHeight);
                scrollOffset = Math.min(maxScroll, scrollOffset + lineHeight);
            }
        }

    public int getX(String text, Graphics2D g2){
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }
}