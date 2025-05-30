package NPC;

import main.GamePanel;
import Items.*;


import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.Font; // Tambahkan untuk dialog
import java.awt.FontMetrics; // Tambahkan untuk dialog


public class Seller extends NPC{
    public Item[] itemsForSale;
    String[] actions = {"Talk", "Give", "Propose", "Marry", "Leave", "Sell"};
    

    public Seller (GamePanel gp, String name, String spawnMapName, int tileX, int tileY, Item[] loveditems, Item[] likedItems, Item[] hatedItems, Item[] itemsForSale) {
        super(gp, name, spawnMapName, tileX, tileY, loveditems, likedItems, hatedItems);
    }


    @Override
    public void drawActionMenu(Graphics2D g2) {
        int frameX = gp.tileSize * 1;
        int frameY = gp.tileSize * 8;
        int frameWidth = gp.tileSize * 14;
        int frameHeight = gp.tileSize * 3;

        drawSubwindow(g2, frameX, frameY, frameWidth, frameHeight);

        g2.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g2.getFontMetrics();

        int cols = 3;
        int rows = 2;
        int cellWidth = frameWidth / cols;
        int cellHeight = frameHeight / rows;

        for (int i = 0; i < actions.length; i++) {
            int col = i % cols;
            int row = i / cols;
            int x = frameX + col * cellWidth + (cellWidth - fm.stringWidth(actions[i])) / 2;
            int y = frameY + row * cellHeight + cellHeight / 2 + fm.getAscent() / 2;

            if (i == selectedActionIndex) {
                g2.setColor(Color.YELLOW);
            } else {
                g2.setColor(Color.WHITE);
            }
            g2.drawString(actions[i], x, y);
        }
    }

    @Override
    public void selectAction(boolean leftPressed, boolean rightPressed) {
        if (leftPressed) {
            selectedActionIndex--;
            if (selectedActionIndex < 0) {
                selectedActionIndex = actions.length - 1; // Loop ke akhir
            }
        } else if (rightPressed) {
            selectedActionIndex++;
            if (selectedActionIndex >= actions.length) {
                selectedActionIndex = 0; // Loop ke awal
            }
        }
    }




    
}
