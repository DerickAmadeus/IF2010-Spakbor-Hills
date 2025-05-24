package player;

import Items.Item;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import main.GamePanel;

public class Inventory<T extends Item> {

    private HashMap<T, Integer> items;
    private ArrayList<T> itemContainer;
    GamePanel gp;
    private int slotCol = 0;
    private int slotRow = 0;

    private int scrollOffset = 0;
    private final int ITEMS_PER_ROW = 5;
    private final int SLOT_SIZE = 64;
    private final int SLOT_PADDING = 8;

    private final int VIEWPORT_HEIGHT = 300; // Tinggi area tampilan inventory (sama seperti height drawRect)

    public Inventory(GamePanel gp) {
        items = new HashMap<>();
        itemContainer = new ArrayList<>();
        this.gp = gp;
    }

    public void drawInventory(Graphics2D g2) {
        // frame
        Color c = new Color(0,0,0, 210);
        int frameX = gp.tileSize*9;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize*6;
        int frameHeight = gp.tileSize*5;
        g2.setColor(c);
        g2.fillRoundRect(frameX, frameY, frameWidth, frameHeight, 35, 35);
        c = new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(frameX+5, frameY+5, frameWidth-10, frameHeight-10, 25, 25);

        //slot
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;

        // CURSOR
        int cursorX = slotXStart + (gp.tileSize * slotCol);
        int cursorY = slotYStart + (gp.tileSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        // DRAW CURSOR
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

        int index = 0;
        for (T item : itemContainer) {
            int col = index % ITEMS_PER_ROW;
            int row = index / ITEMS_PER_ROW;

            int itemX = slotXStart + col * gp.tileSize;
            int itemY = slotYStart + row * gp.tileSize;

            if (item.getIcon() != null) {
                int padding = 6;
                g2.drawImage(item.getIcon(), itemX, itemY, gp.tileSize, gp.tileSize, null);
            }

            // Gambarkan jumlah item
            Integer count = getItemCount(item);
            if (count != null && count > 1) {
                g2.setColor(Color.white);
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                String countStr = String.valueOf(count);
                int stringWidth = g2.getFontMetrics().stringWidth(countStr);
                int stringHeight = g2.getFontMetrics().getHeight();

                g2.drawString(countStr, itemX + gp.tileSize - stringWidth - 4, itemY + gp.tileSize - 4);
            }

            index++;
        }

    }

    public void updateInventoryCursor(boolean up, boolean down, boolean left, boolean right) {
        if (up && slotRow > 0) {
            slotRow--;
        }
        if (down && slotRow < getMaxRow()) {
            slotRow++;
        }
        if (left && slotCol > 0) {
            slotCol--;
        }
        if (right && slotCol < ITEMS_PER_ROW - 1) {
            slotCol++;
        }

    }

    private int getMaxRow() {
        int itemCount = 100;
        return (itemCount - 1) / ITEMS_PER_ROW; // Jumlah baris maksimum
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
