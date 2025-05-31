package NPC;

import main.GamePanel;
import player.Inventory;
import Items.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.BasicStroke;

public class NPC {
    public int worldX, worldY;
    public Rectangle hitbox;
    public Rectangle interactionTriggerArea;

    GamePanel gp;

    public String[] dialogues;
    public int currentDialogueIndex = 0;
    private BufferedImage[] idleFrames;
    private int spriteCounter = 0;
    private int spriteNum = 0;
    private final int ANIMATION_SPEED = 15;
    private final int IDLE_FRAME_COUNT = 4;
    private boolean showActionMenu = false;
    private String[] actions = { "Talk", "Give", "Propose", "Marry", "Leave" };
    public int selectedActionIndex = 0;
    private String[] proposingAnswers = { "AAWWWWWWWWWWW SO SWEEETTTT. AKU MAUUUUUUUUUU", "Dih Effort Dulu Bang",
            "Dah kau lamar bang aku", "Dah nikah kita" }; // Contoh jawaban untuk pertanyaan pernikahan
    public String[] giftingAnswers = { "Wow! I love this! Thank you so much!", "I like this, thanks!", "Appreciated.",
            "Is this a joke..." };
    public String[] marriageAnswers = { "I do! Let's get married!", "I'm not ready for marriage yet.", "No.",
            "Don't cheat on your wife!" };
    public String name;
    public String spawnMapName;
    private int heartPoints;
    private Item[] lovedItems;
    private Item[] likedItems;
    private Item[] hatedItems;
    private String relationship;
    private int daysCanMarry; // 0 = no, 1 = yes, 2 = already married
    protected Inventory<Item> inventory;
    public boolean isTalking = false;
    public boolean isProposed = false;
    public boolean isGifted = false;
    public boolean isMarried = false;

    public NPC(GamePanel gp, String name, String spawnMapName, int tileX, int tileY, Item[] loveditems,
            Item[] likedItems, Item[] hatedItems) {
        this.gp = gp;
        inventory = new Inventory<>(gp);
        this.name = name;
        heartPoints = 0;
        this.lovedItems = loveditems;
        this.likedItems = likedItems;
        this.hatedItems = hatedItems;
        this.selectedActionIndex = 0;
        this.spawnMapName = spawnMapName;
        this.worldX = tileX * gp.tileSize;
        this.worldY = tileY * gp.tileSize;
        this.hitbox = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        daysCanMarry = 0; // Default value, can be set later

        this.interactionTriggerArea = new Rectangle(worldX, worldY, gp.tileSize, gp.tileSize);
        loadIdleAnimation();
        setDefaultDialogues();
    }

    private void setDefaultDialogues() {
        if (this.name.equalsIgnoreCase("Mayor Tadi")) {
            dialogues = new String[] {
                    "Spakbor Hills is becoming quite classy, isn't it? Of course, it's all thanks to my high standards.",
                    "Hmph, an item like that... you'd best not show it to me. My taste is only for the finest and rarest things.",
                    "As mayor, I am always on the lookout for something... special. Something that reflects the opulence of Spakbor Hills."
            };
        } else if (this.name.equalsIgnoreCase("Caroline")) {
            dialogues = new String[] {
                    "This piece of scrap wood? In my hands, it can become a work of art! Recycling is important, you know.",
                    "I'm working on a new order. Oh, by the way, if you have any spare Firewood or Coal, I can always make good use of them.",
                    "Spicy food? Oh dear, please no! My stomach would protest immediately. I prefer something plain."
            };
        } else if (this.name.equalsIgnoreCase("Emily")) {
            dialogues = new String[] {
                    "Welcome! Today's special is made with ingredients straight from my own garden. Fresh and healthy!",
                    "New seeds? I'd be delighted to plant them! Or perhaps some fresh fish? That would be a wonderful addition to today's soup.",
                    "Feel free to look around the store. Besides food, we also have plenty of tools and other things you might need!"
            };
        } else if (this.name.equalsIgnoreCase("Dasco")) {
            dialogues = new String[] {
                    "Welcome to my world,where luxury and luck meet. Care to try your fortune?",
                    "I can't stop winning!",
                    "GACOR KANGGG!!!!!"
            };
        } else if (this.name.equalsIgnoreCase("Abigail")) {
            dialogues = new String[] {
                "Time for an adventure! I'm planning to explore the northern caves today. I'll need a lot of energy!",
                "Fruits are the best source of energy! Blueberries, Melons... anything sweet and refreshing will do!",
                "Vegetables? Hmm, no thanks. I'd rather save my pack space for fruits. They're much more convenient for hiking!"
            };
        } else if (this.name.equalsIgnoreCase("Perry")) {
            dialogues = new String[] {
                "Oh, hello... Sorry, I was just trying to find some inspiration for... the next chapter. Spakbor Hills is so peaceful.",
                "My novel? Someone's reading it? That's... a relief. Thank you for asking.",
                "I quite enjoy strolling around here looking for berries. As for fish... I'm sorry, I'm not really... fond of their smell."
            };
        } else {
            dialogues = new String[] { "Hello there! How can I help you today?" };
        }
    }

