package Map;
import Items.Item;
import main.GamePanel;
import player.Inventory;


public class ShippingBin extends Tile {
    
    public int maxSlot = 16; 
    public int binCount; 
    public int moneyStored; 
    public int lastday;
    public int emptyMarkr;
    protected Inventory<Item> inventory;
    GamePanel gp;
    
    public Inventory<Item> getInventory() {
        return inventory;
    }

    public ShippingBin(String name, boolean isWalkable, GamePanel gp) {
        super(name, isWalkable);
        this.binCount = 0; 
        this.moneyStored = 0; 
        this.lastday = 0;
        this.gp = gp;
        this.inventory = new Inventory<>(gp);
        this.emptyMarkr = 0;
    }

    public ShippingBin(ShippingBin other) {
        super(other);
        this.binCount = other.binCount;
        this.moneyStored = other.moneyStored;
        this.lastday = other.lastday;
        this.gp = other.gp;
        this.inventory = other.inventory;
        this.emptyMarkr = other.emptyMarkr;
    }

    public void addItem(Item item, int quantity) {
        if (binCount < maxSlot || inventory.hasItem(item)) {
            inventory.addItem(item, quantity);
        } else {
            System.out.println("Shipping bin is full!");
        }
    }

    public void clearBin() {
        inventory.getItems().clear();
        inventory.getItemContainer().clear();
        binCount = 0;
        moneyStored = 0;
        lastday = 0;
        emptyMarkr = 1;
    }
}
