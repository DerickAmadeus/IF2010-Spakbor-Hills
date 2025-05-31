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
        loadInitialSeeds();
        loadInitialCrops();
        loadInitialFood();
        loadMisc();
        loadProposalRing();
    }

    public Inventory<Item> getInventory() {
        return inventory;
    }

    public void loadInitialSeeds() {
        ArrayList<String> spring = new ArrayList<>(Arrays.asList("Spring"));
        ArrayList<String> summer = new ArrayList<>(Arrays.asList("Summer"));
        ArrayList<String> fall = new ArrayList<>(Arrays.asList("Fall"));
        ArrayList<String> wheatSeason = new ArrayList<>(Arrays.asList("Spring", "Fall"));

        Seeds parsnip = new Seeds("Parsnip Seeds", "Grows quickly in Spring", 10, 20, 1, spring, 13);
        Seeds cauliflower = new Seeds("Cauliflower Seeds", "Takes time but valuable", 40, 80, 5, spring, 14);
        Seeds potato = new Seeds("Potato Seeds", "Produces multiple potatoes", 25, 50, 3, spring, 15);
        Seeds wheat = new Seeds("Wheat Seeds", "Spring wheat crop", 30, 60, 1, wheatSeason, 16);

        Seeds blueberry = new Seeds("Blueberry Seeds", "Produces blueberries", 40, 80, 7, summer, 17);
        Seeds tomato = new Seeds("Tomato Seeds", "Popular summer crop", 25, 50, 3, summer, 18);
        Seeds hotPepper = new Seeds("Hot Pepper Seeds", "Grows quickly", 20, 40, 1, summer, 19);
        Seeds melon = new Seeds("Melon Seeds", "Large summer fruit", 40, 80, 4, summer, 20);

        Seeds cranberry = new Seeds("Cranberry Seeds", "Multiple harvests", 50, 100, 2, fall, 21);
        Seeds pumpkin = new Seeds("Pumpkin Seeds", "Big and valuable", 75, 150, 7, fall, 22);
        Seeds grape = new Seeds("Grape Seeds", "Climbing vine fruit", 30, 60, 3, fall, 23);

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
        Crops parsnip = new Crops("Parsnip", "Sayuran akar musim semi", 35, 50, 1);
        Crops cauliflower = new Crops("Cauliflower", "Sayuran bunga putih", 150, 200, 1);
        Crops wheat = new Crops("Wheat", "Serealia untuk dijadikan tepung", 30, 50, 3);
        Crops blueberry = new Crops("Blueberry", "Buah kecil biru musim panas", 40, 150, 3);
        Crops tomato = new Crops("Tomato", "Buah merah serbaguna", 60, 90, 1);
        Crops pumpkin = new Crops("Pumpkin", "Buah besar untuk musim gugur", 250, 300, 1);
        Crops grape = new Crops("Grape", "Buah ungu yang bisa dijadikan wine", 10, 100, 20);

        inventory.addItem(parsnip, 20);
        inventory.addItem(cauliflower, 20);
        inventory.addItem(wheat, 20);
        inventory.addItem(blueberry, 20);
        inventory.addItem(tomato, 20);
        inventory.addItem(pumpkin, 20);
        inventory.addItem(grape, 20);
    }

    public void loadInitialFood() {
        Food fishChips = new Food("Fish n' Chips", "Makanan goreng yang gurih", 135, 150, 50);
        Food baguette = new Food("Baguette", "Roti khas Prancis", 80, 100, 25);
        Food sashimi = new Food("Sashimi", "Irisan ikan mentah segar", 275, 300, 70);
        Food wine = new Food("Wine", "Minuman hasil fermentasi anggur", 90, 100, 20);
        Food pumpkinPie = new Food("Pumpkin Pie", "Pai labu manis dan lembut", 100, 120, 35);
        Food veggieSoup = new Food("Veggie Soup", "Sup sehat dari sayuran", 120, 140, 40);
        Food fishStew = new Food("Fish Stew", "Semur ikan hangat", 260, 280, 70);
        Food fishSandwich = new Food("Fish Sandwich", "Sandwich isi ikan", 180, 200, 50);
        Food pigHead = new Food("Cooked Pig's Head", "Kepala babi panggang spesial", 0, 1000, 100);
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
        Misc firewood = new Misc("Firewood", "ini firewood", 20, 40);
        Misc coal = new Misc("Coal", "ini coal", 20, 40);
        Misc egg = new Misc("Egg", "telor bgawk", 30, 60);
        Misc eggplant = new Misc("Eggplant", "terong..", 65, 130);

        inventory.addItem(firewood, 20);
        inventory.addItem(coal, 20);
        inventory.addItem(egg, 20);
        inventory.addItem(eggplant, 20);
    }

    public void loadProposalRing() {
        Equipment ring = new Equipment("Proposal Ring", "Use this to propose/marry your beloved ones.", 0, 10000);
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