    public Item[] getLovedItems() {
        return lovedItems;
    }

    public Item[] getLikedItems() {
        return likedItems;
    }

    public Item[] getHatedItems() {
        return hatedItems;
    }

    public void interact() {
        if (!showActionMenu) {
            showActionMenu = true;
            selectedActionIndex = 0;
        }
    }

    public String[] getActions() {
        return actions;
    }

    public void setActions(String[] actions) {
        this.actions = actions;
    }

    private void loadIdleAnimation() {
        idleFrames = new BufferedImage[IDLE_FRAME_COUNT];

        System.out.println("Loading animation for NPC: " + name);
        for (int i = 0; i < IDLE_FRAME_COUNT; i++) {
            String imagePath = "/NPC/" + name + "/idle_" + i + ".png";
            try {
                InputStream is = getClass().getResourceAsStream(imagePath);
                if (is == null) {
                    System.err.println("Cannot load NPC image: " + imagePath + " (InputStream is null)");
                    idleFrames[i] = createPlaceholderImage();
                } else {
                    idleFrames[i] = ImageIO.read(is);
                    System.out.println("Loaded: " + imagePath);
                    is.close();
                }
            } catch (IOException e) {
                System.err.println("Error loading NPC image: " + imagePath);
                e.printStackTrace();
                idleFrames[i] = createPlaceholderImage();
            }
        }
    }

