package player;
import Items.Item;
import Items.Food;
import java.util.HashMap;

public class Recipe {
    private String itemID;
    private Food food;
    private HashMap<Item, Integer> ingredients;
    private boolean unlockInfo = false;

    public Recipe(String itemID, Food food) {
        this.itemID = itemID;
        this.food = food;
        ingredients = new HashMap<>();
    }

    public String getItemID() {
        return itemID;
    }

    public Food getFood() {
        return food;
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
}
