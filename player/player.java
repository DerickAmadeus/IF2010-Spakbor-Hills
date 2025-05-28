package player;

import main.GamePanel;
import main.KeyHandler;
import Map.Tile;
import Map.Soil;
import Furniture.Bed; // Import Bed class
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.BasicStroke;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import Items.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Player {
    public int x, y;
    public int speed;
    public int screenX;
    public int screenY;
    public Rectangle solidArea;
    public Rectangle interactionArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    GamePanel gp;
    KeyHandler keyH;
    public String direction; // Current animation/movement state (e.g., "up", "idleDown")
    String lastMoveDirection; // Last actual direction of movement input ("up", "down", "left", "right")
    private String location;

    public BufferedImage[] idleDownFrames, idleUpFrames, idleLeftFrames, idleRightFrames,
            leftFrames, rightFrames, upFrames, downFrames;

    private int spriteCounter = 0;
    private int spriteNum = 0;
    private final int ANIMATION_SPEED = 10;
    private boolean isActuallyMoving = false;
    private Inventory<Item> inventory;
    private Item equippedItem;
    private int energy;
    public static final int MAX_ENERGY = 100;
    private String farmName;
    // Removed 'private Tile tile;' as it seemed unused and uninitialized globally

    private int interactionCooldown = 0;

    public Player(GamePanel gp, KeyHandler keyH, String farmName) {
        this.gp = gp;
        this.keyH = keyH;
        this.energy = MAX_ENERGY;
        this.inventory = new Inventory<>(gp);
        this.farmName = farmName;
        loadInitialEquipment();
        loadInitialSeeds();
        loadInitialFood();

        this.screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        this.screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        interactionArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        setDefaultValues();
        getPlayerImage();
    }

    public void loadInitialSeeds() {
        ArrayList<String> spring = new ArrayList<>(Arrays.asList("Spring"));
        ArrayList<String> summer = new ArrayList<>(Arrays.asList("Summer"));
        ArrayList<String> fall = new ArrayList<>(Arrays.asList("Fall"));
        ArrayList<String> wheatSeason = new ArrayList<>(Arrays.asList("Spring", "Fall"));

        Seeds parsnip = new Seeds("Parsnip Seeds", "Grows quickly in Spring", 10, 20, 1, spring, 13);
        Seeds cauliflower = new Seeds("Cauliflower Seeds", "Takes time but valuable", 40, 80, 5, spring, 14);
        Seeds potato = new Seeds("Potato Seeds", "Produces multiple potatoes", 25, 50, 3, spring, 15);
        Seeds wheat = new Seeds("Wheat Seeds", "Spring wheat crop", 30, 60, 1, wheatSeason, 16);

        Seeds blueberry = new Seeds("Blueberry Seeds", "Produces blueberries", 40, 80, 7, summer, 17);
        Seeds tomato = new Seeds("Tomato Seeds", "Popular summer crop", 25, 50, 3, summer, 18);
        Seeds hotPepper = new Seeds("Hot Pepper Seeds", "Grows quickly", 20, 40, 1, summer, 19);
        Seeds melon = new Seeds("Melon Seeds", "Large summer fruit", 40, 80, 4, summer, 20);

        Seeds cranberry = new Seeds("Cranberry Seeds", "Multiple harvests", 50, 100, 2, fall, 21);
        Seeds pumpkin = new Seeds("Pumpkin Seeds", "Big and valuable", 75, 150, 7, fall, 22);
        Seeds grape = new Seeds("Grape Seeds", "Climbing vine fruit", 30, 60, 3, fall, 23);

        inventory.addItem(parsnip, 5);
        inventory.addItem(cauliflower, 2);
        inventory.addItem(potato, 4);
        inventory.addItem(wheat, 3);
        inventory.addItem(blueberry, 2);
        inventory.addItem(tomato, 6);
        inventory.addItem(hotPepper, 3);
        inventory.addItem(melon, 1);
        inventory.addItem(cranberry, 2);
        inventory.addItem(pumpkin, 1);
        inventory.addItem(grape, 3);
    }

    public void loadInitialFood() {
        Food fishChips = new Food("Fish n' Chips", "Makanan goreng yang gurih", 135, 150, 50);
        Food baguette = new Food("Baguette", "Roti khas Prancis", 80, 100, 25);
        Food sashimi = new Food("Sashimi", "Irisan ikan mentah segar", 275, 300, 70);
        Food fugu = new Food("Fugu", "Ikan buntal beracun namun lezat", 135, 0, 50);
        Food wine = new Food("Wine", "Minuman hasil fermentasi anggur", 90, 100, 20);
        Food pumpkinPie = new Food("Pumpkin Pie", "Pai labu manis dan lembut", 100, 120, 35);
        Food veggieSoup = new Food("Veggie Soup", "Sup sehat dari sayuran", 120, 140, 40);
        Food fishStew = new Food("Fish Stew", "Semur ikan hangat", 260, 280, 70);
        Food spakborSalad = new Food("Spakbor Salad", "Salad legendaris dari sayuran terbaik", 250, 0, 70);
        Food fishSandwich = new Food("Fish Sandwich", "Sandwich isi ikan", 180, 200, 50);
        Food legendSpakbor = new Food("The Legends of Spakbor", "Mitos yang bisa dimakan", 2000, 0, 100);
        Food pigHead = new Food("Cooked Pig's Head", "Kepala babi panggang spesial", 0, 1000, 100);

        inventory.addItem(fishChips, 2);
        inventory.addItem(baguette, 3);
        inventory.addItem(sashimi, 1);
        inventory.addItem(fugu, 1);
        inventory.addItem(wine, 2);
        inventory.addItem(pumpkinPie, 2);
        inventory.addItem(veggieSoup, 2);
        inventory.addItem(fishStew, 1);
        inventory.addItem(spakborSalad, 1);
        inventory.addItem(fishSandwich, 1);
        inventory.addItem(legendSpakbor, 1);
        inventory.addItem(pigHead, 1);
    }

    public void loadInitialEquipment() {
        Equipment wateringCan = new Equipment("Watering Can", "Untuk menyiram tanaman.", 10, 10);
        Equipment pickaxe = new Equipment("Pickaxe", "Untuk menghancurkan batu.", 15, 15);
        Equipment hoe = new Equipment("Hoe", "Untuk mencangkul tanah.", 12, 12);
        Equipment fishingRod = new Equipment("Fishing Rod", "Untuk memancing ikan.", 19, 19);

        inventory.addItem(wateringCan, 1);
        inventory.addItem(pickaxe, 1);
        inventory.addItem(hoe, 1);
        inventory.addItem(fishingRod, 1);
    }

    public void showCoordinates() {
        System.out.println("Player Coordinates: (" + x + ", " + y + ")");
        System.out.println("Screen Coordinates: (" + screenX + ", " + screenY + ")");
        System.out.println("Direction: " + direction);
        System.out.println("Last Move Direction: " + lastMoveDirection);
        System.out.println("Energy: " + energy);
    }

    public void setDefaultValues() {
        this.x = gp.tileSize * 10;
        this.y = gp.tileSize * 10;
        this.speed = 4;
        this.lastMoveDirection = "down"; // Default facing direction
        this.direction = "idleDown";     // Default animation state
        this.location = "Farm Map";
    }

    public void getPlayerImage() {
        idleDownFrames = loadAnimationFrames("idledown", 6);
        idleUpFrames = loadAnimationFrames("idleup", 6);
        idleLeftFrames = loadAnimationFrames("idleleft", 6);
        idleRightFrames = loadAnimationFrames("idleright", 6);
        upFrames = loadAnimationFrames("up", 6);
        downFrames = loadAnimationFrames("down", 6);
        leftFrames = loadAnimationFrames("left", 6);
        rightFrames = loadAnimationFrames("right", 6);

        if (upFrames == null) upFrames = new BufferedImage[0];
        if (downFrames == null) downFrames = new BufferedImage[0];
        if (leftFrames == null) leftFrames = new BufferedImage[0];
        if (rightFrames == null) rightFrames = new BufferedImage[0];
        if (idleDownFrames == null) idleDownFrames = new BufferedImage[0];
        if (idleUpFrames == null) idleUpFrames = new BufferedImage[0];
        if (idleLeftFrames == null) idleLeftFrames = new BufferedImage[0];
        if (idleRightFrames == null) idleRightFrames = new BufferedImage[0];
    }

    private BufferedImage[] loadAnimationFrames(String animationIdentifier, int frameCount) {
        BufferedImage[] frames = new BufferedImage[frameCount];
        String actualFolderName;
        String fileNamePrefix = animationIdentifier;

        if (animationIdentifier.startsWith("idle")) {
            actualFolderName = "idle";
        } else {
            actualFolderName = animationIdentifier;
        }

        try {
            for (int i = 0; i < frameCount; i++) {
                String imagePath = "/player/" + actualFolderName + "/" + fileNamePrefix + "_" + i + ".png";
                InputStream is = getClass().getResourceAsStream(imagePath);
                if (is == null) {
                    System.err.println("Cannot load image: " + imagePath);
                    frames[i] = createPlaceholderImage();
                } else {
                    frames[i] = ImageIO.read(is);
                    is.close();
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading animation frames for identifier: " + animationIdentifier);
            e.printStackTrace();
            for (int i = 0; i < frameCount; i++) {
                if (frames[i] == null) frames[i] = createPlaceholderImage();
            }
        }
        return frames;
    }

    private BufferedImage createPlaceholderImage() {
        BufferedImage placeholder = new BufferedImage(gp.tileSize, gp.tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = placeholder.createGraphics();
        g.setColor(Color.MAGENTA);
        g.fillRect(0, 0, gp.tileSize, gp.tileSize);
        g.setColor(Color.BLACK);
        g.drawString("X", gp.tileSize / 2 - 5, gp.tileSize / 2 + 5);
        g.dispose();
        return placeholder;
    }

    public void update() {
        String prevAnimationState = direction;
        isActuallyMoving = false;

        if (interactionCooldown > 0) {
            interactionCooldown--;
        }

        boolean isAttemptingMoveByKeyPress = keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed;

        if (isAttemptingMoveByKeyPress && gp.gameState == gp.playState) {
            if (keyH.upPressed) { direction = "up"; lastMoveDirection = "up"; }
            else if (keyH.downPressed) { direction = "down"; lastMoveDirection = "down"; }
            else if (keyH.leftPressed) { direction = "left"; lastMoveDirection = "left"; }
            else if (keyH.rightPressed) { direction = "right"; lastMoveDirection = "right"; }

            if (!collisionOn) {
                switch (direction) {
                    case "up":    y -= speed; isActuallyMoving = true; break;
                    case "down":  y += speed; isActuallyMoving = true; break;
                    case "left":  x -= speed; isActuallyMoving = true; break;
                    case "right": x += speed; isActuallyMoving = true; break;
                }
            }

            collisionOn = false;
            gp.cChecker.checkTile(this);
        }

        int worldPixelWidth = gp.map.currentMapWorldCol * gp.tileSize;
        int worldPixelHeight = gp.map.currentMapWorldRow * gp.tileSize;

        if (x < 0) x = 0;
        if (x + gp.tileSize > worldPixelWidth) x = worldPixelWidth - gp.tileSize;
        if (y < 0) y = 0;
        if (y + gp.tileSize > worldPixelHeight) y = worldPixelHeight - gp.tileSize;

        int playerCurrentTileCol = (x + solidArea.x + solidArea.width / 2) / gp.tileSize;
        int playerCurrentTileRow = (y + solidArea.y + solidArea.height / 2) / gp.tileSize;

        int targetTileCol = playerCurrentTileCol;
        int targetTileRow = playerCurrentTileRow;

        switch (lastMoveDirection) {
            case "up": targetTileRow--; break;
            case "down": targetTileRow++; break;
            case "left": targetTileCol--; break;
            case "right": targetTileCol++; break;
        }

        interactionArea.x = targetTileCol * gp.tileSize;
        interactionArea.y = targetTileRow * gp.tileSize;

        if (keyH.interactPressed && interactionCooldown == 0) {
            interact();
            keyH.interactPressed = false;
            interactionCooldown = 15;
        }

        if (isAttemptingMoveByKeyPress && !collisionOn && isActuallyMoving) {
            // Direction is already set
        } else {
            switch (lastMoveDirection) {
                case "up":    direction = "idleUp";    break;
                case "down":  direction = "idleDown";  break;
                case "left":  direction = "idleLeft";  break;
                case "right": direction = "idleRight"; break;
                default:      direction = "idleDown";  break;
            }
        }

        if (!prevAnimationState.equals(direction)) {
            spriteNum = 0;
            spriteCounter = 0;
        }

        spriteCounter++;
        if (spriteCounter > ANIMATION_SPEED) {
            spriteNum++;
            BufferedImage[] currentFrames = getCurrentAnimationFrames();
            if (currentFrames != null && currentFrames.length > 0) {
                if (spriteNum >= currentFrames.length) {
                    spriteNum = 0;
                }
            } else {
                spriteNum = 0;
            }
            spriteCounter = 0;
        }
    }

    private BufferedImage[] getCurrentAnimationFrames() {
        switch (direction) {
            case "up": return (upFrames != null && upFrames.length > 0) ? upFrames : idleDownFrames;
            case "down": return (downFrames != null && downFrames.length > 0) ? downFrames : idleDownFrames;
            case "left": return (leftFrames != null && leftFrames.length > 0) ? leftFrames : idleDownFrames;
            case "right": return (rightFrames != null && rightFrames.length > 0) ? rightFrames : idleDownFrames;
            case "idleUp": return (idleUpFrames != null && idleUpFrames.length > 0) ? idleUpFrames : idleDownFrames;
            case "idleDown": return (idleDownFrames != null && idleDownFrames.length > 0) ? idleDownFrames : null;
            case "idleLeft": return (idleLeftFrames != null && idleLeftFrames.length > 0) ? idleLeftFrames : idleDownFrames;
            case "idleRight": return (idleRightFrames != null && idleRightFrames.length > 0) ? idleRightFrames : idleDownFrames;
            default:
                return (idleDownFrames != null && idleDownFrames.length > 0) ? idleDownFrames : null;
        }
    }

    public void drawPlayer(Graphics2D g2) {
        BufferedImage image = null;
        BufferedImage[] currentFrames = getCurrentAnimationFrames();

        if (currentFrames != null && currentFrames.length > 0 && spriteNum < currentFrames.length) {
            image = currentFrames[spriteNum];
        } else if (idleDownFrames != null && idleDownFrames.length > 0) {
            image = idleDownFrames[0];
        }

        if (image == null) {
            g2.setColor(Color.RED);
            g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
            return;
        }
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

        if (gp.debugMode) {
            g2.setColor(new Color(255, 0, 0, 100));
            g2.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

            g2.setColor(new Color(0, 255, 0, 100));
            int interactionScreenX = interactionArea.x - x + screenX;
            int interactionScreenY = interactionArea.y - y + screenY;
            g2.fillRect(interactionScreenX, interactionScreenY, interactionArea.width, interactionArea.height);
        }
        if (equippedItem != null && equippedItem.getIcon() != null) {
            int handX = screenX + gp.tileSize / 2;
            int handY = screenY + gp.tileSize / 2;

            g2.drawImage(equippedItem.getIcon(), handX, handY, gp.tileSize / 2, gp.tileSize / 2, null);

            Integer count = inventory.getItemCount(equippedItem);
            if (count != null && count > 1) {
                g2.setColor(Color.white);
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                String countStr = String.valueOf(count);
                int stringWidth = g2.getFontMetrics().stringWidth(countStr);
                g2.drawString(countStr, handX + gp.tileSize / 2 - stringWidth + 2, handY + gp.tileSize / 2 + 2);
            }
        }
    }
    public void drawEnergyBar(Graphics2D g2) {
        int barX = 20;
        int barY = 20;
        int barWidth = 200;
        int barHeight = 30;

        double percent = (double) energy / MAX_ENERGY;
        int energyWidth = (int) (barWidth * percent);

        g2.setColor(Color.darkGray);
        g2.fillRoundRect(barX, barY, barWidth, barHeight, 10, 10);

        Color energyColor;
        if (percent > 0.5) {
            energyColor = Color.green;
        } else if (percent > 0.3) {
            energyColor = Color.yellow;
        } else if (percent > 0.15) {
            energyColor = Color.orange;
        } else {
            energyColor = Color.red;
        }

        g2.setColor(energyColor);
        g2.fillRoundRect(barX, barY, energyWidth, barHeight, 10, 10);

        g2.setColor(Color.black);
        g2.drawRoundRect(barX, barY, barWidth, barHeight, 10, 10);

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18f));
        String energyText = energy + " / " + MAX_ENERGY;
        int textX = barX + (barWidth - g2.getFontMetrics().stringWidth(energyText)) / 2;
        int textY = barY + barHeight - 8;
        g2.drawString(energyText, textX, textY);
    }
    public void drawNotification(Graphics2D g2, String message, int x, int y) {
        int paddingX = 15;
        int paddingY = 10;

        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(message);
        int textHeight = fm.getHeight();

        int boxWidth = textWidth + paddingX * 2;
        int boxHeight = textHeight + paddingY * 2;

        Color backgroundColor = new Color(0, 0, 0, 180);
        g2.setColor(backgroundColor);
        g2.fillRoundRect(x, y, boxWidth, boxHeight, 20, 20);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, boxWidth, boxHeight, 20, 20);

        g2.setColor(Color.WHITE);
        int textX = x + paddingX;
        int textY = y + paddingY + fm.getAscent();
        g2.drawString(message, textX, textY);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getSpeed() { return speed; }
    public Rectangle getSolidArea() { return solidArea; }
    public String getDirectionForCollision() { return direction; }
    public String getLastMoveDirection() { return lastMoveDirection; }
    public Rectangle getInteractionArea() { return interactionArea; }
    public Inventory<Item> getInventory() { return inventory;}

    public void interact() {
        Tile tileToInteract = gp.map.getTile(interactionArea.x, interactionArea.y);
        if (tileToInteract == null) {
            System.out.println("Player: No tile found at interaction area (" + interactionArea.x/gp.tileSize + "," + interactionArea.y/gp.tileSize + ").");
            return;
        }

        System.out.println("Player: Interacting with tile at: (" + interactionArea.x/gp.tileSize + "," + interactionArea.y/gp.tileSize + ") Name: " + tileToInteract.getTileName());

        if (tileToInteract instanceof Soil) {
            System.out.println("Player: Interacting with Soil tile.");
            Soil soilTile = (Soil) tileToInteract;
            if (soilTile.getSeedPlanted() != null) {
                System.out.println("Tile index:" + (soilTile.getSeedPlanted().getTileIndex() - 13) + "Wet Index:" + soilTile.getSeedPlanted().getWetIndex());
                System.out.println("wet cooldown: " + soilTile.getWetCooldown());
                System.out.println("days: " + soilTile.getDaysToHarvest());
                System.out.println(String.format("day planted/changed: %d, at %d:%d", soilTile.timestampDay, soilTile.timestampHour, soilTile.timestampMinute));
                System.out.println(String.format("day watered/changed: %d, at %d:%d", soilTile.waterTimestampDay, soilTile.waterTimestampHour, soilTile.waterTimestampMinute));
            } else {
                System.out.println("Player: Tanah ini kosong (tidak ada bibit).");
            }
        } else if (tileToInteract instanceof Bed) { // Added Bed interaction
            System.out.println("Player: Interacting with a Bed tile: " + tileToInteract.getTileName());
            if (tileToInteract.getTileName().equals("Bed")) {
                if (gp.gameState == gp.playState) {
                    System.out.println("Player: Time to sleep!");
                    gp.startSleepingSequence();
                } else {
                    System.out.println("Player: Can only sleep during play state.");
                }
            } else {
                System.out.println("Player: This part of the bed is not for sleeping, or you need to face the right spot.");
            }
        } else if (tileToInteract.getTileName().toLowerCase().contains("building")) {
            System.out.println("Player: Interacting with building: " + tileToInteract.getTileName());
<<<<<<< HEAD
        } else if (tileToInteract.getTileName().toLowerCase().contains("door")) { // Contoh interaksi dengan pintu
            System.out.println("Player: Interacting with a door.");
            // Logika pindah map atau masuk gedung
        } else if(tileToInteract.getTileName().toLowerCase().equals("bed")) {
            System.out.println("Player : Interacting with a bed");
            sleeping();
<<<<<<< HEAD
        }else if (tileToInteract.getTileName().equals("TV")){
            System.out.println("Player: Interacting with TV.");
            WatchTV();
<<<<<<< HEAD
<<<<<<< HEAD

        } else {
=======
        
        }else {
>>>>>>> parent of bc053c9 (Adding)
            System.out.println("Player: No specific interaction for this tile (" + tileToInteract.getTileName() + ").");
            // setEnergy(getEnergy()+10); // Mungkin tidak perlu untuk interaksi umum
        }
        gp.addMinutes(60);
        
        // Cooldown sudah diatur di metode update() setelah memanggil interact()
    }
=======
>>>>>>> fb525b28b82c427a37f31a1f9f1e0809b32dc757
=======
>>>>>>> fb525b28b82c427a37f31a1f9f1e0809b32dc757

=======
>>>>>>> parent of d0d269f (Merge branch 'debug' into Derick2)
        } else {
            System.out.println("Player: No specific interaction for this tile (" + tileToInteract.getTileName() + ").");
        }
    }

    public void openInventory(Graphics2D g2) {
        inventory.drawInventory(g2);
    }

    public Item getEquippedItem() {
        return equippedItem;
    }
    public void equipItem(Item item) {
        if (item == null) {
            equippedItem = null;
            System.out.println("Unequipped.");
            return;
        }
        equippedItem = item;
        System.out.println("Equipped: " + item.getName());
    }

    public String getFarmName() {
        return farmName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(int locationID) {
        if (locationID == 0) {
            this.location = "Farm Map";
        } else if (locationID == 1) {
            this.location = "Forest River";
        }

    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        if (energy > MAX_ENERGY) {
            this.energy = MAX_ENERGY;
        } else if (energy < 0 && energy > -20) {
            System.out.println("Warning: Energy is low! Action can still be performed, but consider sleeping.");
            this.energy = energy;
        } else if (energy <= -20) {
            this.energy = -20;
            System.out.println("Error: Energy is too low! Going to sleep...");
            if (gp.gameState == gp.playState) {
                gp.startSleepingSequence();
            }
        } else {
            this.energy = energy;
        }
<<<<<<< HEAD

        // Periksa kondisi auto-sleep jika energi baru saja turun ke/di bawah -20
        // dan pemain sedang dalam kondisi bisa bermain (playState).

=======
>>>>>>> parent of d0d269f (Merge branch 'debug' into Derick2)
    }

    public void tiling() {
        if (equippedItem != null && equippedItem.getName().equals("Hoe") &&
                energy >= -15 && keyH.enterPressed && interactionCooldown == 0) {
            Tile tileToTill = gp.map.getTile(interactionArea.x, interactionArea.y);
            if (tileToTill != null && tileToTill.getTileName().equals("grass")) {
                gp.map.setTileType(interactionArea.x, interactionArea.y, 10);
                setEnergy(getEnergy() - 5);
                gp.addMinutes(5);
                System.out.println("Player: Tilled grass at (" + interactionArea.x/gp.tileSize + "," + interactionArea.y/gp.tileSize + ")");
            } else if (tileToTill != null) {
                //aa
            }
        }
    }

    public void recoverLand() {
        if (equippedItem != null && equippedItem.getName().equals("Pickaxe") &&
                energy >= -15 && keyH.enterPressed && interactionCooldown == 0) {
            Tile tileToTill = gp.map.getTile(interactionArea.x, interactionArea.y);
            if (tileToTill != null && tileToTill.getTileName().equals("soil")) {
                Soil recoverable = (Soil) tileToTill;
                if (recoverable.canPlant()) {
                    gp.map.setTileType(interactionArea.x, interactionArea.y, 0);
                    setEnergy(getEnergy() - 5);
                    gp.addMinutes(5);
                    System.out.println("Player: Ubah ke soil at (" + interactionArea.x/gp.tileSize + "," + interactionArea.y/gp.tileSize + ")");
                }
            } else if (tileToTill != null) {
                System.out.println("Player: Cannot till " + tileToTill.getTileName());
            }
        }
    }

    public void planting() {
        if (equippedItem != null && equippedItem instanceof Seeds &&
                energy >= -15 && keyH.enterPressed && interactionCooldown == 0 && gp.gameState == gp.playState) {

            Tile tileToPlantOn = gp.map.getTile(interactionArea.x, interactionArea.y);
            boolean isLast = false;

            if (tileToPlantOn instanceof Soil) {
                Soil soilTile = (Soil) tileToPlantOn;
                Seeds seedToPlant = (Seeds) equippedItem;
                if (soilTile.canPlant() && seedToPlant.getSeason().contains(gp.currentSeason)) {
                    if (inventory.getItemCount(equippedItem) == 1) {
                        isLast = true;
                    }
                    gp.map.plantSeedAtTile(interactionArea.x, interactionArea.y, seedToPlant);
                    inventory.removeItem(equippedItem, 1);
                    setEnergy(getEnergy() - 5);
                    gp.addMinutes(5);
                    if (isLast) {
                        equipItem(null);
                    }
                } else {
                    if (!soilTile.canPlant()) {
                        System.out.println("Player: Cannot plant, soil already has a seed or not suitable.");
                    } else {
                        System.out.println("Player: Cannot plant, season not suitable.");
                    }
                }
            } else if (tileToPlantOn != null) {
                System.out.println("Player: Cannot plant " + ((Seeds)equippedItem).getName() + " on " + tileToPlantOn.getTileName());
            }
        }
    }
    public void watering() {
        if (equippedItem != null && equippedItem.getName().equals("Watering Can") &&
                energy >= -15 && keyH.enterPressed && interactionCooldown == 0) {
            Tile tileToWater = gp.map.getTile(interactionArea.x, interactionArea.y);
            if (tileToWater != null && tileToWater instanceof Soil) {
                Soil watered = (Soil) tileToWater;
                if (!watered.canPlant() && watered.canWater()) {
                    watered.water(gp);
                    setEnergy(getEnergy() - 5);
                    gp.addMinutes(5);
                }
            }
        }
    }

    public void harvesting() {
        if (equippedItem == null && energy >= -15 && keyH.enterPressed && interactionCooldown == 0) {
            Tile tileToHarvest = gp.map.getTile(interactionArea.x, interactionArea.y);
            if (tileToHarvest != null && tileToHarvest instanceof Soil) {
                Soil harvest = (Soil) tileToHarvest;
                if (!harvest.canPlant() && harvest.getDaysToHarvest() == 0 && harvest.getWetCooldown() > 0) {
                    gp.map.harvestSeedAtTile(interactionArea.x, interactionArea.y);
                    setEnergy(getEnergy() - 5);
                    gp.addMinutes(5);
                }
            }
        }
    }

    public void eating() {
        Item get = inventory.getSelectedItem();
        if ((get instanceof Fish || get instanceof Crops || get instanceof Food) && energy < MAX_ENERGY && keyH.enterPressed && interactionCooldown == 0) {
            if (get instanceof Fish) {
                Fish eaten = (Fish) get;
                eaten.eat(this, eaten);
            } else if (get instanceof Crops) {
                Crops eaten = (Crops) get;
                eaten.eat(this, get);
            } else if (get instanceof Food) {
                Food eaten = (Food) get;
                eaten.eat(this, get);
            }
            // The original gp.addMinutes(1440) was here. If eating takes time, it should be managed.
            // For now, I'm keeping it as it was, but 1440 minutes (24 hours) for eating seems excessive.
            // This might be a placeholder or a specific game mechanic you intended.
            gp.addMinutes(60);
        }
    }
<<<<<<< HEAD

    public void drawFishingWindow(Graphics2D g2) {
        int frameX = gp.tileSize;
        int frameY = gp.tileSize * 2;
        int frameWidth = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 5;

        Color backgroundColor = new Color(0, 0, 0, 210);
        g2.setColor(backgroundColor);
        g2.fillRoundRect(frameX, frameY, frameWidth, frameHeight, 35, 35);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(frameX + 5, frameY + 5, frameWidth - 10, frameHeight - 10, 25, 25);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24F));
        g2.drawString("Fish Caught!", frameX + 20, frameY + 50);
        int debugY = frameY + 130;
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));

        if (gp.fishingTargetFish != null) {
            g2.drawString("Attempt: " + gp.fishingAttempts + " / " + gp.maxFishingAttempts, frameX + 20, debugY);
            debugY += 25;
            g2.drawString("Fish: " + gp.fishingTargetFish.getName() + " (" + gp.fishingTargetFish.getRarity() + ")", frameX + 20, debugY);
            debugY += 25;
        } else {
            debugY += 25;
            g2.drawString("Mulai Memancing!!!", frameX + 20, debugY);
            debugY += 25;
        }

        // Hint berdasarkan tebakan
        if (gp.fishingHint != null && !gp.fishingHint.isEmpty()) {
            g2.setColor(Color.YELLOW);
            g2.drawString("Hint: " + gp.fishingHint, frameX + 20, debugY);
        }
        if (gp.debugMode) {
            if (gp.fishingTarget != -1) {
                g2.drawString("Target Code: " + gp.fishingTarget, frameX + 20, debugY + 25);
            }
        }
        g2.drawString(gp.fishingInput, frameX + 20, frameY + 90);
    }
    public void sleeping() {
        int energyRecover = 0;
        if (energy < 0) {
            energyRecover = (int)(0.1 * MAX_ENERGY);
        } else if (energy < 10 ) {
            energyRecover = (int)(0.5 * MAX_ENERGY); 
        } else {
            energyRecover = MAX_ENERGY;
        }


        if (energy < MAX_ENERGY && keyH.interactPressed && interactionCooldown == 0) {
            if (gp.gameState == gp.playState) {
                System.out.println("Player: Time to sleep!");

                gp.startSleepingSequence();
                // Reset energy to full after sleeping
                setEnergy(energyRecover);

            } else {
                System.out.println("Player: Can only sleep during play state.");
            }
        } else if (energy == -20) {
            gp.startSleepingSequence();
            setEnergy(energyRecover);
        } 
        if (gp.gameHour == 2){
            gp.startSleepingSequence();
            setEnergy(energyRecover);
        } else {
            System.out.println("Player: Energy is already full, no need to sleep.");

        }
    }
