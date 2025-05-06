package Playerstuffs;

public class Recipe {
    private String itemID;
    private String name;
    // private List<item> ingredients;
    private boolean unlockInfo = false;

    public Recipe(String itemID, String name) {
        this.itemID = itemID;
        this.name = name;
        /*ingredients = new ArrayList<>()*/
    }

    public String getItemID() {
        return itemID;
    }

    public String getName() {
        return name;
    }

    public boolean getUnlockInfo() {
        return unlockInfo;
    }
    
    public void setUnlockInfo(boolean info) {
        unlockInfo = info;
    }
}
