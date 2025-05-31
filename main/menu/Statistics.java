package main.menu;

import main.GamePanel;
import player.Player;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.awt.event.KeyEvent;

public class Statistics {
    GamePanel gp;

    Font helpFont;

    public int commandNumber = 0;

    private final String back = "BACK";
    private final String title = "STATISTICS";

    public Statistics(GamePanel gp) {
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
        int frameX = (gp.screenWidth - frameWidth) / 2;
        int frameY = (gp.screenHeight - frameHeight) / 2;

        // kotak
        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(frameX, frameY, frameWidth, frameHeight, 35, 35);
        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(frameX + 5, frameY + 5, frameWidth - 10, frameHeight - 10, 25, 25);

        // title
        g2.setFont(helpFont.deriveFont(Font.BOLD, 26F));
        g2.setColor(new Color(255, 215, 0));
        int titleX = getX(title, g2);
        int titleY = frameY + gp.tileSize * 2 - 40;
        g2.drawString(title, titleX, titleY);

        // konten
        g2.setFont(helpFont.deriveFont(Font.PLAIN, 10F));
        g2.setColor(Color.WHITE);

        int lx = frameX + gp.tileSize; // margin kiri dalam kotak
        int ly = titleY + gp.tileSize / 2;

        Player p = gp.player; // referensi pemain
        String[] rows = {
                "Total income           : " + p.totalIncome,
                "Total expenditure      : " + p.totalExpenditure,
                "Average season income  : " + (int) p.totalIncome / 4,
                "Total days played      : " + gp.daysPlayed,
                
                "NPCs status - " + gp.npcs[0].getName() + " : ",
                "   - Relationship status   : " + gp.npcs[0].getRelationship(),
                "   - Chatting Frequency    : ",
                "   - Gifting Frequency     : ",
                "   - Visiting Frequency    : ",
                
                "NPCs status - " + gp.npcs[1].getName() + " : ",
                "   - Relationship status   : " + gp.npcs[1].getRelationship(),
                "   - Chatting Frequency    : ",
                "   - Gifting Frequency     : ",
                "   - Visiting Frequency    : ",

                "NPCs status - " + gp.npcs[2].getName() + " : ",
                "   - Relationship status   : " + gp.npcs[2].getRelationship(),
                "   - Chatting Frequency    : ",
                "   - Gifting Frequency     : ",
                "   - Visiting Frequency    : ",

                "NPCs status - " + gp.npcs[3].getName() + " : ",
                "   - Relationship status   : " + gp.npcs[3].getRelationship(),
                "   - Chatting Frequency    : ",
                "   - Gifting Frequency     : ",
                "   - Visiting Frequency    : ",

                "NPCs status - " + gp.npcs[4].getName() + " : ",
                "   - Relationship status   : " + gp.npcs[4].getRelationship(),
                "   - Chatting Frequency    : ",
                "   - Gifting Frequency     : ",
                "   - Visiting Frequency    : ",

                "NPCs status - " + gp.npcs[5].getName() + " : ",
                "   - Relationship status   : " + gp.npcs[5].getRelationship(),
                "   - Chatting Frequency    : ",
                "   - Gifting Frequency     : ",
                "   - Visiting Frequency    : ",
            
                "Crops harvested        : " + p.cropsHarvested,
                "Fish caught :",
                "   - Common    : " + p.fishCaughtCommon,
                "   - Regular   : " + p.fishCaughtRegular,
                "   - Legendary : " + p.fishCaughtLegendary,
        };

        for (String line : rows) {
            g2.drawString(line, lx, ly);
            ly += gp.tileSize / 2;
        }

        // BACK
        g2.setFont(helpFont.deriveFont(Font.BOLD, 20F));
        int backX = frameX + gp.tileSize + (commandNumber == 0 ? gp.tileSize : 0);
        int backY = frameY + frameHeight - 20;

        g2.setColor(Color.BLACK);
        g2.drawString(back, backX + 2, backY + 2);
        g2.setColor(commandNumber == 0 ? Color.yellow : Color.WHITE);

        if (commandNumber == 0) {
            g2.setColor(Color.yellow);
            g2.drawString(">", backX - 50, backY);
        }

        g2.drawString(back, backX, backY);
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_ESCAPE) { // ESC auto back
            gp.gameState = gp.menuState;
            return;
        } else if (keyCode == KeyEvent.VK_TAB) {
            commandNumber = (commandNumber == 0) ? 1 : 0;
            gp.keyHandler.upPressed = false;
            gp.keyHandler.downPressed = false;
        } else if (keyCode == KeyEvent.VK_ENTER) {
            if (commandNumber == 0) {
                gp.gameState = gp.menuState;
                gp.keyHandler.enterPressed = false;
            }
        }
    }

    public int getX(String text, Graphics2D g2) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }

}
