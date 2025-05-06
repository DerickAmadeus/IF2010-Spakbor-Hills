import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private char gender;
    private int energy;
    private static int maxEnergy = 100;
    private String farmName;
    private /*NPC */ String partner = null;
    private int gold = 0;
    // private Inventory inventory;
    // private Point location;
    private List<Recipe> ownedRecipe;

    public Player(String name, char gender, String farmName) {
        this.name = name;
        this.gender = gender;
        energy = maxEnergy;
        this.farmName = farmName;
        // construct inventory
        // construct point
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

    public /*NPC */ String getPartner() {
        return partner;
    }

    public int getGold() {
        return gold;
    }

    public String getFarmName() {
        return farmName;
    }

    /*public Inventory getInventory() {
        return inventory;
    }*/

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

    public void setEnergy(int energy) { // throws exception di kelas yang make
        if (energy > maxEnergy) {
            this.energy = maxEnergy;
        } else if (energy < 0) { // lupa actual valuenya brp
            this.energy = 0;
        } else {
            this.energy = energy;
        }
    }

    public void setPartner(String partner) {
        this.partner = partner;
    } //NPC

    public void setGold(int gold) { // throws exception di kelas yang make
        this.gold = gold; // gold ga valid bakal dihandle di buying
    }

    /*public void setInventory() {
        hdeh
    }*/

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

    public void printInventory() {}

    public void proposing() {}

    public void marry() {}

    public void visiting() {}

    public void chatting() {}

    public void gifting() {}

    public void moving() {}

    public void openInventory() {}
}
