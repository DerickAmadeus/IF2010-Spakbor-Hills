package Playerstuffs;
import java.util.ArrayList;
import java.util.List;
import NPC.NPC;
import player.Inventory;
import Items.Item;

public class Player {
    private String name;
    private char gender;
    private int energy;
    private static final int MAX_ENERGY = 100; 
    private String farmName;
    private NPC partner = null;
    private int gold = 0;
    private Inventory<Item> inventory;
    //private Point location;
    private List<Recipe> ownedRecipe;

    public Player(String name, char gender, String farmName) {
        this.name = name;
        this.gender = gender;
        energy = MAX_ENERGY;
        this.farmName = farmName;
        inventory = new Inventory<>();
        // location
        ownedRecipe = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public char getGender() {
        return gender;
    }

    public int getEnergy() {
        return energy;
    }

    public NPC getPartner() {
        return partner;
    }

    public int getGold() {
        return gold;
    }

    public String getFarmName() {
        return farmName;
    }

    public Inventory<Item> getInventory() {
        return inventory;
    }

    /*public Point getLocation() {
        return location;
    }*/

    public List<Recipe> getRecipe() {
        return ownedRecipe;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public void setEnergy(int energy) {
        if (energy > MAX_ENERGY) {
            this.energy = MAX_ENERGY;
        } else if (energy < 0 && energy > -20) {
            // masih print
            System.out.println("Warning: Energy is low! Action can still be performed, but consider sleeping.");
            this.energy = energy;
        } else if (energy < -20) {
            // msdih print
            System.out.println("Error: Energy is too low! sleeping rn...");
        } else {
            this.energy = energy;
        }
    }
    

    public void setPartner(NPC partner) {
        this.partner = partner;
    } 

    public void setGold(int gold) { 
        this.gold = gold; // gold ga valid bakal dihandle di buying
    }

    /*public Point setLocation() {
        ntr
    }*/

    public void setRecipe() {
        for (int i = 0; i < ownedRecipe.size(); i++) {
            if (!ownedRecipe.get(i).getUnlockInfo()) {
                ownedRecipe.get(i).setUnlockInfo(true);
            }
        }
    }

    public void printInventory() {
        if (inventory.getItemContainer().isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("Your inventory:");
            // Loop untuk menampilkan item dan jumlahnya
            for (Item item : inventory.getItemContainer()) {
                int count = inventory.getItemCount(item); // Mengambil jumlah item dalam inventory
                System.out.println(item.getName() + " - " + count);
            }
        }
    }

    public void proposing() {}

    public void marry() {}

    public void visiting() {}

    public void chatting() {}

    public void gifting() {}

    public void moving() {}

    public void openInventory() {}
}
