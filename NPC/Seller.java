package NPC;

import main.GamePanel;
import player.Inventory;
import Items.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Font; 
import java.awt.FontMetrics; 


public class Seller extends NPC{  
    public boolean isBuying = false;
    public Seller (GamePanel gp, String name, String spawnMapName, int tileX, int tileY, Item[] loveditems, Item[] likedItems, Item[] hatedItems) {
        super(gp, name, spawnMapName, tileX, tileY, loveditems, likedItems, hatedItems);
        String[] actions = {"Talk", "Give", "Propose", "Marry", "Leave", "Buy"};
        this.setActions(actions);
        if (name == "Emily"){ 
            loadInitialSeeds();
            loadInitialCrops();
            loadInitialFood();
            loadProposalRing();
        } else if (name == "Caroline"){
            loadMisc();
        }
    }

    public Inventory<Item> getInventory() {
        return inventory;
    }

    public void loadInitialSeeds() {
        ArrayList<String> spring = new ArrayList<>(Arrays.asList("Spring"));
        ArrayList<String> summer = new ArrayList<>(Arrays.asList("Summer"));
        ArrayList<String> fall = new ArrayList<>(Arrays.asList("Fall"));
        ArrayList<String> wheatSeason = new ArrayList<>(Arrays.asList("Spring", "Fall"));

        Seeds parsnip = new Seeds("Parsnip Seeds", 10, 20, 1, spring, 13);
        Seeds cauliflower = new Seeds("Cauliflower Seeds", 40, 80, 5, spring, 14);
        Seeds potato = new Seeds("Potato Seeds", 25, 50, 3, spring, 15);
        Seeds wheat = new Seeds("Wheat Seeds", 30, 60, 1, wheatSeason, 16);

        Seeds blueberry = new Seeds("Blueberry Seeds", 40, 80, 7, summer, 17);
        Seeds tomato = new Seeds("Tomato Seeds", 25, 50, 3, summer, 18);
        Seeds hotPepper = new Seeds("Hot Pepper Seeds", 20, 40, 1, summer, 19);
        Seeds melon = new Seeds("Melon Seeds", 40, 80, 4, summer, 20);

        Seeds cranberry = new Seeds("Cranberry Seeds", 50, 100, 2, fall, 21);
        Seeds pumpkin = new Seeds("Pumpkin Seeds", 75, 150, 7, fall, 22);
        Seeds grape = new Seeds("Grape Seeds", 30, 60, 3, fall, 23);

        inventory.addItem(parsnip, 20);
        inventory.addItem(cauliflower, 20);
        inventory.addItem(potato, 20);
        inventory.addItem(wheat, 20);
        inventory.addItem(blueberry, 20);
        inventory.addItem(tomato, 20);
        inventory.addItem(hotPepper, 20);
        inventory.addItem(melon, 20);
        inventory.addItem(cranberry, 20);
        inventory.addItem(pumpkin, 20);
        inventory.addItem(grape, 20);
    }

    public void loadInitialCrops() {
        Crops parsnip = new Crops("Parsnip", 35, 50, 1);
        Crops cauliflower = new Crops("Cauliflower", 150, 200, 1);
        Crops wheat = new Crops("Wheat", 30, 50, 3);
        Crops blueberry = new Crops("Blueberry", 40, 150, 3);
        Crops tomato = new Crops("Tomato", 60, 90, 1);
        Crops pumpkin = new Crops("Pumpkin", 250, 300, 1);
        Crops grape = new Crops("Grape", 10, 100, 20);

        inventory.addItem(parsnip, 20);
        inventory.addItem(cauliflower, 20);
        inventory.addItem(wheat, 20);
        inventory.addItem(blueberry, 20);
        inventory.addItem(tomato, 20);
        inventory.addItem(pumpkin, 20);
        inventory.addItem(grape, 20);
    }

    public void loadInitialFood() {
        Food fishChips = new Food("Fish n' Chips",  135, 150, 50);
        Food baguette = new Food("Baguette", 80, 100, 25);
        Food sashimi = new Food("Sashimi", 275, 300, 70);
        Food wine = new Food("Wine", 90, 100, 20);
        Food pumpkinPie = new Food("Pumpkin Pie", 100, 120, 35);
        Food veggieSoup = new Food("Veggie Soup", 120, 140, 40);
        Food fishStew = new Food("Fish Stew", 260, 280, 70);
        Food fishSandwich = new Food("Fish Sandwich", 180, 200, 50);
        Food pigHead = new Food("Cooked Pig's Head", 0, 1000, 100);
        inventory.addItem(fishChips, 20);
        inventory.addItem(baguette, 20);
        inventory.addItem(sashimi, 20);
        inventory.addItem(wine, 20);
        inventory.addItem(pumpkinPie, 20);
        inventory.addItem(veggieSoup, 20);
        inventory.addItem(fishStew, 20);
        inventory.addItem(fishSandwich, 20);
        inventory.addItem(pigHead, 20);
    }

    public void loadMisc() {
        Misc firewood = new Misc("Firewood", "Sell Price: 10 | Buy Price: 20. Use this to cook a meal." , 10, 20);
        Misc coal = new Misc("Coal", "Sell Price: 20 | Buy Price: 40. Use this to cook 2 meals (the ingredients will be doubled).", 20, 40);
        Misc egg = new Misc("Egg", "Sell Price: 30 | Buy Price: 60. Can be used to make Pumpkin Pie.", 30, 60);
        Misc eggplant = new Misc("Eggplant", "Sell Price: 65 | Buy Price: 135. Can be used to make The Legends of Spakbor.", 65, 130);

        inventory.addItem(firewood, 20);
        inventory.addItem(coal, 20);
        inventory.addItem(egg, 20);
        inventory.addItem(eggplant, 20);
    }

    public void loadProposalRing() {
        Equipment ring = new Equipment("Proposal Ring", "Buy Price: 10000. Use this to propose/marry your beloved ones.", 0, 10000);
        inventory.addItem(ring, 1);
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

        for (int i = 0; i < getActions().length; i++) {
            int col = i % cols;
            int row = i / cols;
            int x = frameX + col * cellWidth + (cellWidth - fm.stringWidth(getActions()[i])) / 2;
            int y = frameY + row * cellHeight + cellHeight / 2 + fm.getAscent() / 2;

            if (i == selectedActionIndex) {
                g2.setColor(Color.YELLOW);
            } else {
                g2.setColor(Color.WHITE);
            }
            g2.drawString(getActions()[i], x, y);
        }
    }

    @Override
    public void selectAction(boolean leftPressed, boolean rightPressed) {
        if (leftPressed) {
            selectedActionIndex--;
            if (selectedActionIndex < 0) {
                selectedActionIndex = getActions().length - 1; // Loop ke akhir
            }
        } else if (rightPressed) {
            selectedActionIndex++;
            if (selectedActionIndex >= getActions().length) {
                selectedActionIndex = 0; // Loop ke awal
            }
        }
    }




    
}
