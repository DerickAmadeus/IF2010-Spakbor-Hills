package Playerstuffs;

import Items.Item;
import java.util.ArrayList;
import java.util.HashMap;

public class Inventory<T extends Item> {
    private HashMap<T, Integer> items;
    private ArrayList<T> itemContainer;

    public Inventory() {
        items = new HashMap<>();
        itemContainer = new ArrayList<>();
    }

    public T getItem(T key) {
        if (items.containsKey(key)) {
            return key;
        } else {
            return null;
        }
    }

    public Integer getItemCount(T item) {
        if (items.containsKey(item)) {
            return items.get(item);
        } else {
            return 0;
        }
    }

    public void addItem(T item, int count) {
        if (items.containsKey(item)) {
            int current = items.get(item);
            items.put(item, current + count);
        } else {
            items.put(item, count);
        }
        itemContainer.add(item);
    }

    public void removeItem(T item, int count) {
        if (items.containsKey(item)) {
            int currentCount = items.get(item);
            if (count >= currentCount) {
                items.remove(item); 
                itemContainer.remove(item);
            } else {
                items.put(item, currentCount - count);
            }
        }
    }

    public ArrayList<T> getItemContainer() {
        return itemContainer;
    }
}
