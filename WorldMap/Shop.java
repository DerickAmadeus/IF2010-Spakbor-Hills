package WorldMap;
import Items.Item;
import NPC.NPC;
import Playerstuffs.Player;
import Playerstuffs.Inventory;

import java.util.ArrayList;

public class Shop<T extends Item> extends WorldBuilding {
    private ArrayList<T> SelledItems;
    private NPC owner;

    public Shop(String buildingName, NPC owner) {
        super(buildingName);
        this.owner = owner;
        this.SelledItems = new ArrayList<>();
    }

    public void addItem(T item) {
        SelledItems.add(item);
    }
    public void removeItem(T item) {
        SelledItems.remove(item);
    }

    public void showItems() {
        System.out.println("Items available in " + getBuildingName() + ":");
        for (T item : SelledItems) {
            System.out.println("- " + item.getName() + ": " + item.getDesc());
        }
    }

    @Override
    public void interact() {
        System.out.println("Welcome to " + getBuildingName() + "! I am " + owner.getName() + ".");
        showItems();
    }

    public void buyItem(Player player, T item) {
        if (SelledItems.contains(item)) {
            System.out.println("You bought " + item.getName() + " from " + getBuildingName() + ".");
            removeItem(item);
            // Implementasi Tambahan sama Player
            // player.removeGold(item.getPrice());
            player.getInventory().addItem(item, 1);
            player.setGold(player.getGold() - item.getHargaBeli());
        } else if (player.getGold() < item.getHargaBeli()) {
            System.out.println("You don't have enough gold to buy " + item.getName() + "."); 
        } else {
            System.out.println(item.getName() + " is not available in " + getBuildingName() + ".");
        }
    }
    
}