    private BufferedImage createPlaceholderImage() {
        BufferedImage placeholder = new BufferedImage(gp.tileSize, gp.tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = placeholder.createGraphics();
        g.setColor(Color.MAGENTA);
        g.fillRect(0, 0, gp.tileSize, gp.tileSize);
        g.setColor(Color.BLACK);
        g.drawString("NPC?", gp.tileSize / 4, gp.tileSize / 2 + 5);
        g.dispose();
        return placeholder;
    }

    public void update() {
        spriteCounter++;
        if (spriteCounter > ANIMATION_SPEED) {
            spriteNum++;
            if (spriteNum >= IDLE_FRAME_COUNT) {
                spriteNum = 0;
            }
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage imageToDraw = null;
        if (idleFrames != null && idleFrames.length > 0 && spriteNum < idleFrames.length
                && idleFrames[spriteNum] != null) {
            imageToDraw = idleFrames[spriteNum];
        } else if (idleFrames != null && idleFrames.length > 0 && idleFrames[0] != null) {
            imageToDraw = idleFrames[0];
        } else {
            imageToDraw = createPlaceholderImage();
        }

        int screenX = worldX - gp.player.x + gp.player.screenX;
        int screenY = worldY - gp.player.y + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.x - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.x + gp.player.screenX + gp.screenWidth &&
                worldY + gp.tileSize > gp.player.y - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.y + gp.player.screenY + gp.screenHeight) {
            g2.drawImage(imageToDraw, screenX, screenY, gp.tileSize, gp.tileSize, null);
            if (gp.debugMode) {
                g2.setColor(new Color(255, 0, 255, 100));
                g2.drawRect(screenX + hitbox.x, screenY + hitbox.y, hitbox.width, hitbox.height);
                Rectangle actualTriggerArea = getInteractionTriggerAreaWorld();
                int triggerScreenX = actualTriggerArea.x - gp.player.x + gp.player.screenX;
                int triggerScreenY = actualTriggerArea.y - gp.player.y + gp.player.screenY;
                g2.setColor(new Color(0, 255, 255, 80));
                g2.drawRect(triggerScreenX, triggerScreenY, actualTriggerArea.width, actualTriggerArea.height);

                g2.setColor(Color.WHITE);
                g2.drawString(name, screenX, screenY - 5);
            }
        }
    }

    public String getSpawnMapName() {
        return spawnMapName;
    }

    public Rectangle getInteractionTriggerAreaWorld() {
        return new Rectangle(worldX + hitbox.x, worldY + hitbox.y, hitbox.width, hitbox.height);
    }

    public void drawSubwindow(Graphics2D g2, int frameX, int frameY, int frameWidth, int frameHeight) {
        Color c = new Color(53, 33, 0, 255);
        g2.setColor(c);
        g2.fillRoundRect(frameX, frameY, frameWidth, frameHeight, 35, 35);
        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(frameX + 5, frameY + 5, frameWidth - 10, frameHeight - 10, 25, 25);
    }

    public void showStatus(Graphics2D g2) {
        int frameX = gp.tileSize * 9;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 8;
        drawSubwindow(g2, frameX, frameY, frameWidth, frameHeight);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String statusText = "NPC: " + name;
        statusText += " (" + (relationship != null ? relationship : "Single") + ")";

        g2.setColor(Color.WHITE);
        g2.drawString(statusText, frameX + 20, frameY + 40);
        g2.setStroke(new BasicStroke(2));

        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        String heartPointsText = "Heart Points: " + heartPoints;

        if (heartPoints == 150) {
            g2.setColor(Color.pink);
        } else {
            g2.setColor(Color.WHITE);
        }
        g2.drawString(heartPointsText, frameX + 20, frameY + 80);
        g2.setColor(Color.WHITE);

        int y = frameY + 100;
        FontMetrics fm = g2.getFontMetrics();
        int maxWidth = frameWidth - 40;
        int x = frameX + 20;

        if (lovedItems != null && lovedItems.length > 0) {
            String lovedItemsText = "Loved Items: ";
            for (Item item : lovedItems) {
                lovedItemsText += item.getName() + ", ";
            }
            if (lovedItemsText.endsWith(", ")) {
                lovedItemsText = lovedItemsText.substring(0, lovedItemsText.length() - 2);
            }
            String[] words = lovedItemsText.split(" ");
            StringBuilder line = new StringBuilder();
            for (String word : words) {
                String testLine = line + (line.length() == 0 ? "" : " ") + word;
                if (fm.stringWidth(testLine) > maxWidth) {
                    g2.drawString(line.toString(), x, y);
                    y += fm.getHeight() + 4;
                    line = new StringBuilder(word);
                } else {
                    if (line.length() > 0)
                        line.append(" ");
                    line.append(word);
                }
            }
            if (line.length() > 0) {
                g2.drawString(line.toString(), x, y);
                y += fm.getHeight() + 8;
            }
        }

        if (likedItems != null && likedItems.length > 0) {
            String likedItemsText = "Liked Items: ";
            for (Item item : likedItems) {
                likedItemsText += item.getName() + ", ";
            }
            if (likedItemsText.endsWith(", ")) {
                likedItemsText = likedItemsText.substring(0, likedItemsText.length() - 2);
            }

            String[] words = likedItemsText.split(" ");
            StringBuilder line = new StringBuilder();
            for (String word : words) {
                String testLine = line + (line.length() == 0 ? "" : " ") + word;
                if (fm.stringWidth(testLine) > maxWidth) {
                    g2.drawString(line.toString(), x, y);
                    y += fm.getHeight() + 4;
                    line = new StringBuilder(word);
                } else {
                    if (line.length() > 0)
                        line.append(" ");
                    line.append(word);
                }
            }
            if (line.length() > 0) {
                g2.drawString(line.toString(), x, y);
                y += fm.getHeight() + 8;
            }
        }

        if (hatedItems != null && hatedItems.length > 0) {
            String hatedItemsText = "Hated Items: ";
            for (Item item : hatedItems) {
                hatedItemsText += item.getName() + ", ";
            }
            if (hatedItemsText.endsWith(", ")) {
                hatedItemsText = hatedItemsText.substring(0, hatedItemsText.length() - 2);
            }

            String[] words = hatedItemsText.split(" ");
            StringBuilder line = new StringBuilder();
            for (String word : words) {
                String testLine = line + (line.length() == 0 ? "" : " ") + word;
                if (fm.stringWidth(testLine) > maxWidth) {
                    g2.drawString(line.toString(), x, y);
                    y += fm.getHeight() + 4;
                    line = new StringBuilder(word);
                } else {
                    if (line.length() > 0)
                        line.append(" ");
                    line.append(word);
                }
            }
            if (line.length() > 0) {
                g2.drawString(line.toString(), x, y);
                y += fm.getHeight() + 8;
            }
        } else {
            String hatedItemsText = "Hated Items: Seluruh item yang bukan merupakan lovedItems dan likedItems.";
            String[] words = hatedItemsText.split(" ");
            StringBuilder line = new StringBuilder();
            for (String word : words) {
                String testLine = line + (line.length() == 0 ? "" : " ") + word;
                if (fm.stringWidth(testLine) > maxWidth) {
                    g2.drawString(line.toString(), x, y);
                    y += fm.getHeight() + 4;
                    line = new StringBuilder(word);
                } else {
                    if (line.length() > 0)
                        line.append(" ");
                    line.append(word);
                }
            }
            if (line.length() > 0) {
                g2.drawString(line.toString(), x, y);
                y += fm.getHeight() + 8;
            }
            y += fm.getHeight() + 8;
        }

    }

    public void drawActionMenu(Graphics2D g2) {
        int frameX = gp.tileSize * 1;
        int frameY = gp.tileSize * 8;
        int frameWidth = gp.tileSize * 14;
        int frameHeight = gp.tileSize * 3;

        drawSubwindow(g2, frameX, frameY, frameWidth, frameHeight);

        g2.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g2.getFontMetrics();

        int cols = 3;
        int rows = 2;
        int cellWidth = frameWidth / cols;
        int cellHeight = frameHeight / rows;

        for (int i = 0; i < actions.length; i++) {
            int col = i % cols;
            int row = i / cols;
            int x = frameX + col * cellWidth + (cellWidth - fm.stringWidth(actions[i])) / 2;
            int y = frameY + row * cellHeight + cellHeight / 2 + fm.getAscent() / 2;

            if (i == selectedActionIndex) {
                g2.setColor(Color.YELLOW);
            } else {
                g2.setColor(Color.WHITE);
            }
            g2.drawString(actions[i], x, y);
        }
    }

    public void selectAction(boolean leftPressed, boolean rightPressed) {
        if (leftPressed) {
            selectedActionIndex--;
            if (selectedActionIndex < 0) {
                selectedActionIndex = actions.length - 1;
            }
        } else if (rightPressed) {
            selectedActionIndex++;
            if (selectedActionIndex >= actions.length) {
                selectedActionIndex = 0;
            }
        }
    }

    public String confirmAction() {
        return actions[selectedActionIndex];
    }

    public void drawNPCDialog(Graphics2D g2, String speakerName) {
        // Ukuran dan posisi jendela dialog
        int x = gp.tileSize * 1;
        int y = gp.tileSize * 8;
        int width = gp.tileSize * 14;
        int height = gp.tileSize * 3;

        // Gambar latar belakang dialog
        drawSubwindow(g2, x, y, width, height);

        // Atur font dan warna teks
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.setColor(Color.WHITE);

        // Padding teks
        int paddingX = 20;
        int paddingY = 30;

        // Gambar nama pembicara
        g2.drawString(speakerName + ":", x + paddingX, y + paddingY);

        // Cek apakah indeks dialog valid
        if (currentDialogueIndex < dialogues.length) {
            // Word wrap dialog agar tidak keluar border
            String dialog = dialogues[currentDialogueIndex];
            FontMetrics fm = g2.getFontMetrics();
            int maxWidth = width - 2 * paddingX;
            int lineHeight = fm.getHeight();
            int drawY = y + paddingY + 30;
            String[] words = dialog.split(" ");
            StringBuilder line = new StringBuilder();
            for (String word : words) {
                String testLine = line.length() == 0 ? word : line + " " + word;
                if (fm.stringWidth(testLine) > maxWidth) {
                    g2.drawString(line.toString(), x + paddingX, drawY);
                    drawY += lineHeight;
                    line = new StringBuilder(word);
                } else {
                    if (line.length() > 0) line.append(" ");
                    line.append(word);
                }
            }
            if (line.length() > 0) {
                g2.drawString(line.toString(), x + paddingX, drawY);
            }
        } else {
            // Reset dialog jika sudah selesai
            currentDialogueIndex = 0;
            isTalking = false;
            showActionMenu = true;
        }
    }


    public boolean drawProposingAnswer(Graphics2D g2, String speakerName) {
        int x = gp.tileSize * 1;
        int y = gp.tileSize * 8;
        int width = gp.tileSize * 14;
        int height = gp.tileSize * 3;

        drawSubwindow(g2, x, y, width, height);

        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.setColor(Color.WHITE);
        g2.drawString(speakerName + ":", x + 20, y + 35);

        if (heartPoints >= 150 && relationship != "Married" && gp.player.getEquippedItem() != null
                && gp.player.getEquippedItem().getName().equals("Proposal Ring")) {
            g2.drawString(proposingAnswers[0], x + 20, y + 100);
            relationship = "Proposed";
            return true;
        } else if (relationship == "Married") {
            g2.drawString(proposingAnswers[3], x + 20, y + 100);
            return true;

        } else {
            g2.drawString(proposingAnswers[1], x + 20, y + 100);
            return false;
        }

    }

    public void drawGifting(Graphics2D g2, String speakerName, int response) {
        int x = gp.tileSize * 1;
        int y = gp.tileSize * 8;
        int width = gp.tileSize * 14;
        int height = gp.tileSize * 3;

        drawSubwindow(g2, x, y, width, height);

        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.setColor(Color.WHITE);
        g2.drawString(speakerName + ":", x + 20, y + 35);
        if (response == 1) {
            g2.drawString(giftingAnswers[0], x + 20, y + 100);
        } else if (response == 2) {
            g2.drawString(giftingAnswers[1], x + 20, y + 100);
        } else if (response == 3) {
            g2.drawString(giftingAnswers[3], x + 20, y + 100);
        } else {
            g2.drawString(giftingAnswers[2], x + 20, y + 100);
        }
    }

    public boolean drawMarrying(Graphics2D g2, String speakerName) {
        int x = gp.tileSize * 1;
        int y = gp.tileSize * 8;
        int width = gp.tileSize * 14;
        int height = gp.tileSize * 3;

        drawSubwindow(g2, x, y, width, height);

        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.setColor(Color.WHITE);
        g2.drawString(speakerName + ":", x + 20, y + 35);

        if (heartPoints >= 150 && relationship == "Proposed" && daysCanMarry <= gp.gameDay) {
            g2.drawString(marriageAnswers[0], x + 20, y + 100);
            relationship = "Married";
            return true;
        } else if (relationship == "Married") {
            g2.drawString(marriageAnswers[3], x + 20, y + 100);
            return false;
        } else if (daysCanMarry > gp.gameDay) {
            g2.drawString(marriageAnswers[1], x + 20, y + 100);
            return false;
        } else {
            g2.drawString(marriageAnswers[2], x + 20, y + 100);
            return false;

        }

    }

    public String getName() {
        return name;
    }

    public void addHeartPoints(int points) {
        heartPoints += points;
        if (heartPoints > 150) {
            heartPoints = 150;
        }
    }

    public void substractHeartPoints(int points) {
        heartPoints -= points;
        if (heartPoints < 0) {
            heartPoints = 0;
        }
    }

    public String getRelationship() {
        return relationship;
    }
}