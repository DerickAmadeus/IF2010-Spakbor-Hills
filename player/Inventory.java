package player;

import Items.Item;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Inventory<T extends Item> {

    private HashMap<T, Integer> items;
    private ArrayList<T> itemContainer;

    private int scrollOffset = 0;
    private final int ITEMS_PER_ROW = 5;
    private final int SLOT_SIZE = 64;
    private final int SLOT_PADDING = 8;

    private final int VIEWPORT_HEIGHT = 300; // Tinggi area tampilan inventory (sama seperti height drawRect)

    public Inventory() {
        items = new HashMap<>();
        itemContainer = new ArrayList<>();
    }

    public void draw(Graphics2D g2) {
        int startX = 100;
        int startY = 100;
        int width = 400;
        int height = VIEWPORT_HEIGHT;

        // Background semi-transparan
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(startX, startY, width, height);

        // Border dan judul
        g2.setColor(Color.WHITE);
        g2.drawRect(startX, startY, width, height);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
        g2.drawString("Inventory", startX + 10, startY + 25);

        // Mulai menggambar grid item
        int x = startX + SLOT_PADDING;
        int y = startY + 40;

        int rowsVisible = (height - 40) / (SLOT_SIZE + SLOT_PADDING);
        int maxVisibleItems = rowsVisible * ITEMS_PER_ROW;

        ArrayList<T> itemsToShow = getItemContainer();
        int endIndex = Math.min(scrollOffset + maxVisibleItems, itemsToShow.size());

        for (int i = scrollOffset; i < endIndex; i++) {
            T item = itemsToShow.get(i);
            int row = (i - scrollOffset) / ITEMS_PER_ROW;
            int col = (i - scrollOffset) % ITEMS_PER_ROW;

            int boxX = x + (SLOT_SIZE + SLOT_PADDING) * col;
            int boxY = y + (SLOT_SIZE + SLOT_PADDING) * row;

            // Gambar kotak item
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(boxX, boxY, SLOT_SIZE, SLOT_SIZE);
            g2.setColor(Color.WHITE);
            g2.drawRect(boxX, boxY, SLOT_SIZE, SLOT_SIZE);

            // Nama item dan jumlah
            g2.setFont(g2.getFont().deriveFont(10f));
            g2.drawString(item.getName(), boxX + 5, boxY + 15);
            g2.drawString("x" + getItemCount(item), boxX + 5, boxY + 30);
        }
    }

    public void scrollUp() {
        if (scrollOffset - ITEMS_PER_ROW >= 0) {
            scrollOffset -= ITEMS_PER_ROW;
        }
    }

    public void scrollDown() {
        int totalItems = itemContainer.size();
        int rowsNeeded = (int) Math.ceil(totalItems / (double) ITEMS_PER_ROW);
        int totalHeight = rowsNeeded * (SLOT_SIZE + SLOT_PADDING);
        int maxOffset = Math.max(0, totalItems - getMaxVisibleItems());

        if (scrollOffset + ITEMS_PER_ROW < totalItems && scrollOffset + ITEMS_PER_ROW <= maxOffset) {
            scrollOffset += ITEMS_PER_ROW;
        }
    }

    private int getMaxVisibleItems() {
        return ((VIEWPORT_HEIGHT - 40) / (SLOT_SIZE + SLOT_PADDING)) * ITEMS_PER_ROW;
    }

    public T getItem(T key) {
        return items.containsKey(key) ? key : null;
    }

    public Integer getItemCount(T item) {
        return items.getOrDefault(item, 0);
    }

    public void addItem(T item, int count) {
        if (items.containsKey(item)) {
            int current = items.get(item);
            items.put(item, current + count);
        } else {
            items.put(item, count);
            itemContainer.add(item);
        }
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
