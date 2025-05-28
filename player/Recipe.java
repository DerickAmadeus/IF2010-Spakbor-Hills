package player;
import Items.Item;
import java.util.HashMap;

public class Recipe {
    private String itemID;
    private String name;
    private HashMap<Item, Integer> ingredients;
    private boolean unlockInfo = false;
    private boolean canCook = false;

    public Recipe(String itemID, String name) {
        this.itemID = itemID;
        this.name = name;
        ingredients = new HashMap<>();
    }

    public String getItemID() {
        return itemID;
    }

    public String getName() {
        return name;
    }

    public HashMap<Item, Integer> getIngredients() {
        return ingredients;
    }

    public boolean getUnlockInfo() {
        return unlockInfo;
    }
    
    public void setUnlockInfo(boolean info) {
        unlockInfo = info;    
    }
    public boolean getCanCook() {
        return canCook;
    }
    
    public void setCanCook(boolean info) {
        canCook = info;    
    }
}