<<<<<<< HEAD

    public void WatchTV() {
        if (keyH.interactPressed && interactionCooldown == 0) {
            if (gp.gameState == gp.playState) {
                System.out.println("Player: Watching TV!");
                // Implementasi logika menonton TV
                // Misalnya, menambah energi atau waktu
                // gp.showWeatherNotification(gp.getCurrentWeather());
<<<<<<< HEAD
<<<<<<< HEAD
            }
        }
    }
=======
>>>>>>> parent of bc053c9 (Adding)
    /*public void fishing() {
        if (equippedItem != null && equippedItem.getName().equals("Fishing Rod") && 
            energy >= -15 && keyH.enterPressed && interactionCooldown == 0) {
            Tile tileToFish = gp.map.getTile(interactionArea.x, interactionArea.y);
            if (tileToFish != null && tileToFish.getTileName().equals("water")) { 
                //implementasi memancing
                setEnergy(getEnergy() - 5);
                gp.addMinutes(5);
                System.out.println("Player: Tilled grass at (" + interactionArea.x/gp.tileSize + "," + interactionArea.y/gp.tileSize + ")");
            } else if (tileToFish != null) {
                System.out.println("Player: Cannot till " + tileToFish.getTileName());
=======
>>>>>>> fb525b28b82c427a37f31a1f9f1e0809b32dc757
=======
>>>>>>> fb525b28b82c427a37f31a1f9f1e0809b32dc757
            }
        }
    }
    public void fishing() {
        if (equippedItem != null && equippedItem.getName().equals("Fishing Rod") && 
            energy >= -15 && keyH.enterPressed && interactionCooldown == 0 && gp.gameState != gp.fishingState) {
            Tile tileToFish = gp.map.getTile(interactionArea.x, interactionArea.y);
            if (tileToFish != null && tileToFish.getTileName().equals("Water")) {
                gp.gameState = gp.fishingState; 
                setEnergy(getEnergy() - 5);
                gp.addMinutes(15);
                keyH.enterPressed = false;
            } 
        }
    }

=======
>>>>>>> parent of d0d269f (Merge branch 'debug' into Derick2)
}