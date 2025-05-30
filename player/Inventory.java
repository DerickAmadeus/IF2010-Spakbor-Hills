package player;

import Items.*;
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
    private final int VIEWPORT_HEIGHT = 300; 
    private final int MAX_ROWS_ON_SCREEN = VIEWPORT_HEIGHT / SLOT_SIZE;

    // selecting
    public int selectedItemIndex = -1;
    public int optionCommandNum = 0; // untuk navigasi menu opsi


    public Inventory(GamePanel gp) {
        items = new HashMap<>();
        itemContainer = new ArrayList<>();
        this.gp = gp;
    }

    public void drawSubwindow(Graphics2D g2, int frameX, int frameY, int frameWidth, int frameHeight) {
        Color c = new Color(0,0,0, 210);
        g2.setColor(c);
        g2.fillRoundRect(frameX, frameY, frameWidth, frameHeight, 35, 35);
        c = new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(frameX+5, frameY+5, frameWidth-10, frameHeight-10, 25, 25);
    }

    public void drawInventory(Graphics2D g2) {
        // frame
        int frameX = gp.tileSize*9;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize*6;
        int frameHeight = gp.tileSize*5;
        drawSubwindow(g2, frameX, frameY, frameWidth, frameHeight);

        //slot
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;

        // CURSOR
        int cursorX = slotXStart + (gp.tileSize * slotCol);
        int cursorY = slotYStart + (gp.tileSize * (slotRow - scrollOffset));
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        // DRAW CURSOR
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

        // DRAW DESC WINDOW
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gp.tileSize * 3;
        drawSubwindow(g2, dFrameX, dFrameY, dFrameWidth, dFrameHeight);

        int index = 0;
        for (T item : itemContainer) {
            int row = index / ITEMS_PER_ROW;

            if (row < scrollOffset) {
                index++;
                continue; // Lewati baris di atas viewport
            }

            if (row >= scrollOffset + MAX_ROWS_ON_SCREEN) {
                break; 
            }

            int col = index % ITEMS_PER_ROW;
            int itemX = slotXStart + col * gp.tileSize;
            int itemY = slotYStart + (row - scrollOffset) * gp.tileSize; 

            if (item.getIcon() != null) {
                int padding = 6;
                int drawSize = gp.tileSize - 2 * padding;
                int drawX = itemX + padding;
                int drawY = itemY + padding;

                g2.drawImage(item.getIcon(), drawX, drawY, drawSize, drawSize, null);
            }

            Integer count = getItemCount(item);
            if (count != null && count > 1) {
                g2.setColor(Color.white);
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                String countStr = String.valueOf(count);
                int stringWidth = g2.getFontMetrics().stringWidth(countStr);
                g2.drawString(countStr, itemX + gp.tileSize - stringWidth - 4, itemY + gp.tileSize - 4);
            }

            index++;
        }

        int selectedIndex = slotRow * ITEMS_PER_ROW + slotCol;

        if (selectedIndex >= 0 && selectedIndex < itemContainer.size()) {
            T selectedItem = itemContainer.get(selectedIndex);
            if (selectedItem != null) {
                g2.setColor(Color.white);
                g2.setFont(new Font("Arial", Font.BOLD, 18));
                g2.drawString(selectedItem.getName(), dFrameX + 20, dFrameY + 30);

                g2.setFont(new Font("Arial", Font.PLAIN, 14));
                drawWrappedText(g2, selectedItem.getDesc(), dFrameX + 20, dFrameY + 55, dFrameWidth - 40, 18);
            }
        }
    }

    private void drawWrappedText(Graphics2D g2, String text, int x, int y, int maxWidth, int lineHeight) {
        FontMetrics metrics = g2.getFontMetrics();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int drawY = y;

        for (String word : words) {
            String testLine = line + word + " ";
            int lineWidth = metrics.stringWidth(testLine);
            if (lineWidth > maxWidth) {
                g2.drawString(line.toString(), x, drawY);
                line = new StringBuilder(word + " ");
                drawY += lineHeight;
            } else {
                line.append(word).append(" ");
            }
        }
        if (!line.toString().isEmpty()) {
            g2.drawString(line.toString(), x, drawY);
        }
    }
    public void drawItemOptionWindow(Graphics2D g2) {
        if (selectedItemIndex < 0 || selectedItemIndex >= itemContainer.size()) return;

        T item = itemContainer.get(selectedItemIndex);
        int x = 200, y = 100, w = 400, h = 150;

        drawSubwindow(g2, x, y, w, h);

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));

        int textX = x + 40;
        int textY = y + 50;
        String[] options;

        if (item instanceof Equipment) {
            options = new String[]{"Equip/Unequip", "Cancel"};
        } else if (item instanceof Seeds) {
            options = new String[]{"Hold/Put Out", "Cancel"};
        } else if (item instanceof Fish || item instanceof Crops || item instanceof Food){
            options = new String[]{"Eat", "Cancel"};
        } else {
            options = new String[]{"Cancel"};
        }

        for (int i = 0; i < options.length; i++) {
            if (i == optionCommandNum) {
                g2.setColor(Color.yellow);
            } else {
                g2.setColor(Color.white);
            }
            g2.drawString(options[i], textX, textY + (i * 40));
        }
    }

    public void drawShipping(Graphics2D g2) {
        // frame
        int frameX = gp.tileSize*9;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize*6;
        int frameHeight = gp.tileSize*5;
        drawSubwindow(g2, frameX, frameY, frameWidth, frameHeight);

        //slot
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;

        // CURSOR
        int cursorX = slotXStart + (gp.tileSize * slotCol);
        int cursorY = slotYStart + (gp.tileSize * (slotRow - scrollOffset));
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        // DRAW CURSOR
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

        // DRAW DESC WINDOW
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gp.tileSize * 3;
        drawSubwindow(g2, dFrameX, dFrameY, dFrameWidth, dFrameHeight);

        int index = 0;
        for (T item : itemContainer) {
            int row = index / ITEMS_PER_ROW;

            if (row < scrollOffset) {
                index++;
                continue; // Lewati baris di atas viewport
            }

            if (row >= scrollOffset + MAX_ROWS_ON_SCREEN) {
                break; // Hentikan kalau sudah melebihi viewport
            }

            int col = index % ITEMS_PER_ROW;
            int itemX = slotXStart + col * gp.tileSize;
            int itemY = slotYStart + (row - scrollOffset) * gp.tileSize; // kurangi offset agar scroll naik

            if (item.getIcon() != null) {
                int padding = 6;
                int drawSize = gp.tileSize - 2 * padding;
                int drawX = itemX + padding;
                int drawY = itemY + padding;

                g2.drawImage(item.getIcon(), drawX, drawY, drawSize, drawSize, null);
            }

            Integer count = getItemCount(item);
            if (count != null && count > 1) {
                g2.setColor(Color.white);
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                String countStr = String.valueOf(count);
                int stringWidth = g2.getFontMetrics().stringWidth(countStr);
                g2.drawString(countStr, itemX + gp.tileSize - stringWidth - 4, itemY + gp.tileSize - 4);
            }

            index++;
        }
        // AFTER drawing all items...

        // Hitung index dari cursor saat ini
        int selectedIndex = slotRow * ITEMS_PER_ROW + slotCol;

        if (selectedIndex >= 0 && selectedIndex < itemContainer.size()) {
            T selectedItem = itemContainer.get(selectedIndex);
            if (selectedItem != null) {
                // Gambar nama dan deskripsi
                g2.setColor(Color.white);
                g2.setFont(new Font("Arial", Font.BOLD, 18));
                g2.drawString(selectedItem.getName(), dFrameX + 20, dFrameY + 30);

                g2.setFont(new Font("Arial", Font.PLAIN, 14));
                // Bungkus teks deskripsi agar tidak keluar jendela
                drawWrappedText(g2, Integer.toString(selectedItem.getHargaJual()), dFrameX + 20, dFrameY + 55, dFrameWidth - 40, 18);
            }
        }
    }

    public void drawShippingOptionWindow(Graphics2D g2) {
        if (selectedItemIndex < 0 || selectedItemIndex >= itemContainer.size()) return;

        T item = itemContainer.get(selectedItemIndex);
        boolean validGiftingOption = !(item instanceof Equipment || item instanceof Seeds);
        int x = 200, y = 100, w = 400, h = 150;

        drawSubwindow(g2, x, y, w, h);

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));

        int textX = x + 40;
        int textY = y + 50;
        String[] options;
        String[] gifting;

        options = new String[]{"Sell", "Cancel"};
        gifting = new String[]{"Gift", "Cancel"};


        for (int i = 0; i < options.length; i++) {
            if (i == optionCommandNum) {
                g2.setColor(Color.yellow);
            } else {
                g2.setColor(Color.white);
            }
            if (gp.player.currentNPC.isGifted && validGiftingOption) {
                g2.drawString(gifting[i], textX, textY + (i * 40));
            } else if (!gp.player.currentNPC.isGifted){
                g2.drawString(options[i], textX, textY + (i * 40));
            }
        }

        if(!validGiftingOption) {
            g2.drawString("You cannot gift " + item.getClass().getSimpleName() + "!", textX, textY);
        }
    }

    public void selectCurrentItemShipping() {
        int selectedIndex = slotRow * ITEMS_PER_ROW + slotCol;
        if (selectedIndex >= 0 && selectedIndex < itemContainer.size()) {
            selectedItemIndex = selectedIndex; 
            optionCommandNum = 0; 
            gp.gameState = gp.shippingOptionState; 
        }
    }

    public void updateInventoryCursor(boolean up, boolean down, boolean left, boolean right) {
        int maxIndex = getItemCountTotal() - 1;

        if (up && slotRow > 0) {
            slotRow--;
            if (slotRow < scrollOffset) {
                scrollOffset = slotRow;
            }
        }
        if (down) {
            int nextIndex = (slotRow + 1) * ITEMS_PER_ROW + slotCol;
            if (nextIndex <= maxIndex) {
                slotRow++;
                if (slotRow >= scrollOffset + MAX_ROWS_ON_SCREEN) {
                    scrollOffset = slotRow - MAX_ROWS_ON_SCREEN + 1;
                }
            }
        }
        if (left && slotCol > 0) {
            slotCol--;
        }
        if (right) {
            if (slotCol < ITEMS_PER_ROW - 1) {
                int nextIndex = slotRow * ITEMS_PER_ROW + slotCol + 1;
                if (nextIndex <= maxIndex) {
                    slotCol++;
                }
            }
        }

        if ((slotRow * ITEMS_PER_ROW + slotCol) > maxIndex) {
            slotCol = maxIndex % ITEMS_PER_ROW;
            slotRow = maxIndex / ITEMS_PER_ROW;
        }
    }
    public void selectCurrentItem() {
        int selectedIndex = slotRow * ITEMS_PER_ROW + slotCol;
        if (selectedIndex >= 0 && selectedIndex < itemContainer.size()) {
            selectedItemIndex = selectedIndex; 
            optionCommandNum = 0; 
            gp.gameState = gp.itemOptionState; 
        }
    }

    private int getItemCountTotal() {
        return itemContainer.size();
    }

    public T getSelectedItem() {
        if (selectedItemIndex >= 0 && selectedItemIndex < itemContainer.size()) {
            return itemContainer.get(selectedItemIndex);
        }
        return null;
    }

    public T getItem(T key) {
        return items.containsKey(key) ? key : null;
    }

    public Integer getItemCount(T item) {
        return items.getOrDefault(item, 0);
    }

    public boolean hasItem(T item) {
        return items.containsKey(item) && items.get(item) > 0;
    }

    public boolean hasItemOfClass(Class<?> itemClass, int amount) {
        for (T item : items.keySet()) {
            if (itemClass.isInstance(item) && items.get(item) >= amount) {
                return true;
            }
        }
        return false;
    }

    public HashMap<Item, Integer> getAllItemOfClass(Class<?> itemClass) {
        HashMap<Item, Integer> container = new HashMap<>();
        for (Item item : items.keySet()) {
            if (itemClass.isInstance(item)) {
                container.put(item, items.get(item));
            }
        }
        return container;
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
