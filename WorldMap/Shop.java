package WorldMap;

import java.util.ArrayList;

public class Shop extends WorldBuilding {
    private ArrayList<Item> SelledItems;
    private NPC owner;

    public Shop(String buildingName, NPC owner) {
        super(buildingName);
        this.owner = owner;
        this.SelledItems = new ArrayList<>();
    }

    public void addItem(Item item) {
        SelledItems.add(item);
    }
    public void removeItem(Item item) {
        SelledItems.remove(item);
    }

    public void showItems() {
        System.out.println("Items available in " + getBuildingName() + ":");
        for (Item item : SelledItems) {
            System.out.println("- " + item.getName() + ": " + item.getDescription());
        }
    }

    @Override
    public void interact() {
        System.out.println("Welcome to " + getBuildingName() + "! I am " + owner.getName() + ".");
        showItems();
    }

    public void buyItem(Item item) {
        if (SelledItems.contains(item)) {
            System.out.println("You bought " + item.getName() + " from " + getBuildingName() + ".");
            removeItem(item);
            // Implementasi Tambahan sama Player
            // player.removeGold(item.getPrice());
            // player.addItem(item);
        } else {
            System.out.println("Item not available in the shop.");
        }
    }
    
}
