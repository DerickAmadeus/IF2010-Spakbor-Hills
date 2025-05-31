package player;

import Furniture.*;
import Items.*;
import Map.ShippingBin;
import Map.Soil;
import Map.Tile;
import NPC.NPC;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

public class Player {
    public int x, y; 
    public int speed;
    public int screenX; 
    public int screenY; 
    public Rectangle solidArea; 
    public Rectangle interactionArea; 
    public int solidAreaDefaultX, solidAreaDefaultY;
    private int money = 1000;
    private int storedMoney = 0; 
    public boolean collisionOn = false;
    GamePanel gp;
    KeyHandler keyH; 
    public String direction; 
    String lastMoveDirection; 
    private String location;
    public NPC currentNPC; 

    public BufferedImage[] idleDownFrames, idleUpFrames, idleLeftFrames, idleRightFrames,
                           leftFrames, rightFrames, upFrames, downFrames;
    public BufferedImage goldIcon;
    public ShippingBin currSB;
    public int checkerstate = 0;

    private int lastday = 1;
    private int spriteCounter = 0;
    private int spriteNum = 0;
    private final int ANIMATION_SPEED = 10;
    private boolean isActuallyMoving = false;
    private Inventory<Item> inventory;
    private Item equippedItem;
    private int energy;
    private static final int MAX_ENERGY = 100; 

    private Tile tile; 
    private String farmName;

    private String playerName = null;
    private String gender = null;

    private final String[] menu = { "Continue", "Player Info", "Statistics", "Help", "Quit" };
    public int menuCommand = 0;

    private int interactionCooldown = 0;
    boolean isSleeping = false;
    
    public int totalIncome = 0;
    public int totalExpenditure = 0;
    public int cropsHarvested = 0;
    public int fishCaught = 0;
    public int fishCaughtCommon = 0;
    public int fishCaughtRegular = 0;
    public int fishCaughtLegendary = 0;

    public Player(GamePanel gp, KeyHandler keyH, String farmName) {
        this.gp = gp;
        this.keyH = keyH;
        this.energy = MAX_ENERGY;
        this.inventory = new Inventory<>(gp);
        this.farmName = farmName;

        loadInitialItems();
        this.screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        this.screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        try {
            goldIcon = ImageIO.read(getClass().getResourceAsStream("/main/Gold.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        interactionArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        setDefaultValues();
        getPlayerImage();
    }

    public void loadInitialItems() {
        ArrayList<String> spring = new ArrayList<>(Arrays.asList("Spring"));
        Equipment wateringCan = new Equipment("Watering Can", "Untuk menyiram tanaman.", 10, 10);
        Equipment pickaxe = new Equipment("Pickaxe", "Untuk menghancurkan batu.", 15, 15);
        Equipment hoe = new Equipment("Hoe", "Untuk mencangkul tanah.", 12, 12);
        Equipment fishingRod = new Equipment("Fishing Rod", "Untuk memancing ikan.", 19, 19);
        Seeds parsnip = new Seeds("Parsnip Seeds", "Grows quickly in Spring", 10, 20, 1, spring, 13);

        inventory.addItem(wateringCan, 1);
        inventory.addItem(pickaxe, 1);
        inventory.addItem(hoe, 1);
        inventory.addItem(fishingRod, 1);
        inventory.addItem(parsnip, 15);
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
        this.speed = 10;
        this.lastMoveDirection = "down";
        this.direction = "idleDown";
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
            gp.cChecker.checkNPC(this); 
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
    public void drawGoldWindow(Graphics2D g2) {
        int barX = 20;
        int barY = 20;
        int barHeight = 30;
        int margin = 4;

        int frameX = barX;
        int frameY = barY + barHeight + margin;
        int frameHeight = 24;

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14f));
        FontMetrics fm = g2.getFontMetrics();

        String goldAmountText = String.valueOf(money);
        int textPadding = 6;

        int iconSize = 16;
        int iconPadding = 4;

        int frameWidth = iconPadding + iconSize + iconPadding + fm.stringWidth(goldAmountText) + textPadding;

        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(frameX, frameY, frameWidth, frameHeight, 10, 10);

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(frameX + 2, frameY + 2, frameWidth - 4, frameHeight - 4, 10, 10);

        if (goldIcon != null) {
            g2.drawImage(goldIcon, frameX + iconPadding, frameY + (frameHeight - iconSize) / 2, iconSize, iconSize, null);
        }

        g2.setColor(Color.yellow);
        int textX = frameX + iconPadding + iconSize + iconPadding;
        int textY = frameY + frameHeight - 7;
        g2.drawString(goldAmountText, textX, textY);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getSpeed() { return speed; }
    public Rectangle getSolidArea() { return solidArea; }
    public String getDirectionForCollision() { return direction; } 
    public String getLastMoveDirection() { return lastMoveDirection; } 
    public Rectangle getInteractionArea() { return interactionArea; } 
    public Inventory<Item> getInventory() { return inventory;}
    public static int getMaxEnergy() { return MAX_ENERGY; }

    public void printStats() {
        System.out.println("=== Player Statistics ===");
        System.out.println("Total Income: " + totalIncome);
        System.out.println("Total Expenditure: " + totalExpenditure);
        System.out.println("Average Total Income: " + (int) totalIncome/4);
        System.out.println("Average Total Expenditure: " + (int) totalExpenditure/4);
        System.out.println("Days played: " + gp.daysPlayed);
        System.out.println("Crops Harvested: " + cropsHarvested);
        System.out.println("Fish Caught: " + fishCaught);
        System.out.println("  - Common: " + fishCaughtCommon);
        System.out.println("  - Regular: " + fishCaughtRegular);
        System.out.println("  - Legendary: " + fishCaughtLegendary);
        System.out.println("==========================");
    }

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

        } else if (tileToInteract.getTileName().toLowerCase().contains("door")) { 
            System.out.println("Player: Interacting with a door.");
        } else if(tileToInteract.getTileName().toLowerCase().equals("bed")) {
            System.out.println("Player : Interacting with a bed");
            sleeping();
        }  else if (tileToInteract instanceof ShippingBin) {
            System.out.println("Player : Interacting with shipping bin");
            ShippingBin sb = (ShippingBin) tileToInteract;
            currSB = sb; 
            checkerstate = 1; 
        }
        else {
            System.out.println("Player: No specific interaction for this tile (" + tileToInteract.getTileName() + ").");
        }
        if (gp.activeStove != null) {
            System.out.println(String.format("hari %d jam %d:%d", gp.activeStove.timestampDay, gp.activeStove.timestampHour, gp.activeStove.timestampMinute));
        }
        if (gp.debugMode) {
            for (int rainyDays : gp.rainDaysInSeason) {
                System.out.println(rainyDays);
            }
            gp.addMinutes(1440);
        }
        printStats();
        /*for (Fish f : gp.allFishes) {
            System.out.println(f.getName() + ": " + f.getHargaJual());
        }*/
    }


    public boolean interactingWithNPC() {
        if (gp.keyHandler.enterPressed && this.interactionCooldown == 0 && gp.gameState == gp.playState) {
            for (NPC npc : gp.npcs) {
                if (npc != null && npc.getSpawnMapName().equals(this.getLocation())) { 
                    Rectangle npcInteractionZone = npc.getInteractionTriggerAreaWorld(); 
                    if (this.interactionArea.intersects(npcInteractionZone)) {
                        System.out.println("Player: Memulai interaksi dengan NPC: " + npc.name);
                        gp.gameState = gp.dialogState;
                        currentNPC = npc;
                        gp.keyHandler.enterPressed = false; 
                        this.interactionCooldown = 20;       
                        return true; 
                    }
                }
            }
            gp.keyHandler.enterPressed = false;
            this.interactionCooldown = 5;          
            return false; 
        }
        return false;
    }

    public void dialogNPC(Graphics2D g2) {
        if (currentNPC != null) {
            if (energy <= -20){
                gp.gameState = gp.playState ; 
            }
            currentNPC.showStatus(g2);
            currentNPC.drawActionMenu(g2);
            if (currentNPC.isTalking) {
                chatting(g2); 
            } else if (currentNPC.isProposed) {
                proposing(g2);
            } else if (currentNPC.isGifted){
                gifting(g2);
            }

        } else {
            System.out.println("Player: No NPC to talk to.");
        }
    }

    public void openInventory(Graphics2D g2) {
        inventory.drawInventory(g2);
    }

    public void openShipping(Graphics2D g2) {
        inventory.drawShipping(g2);
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

    public String getPlayerName() {
        return playerName;
    }

    public String getGender() {
        return gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(int locationID) {
        if (locationID == 0) {
            this.location = "Farm Map";
        } else if (locationID == 1) {
            this.location = "Forest River";
        } else if (locationID == 2) {
            this.location = "Mountain Lake";
        } else if (locationID == 5){
            this.location = "MTHouse";
        } else if (locationID == 3) {
            this.location = "Player's House";
        } else if (locationID == 4) {
            this.location = "World Map";
        } else if (locationID == 6) {
            this.location = "Caroline's House";
        } else if (locationID == 7) {
            this.location = "Perry's House";
        } else if (locationID == 8) {
            this.location = "Dasco's House";
        } else if (locationID == 10) {
            this.location = "Emily's Store";
        } else if (locationID == 9) {
            this.location = "Abigail's House";
        }
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int amount) {
        this.money = amount; 
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int newEnergyValue) {
        if (newEnergyValue > MAX_ENERGY) {
            this.energy = MAX_ENERGY;
        } else if (newEnergyValue < -20) { 
            this.energy = -20; 
        } else {
            this.energy = newEnergyValue;
        }
    }

    public void tiling() {
        if (equippedItem != null && equippedItem.getName().equals("Hoe") && 
            energy >= -15 && keyH.enterPressed && location.equals("Farm Map")) {
            Tile tileToTill = gp.map.getTile(interactionArea.x, interactionArea.y);
            if (tileToTill != null && tileToTill.getTileName().equals("grass")) { 
                gp.map.setTileType(interactionArea.x, interactionArea.y, 10); 
                setEnergy(getEnergy() - 5);
                gp.addMinutes(5);
                System.out.println("Player: Tilled grass at (" + interactionArea.x/gp.tileSize + "," + interactionArea.y/gp.tileSize + ")");
            } else if (tileToTill != null) {
                System.out.println("Player: Cannot till " + tileToTill.getTileName());
            }
            
        }
    }

    public void recoverLand() {
        if (equippedItem != null && equippedItem.getName().equals("Pickaxe") && 
            energy >= -15 && keyH.enterPressed && location.equals("Farm Map")) {
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
            energy >= -15 && keyH.enterPressed && gp.gameState == gp.playState && location.equals("Farm Map")) {
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
            energy >= -15 && keyH.enterPressed && location.equals("Farm Map")) {
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

    public void showMenu(Graphics2D g2) {
        if (gp.gameState == gp.menuState) {
            int frameWidth = gp.tileSize * 12;
            int frameHeight = gp.tileSize * 10;
            int frameX = (gp.screenWidth  - frameWidth)  / 2;
            int frameY = (gp.screenHeight - frameHeight) / 2;
            Font menuFont;
            try {
                InputStream inputStream = getClass().getResourceAsStream("/main/PressStart2PRegular.ttf");
                menuFont = Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(24f);
            } catch (Exception e) {
                menuFont = new Font("Arial", Font.BOLD, 24);
            }
            
            Color c = new Color(0,0,0, 210);
            g2.setColor(c);
            g2.fillRoundRect(frameX, frameY, frameWidth, frameHeight, 35, 35);
            c = new Color(255,255,255);
            g2.setColor(c);
            g2.setStroke(new BasicStroke(5));
            g2.drawRoundRect(frameX+5, frameY+5, frameWidth-10, frameHeight-10, 25, 25);

            g2.setFont(menuFont.deriveFont(Font.BOLD, 30F));
            String title = "GAME MENU";
            int titleX = getX(title, g2);
            int titleY = frameY + gp.tileSize * 2;
            
            g2.setColor(Color.BLACK);
            g2.drawString(title, titleX + 3, titleY + 3);
            g2.setColor(Color.WHITE);
            g2.drawString(title, titleX, titleY);
            g2.setFont(menuFont.deriveFont(Font.PLAIN, 20F));
            for (int i = 0; i < menu.length; i++) {
                String option = menu[i];
                int optionX = getX(option, g2);
                int optionY = titleY + gp.tileSize * 2 + i * gp.tileSize;
                
                g2.setColor(Color.BLACK);
                g2.drawString(option, optionX + 2, optionY + 2);
                
                if (i == menuCommand) {
                    g2.setColor(Color.YELLOW);
                    g2.drawString(">", optionX - gp.tileSize, optionY);
                } else {
                    g2.setColor(Color.WHITE);
                }
                
                g2.drawString(option, optionX, optionY);
            }
        }
    }

    private int getX(String text, Graphics2D g2) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }

    public void handleMenuKey(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_S) {
            gp.keyHandler.upPressed = (code == KeyEvent.VK_W);
            gp.keyHandler.downPressed = (code == KeyEvent.VK_S);
            if (gp.keyHandler.upPressed) {
                menuCommand = (menuCommand + menu.length - 1) % menu.length;
            } else if (gp.keyHandler.downPressed) {
                menuCommand = (menuCommand + 1) % menu.length;
            }
            gp.keyHandler.upPressed = false;
            gp.keyHandler.downPressed = false;
        } else if (code == KeyEvent.VK_ENTER) {
            switch (menuCommand) {
                case 0:
                    gp.gameState = gp.playState; 
                    break;
                case 1:
                    // gp.gameState = gp.playerInfoState;
                    break;
                case 2:
                    // gp.gameState = gp.statisticsState;
                    break;
                case 3:
                    gp.gameState = gp.inGameHelpState; 
                    break;
                case 4:
                    System.exit(0); 
                    break;
            }
        } else if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }
    }

    public void harvesting() {
        if (equippedItem == null && energy >= -15 && keyH.enterPressed && location.equals("Farm Map")) {
            Tile tileToHarvest = gp.map.getTile(interactionArea.x, interactionArea.y );
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
            gp.addMinutes(5);
        }
    }

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
        } else if (energy <= 10 ) {
            energyRecover = (int)(0.5 * MAX_ENERGY); 
        } else {
            energyRecover = MAX_ENERGY;
        }

        if (energy < MAX_ENERGY && keyH.interactPressed && interactionCooldown == 0) {
            if (gp.gameState == gp.playState) {
                System.out.println("Player: Time to sleep!");
                gp.startSleepingSequence();
                setEnergy(energyRecover);
            } else {
                System.out.println("Player: Can only sleep during play state.");
            }
        } else if (energy == -20 && gp.gameState == gp.playState) {
            gp.startSleepingSequence();
            setEnergy(energyRecover);
            System.out.println("Player: Energy is at minimum, sleeping to recover energy.");
            System.out.println("hehe");
        } 
        if (gp.gameHour == 2){
            gp.startSleepingSequence();
            setEnergy(energyRecover);
        } else {
            //System.out.println("Player: Energy is already full, no need to sleep.");

        }
        isSleeping = false; 
    }

    public void fishing() {
        if (equippedItem != null && equippedItem.getName().equals("Fishing Rod") && 
            energy >= -5 && keyH.enterPressed && interactionCooldown == 0 && gp.gameState != gp.fishingState) {
            Tile tileToFish = gp.map.getTile(interactionArea.x, interactionArea.y);
            if (tileToFish != null && tileToFish.getTileName().equals("Water")) {
                gp.gameState = gp.fishingState; 
                setEnergy(getEnergy() - 5);
                gp.addMinutes(15);
                keyH.enterPressed = false;
            } 
        }
    }

public boolean energyReducedInThisChat = false;

    public void chatting(Graphics2D g2) {
        if (!energyReducedInThisChat) {
            setEnergy(getEnergy() - 10);
            energyReducedInThisChat = true;
            gp.addMinutes(10);
            currentNPC.addHeartPoints(10);
        }
        currentNPC.drawNPCDialog(g2, currentNPC.getName());
    }


    public void proposing(Graphics2D g2) {
        int energyUsed = 0;
        System.out.println(energyUsed);
        Boolean hasil = currentNPC.drawProposingAnswer(g2, currentNPC.getName());
        System.out.println(hasil);
        if (hasil == true) {
            energyUsed = 10;
        } else {
            energyUsed = 20;
        }
        if (!energyReducedInThisChat) {
            setEnergy(getEnergy() - energyUsed);
            energyReducedInThisChat = true;
        }
    }

     public void gifting(Graphics2D g2) {
        if (currentNPC != null) {
            Item[] loved = currentNPC.getLovedItems();
            Item[] liked = currentNPC.getLikedItems();
            Item[] hated = currentNPC.getHatedItems();
            int response = 0;
            Item selectedItem;
            if (inventory.getSelectedItem() != null) {
                selectedItem = inventory.getSelectedItem();
                for (Item item : loved) {
                    if (item != null && item.getName().equals(selectedItem.getName())) {
                        response = 1;
                    }
                }
                for (Item item : liked) {
                    if (item != null && item.getName().equals(selectedItem.getName())) {
                        response = 2;
                    }
                }
                for (Item item : hated) {
                    if (item != null && item.getName().equals(selectedItem.getName())) {
                        response = 3;
                    }
                }
                if (currentNPC.getName().equals("MT") && response == 0) { //Nanti ganti Mayor Tadi ya kalo dah kelar
                    response = 3;
                }
                if (inventory.optionCommandNum == 0) {
                    if (!energyReducedInThisChat) {
                        setEnergy(getEnergy() - 5);
                        energyReducedInThisChat = true;
                        gp.addMinutes(10);
                        switch (response) {
                            case 1:
                                currentNPC.addHeartPoints(25);
                                break;
                           case 2:
                                currentNPC.addHeartPoints(20);
                                break;
                           case 3:
                                currentNPC.substractHeartPoints(25);
                                break;
                            default:
                                break;
                        }
                    }
                    currentNPC.drawGifting(g2, currentNPC.getName(), response);
                }
            }
        } else {
            System.out.println("Player: No NPC to give gift to.");
        }
    }

    public void cooking() {
        if (energy >= -10 && (keyH.enterPressed || keyH.fpressed) && interactionCooldown == 0 && gp.gameState != gp.cookingState) {
            Tile check = gp.map.getTile(interactionArea.x, interactionArea.y);
            if (check != null && check instanceof Stove) {
                if(gp.activeStove == null || (gp.activeStove != null && gp.activeStove.getFood() == null)) {
                    gp.activeStove = (Stove) check;
                    if(keyH.enterPressed) {
                        gp.gameState = gp.cookingState;
                        keyH.enterPressed = false;
                    } else if (keyH.fpressed) {
                        gp.gameState = gp.fuelState;
                        keyH.fpressed = false;
                    }
                } else {
                    System.out.println("Stove is Cooking.. Please use later!");
                }
            }
        }
    }

    public void watching() {
        if (energy >= -5 && keyH.enterPressed && interactionCooldown == 0) {
            Tile TV = gp.map.getTile(interactionArea.x, interactionArea.y);
            if (TV != null && TV instanceof TV) {
                gp.activeTV = (TV) TV;
                gp.gameState = gp.watchingState;
                keyH.enterPressed = false;
                setEnergy(getEnergy() - 5);
                gp.addMinutes(15);
            } 
        }
    }

    public void selling() {
        Item sellingItem = inventory.getSelectedItem();
        Tile tileToSell = gp.map.getTile(interactionArea.x, interactionArea.y);
        ShippingBin sb = (ShippingBin) tileToSell;
        if(sb.lastday < gp.gameDay) {
            sb.binCount = 0;
            sb.lastday = gp.gameDay;
            /*money += storedMoney;
            storedMoney = 0;
            System.out.println("Player: Shipping bin has been emptied, Current Money: " + money + " coins.");*/
        }
        if (sb.binCount < sb.maxSlot) {
            if (inventory.getItemCount(sellingItem) > 0) {
                int price = sellingItem.getHargaJual();
                if (price > 0) {
                    inventory.removeItem(sellingItem, 1);
                    storedMoney += price;
                    System.out.println("Player: Sold " + sellingItem.getName() + " for " + price + " coins.");
                    System.out.println("Player: Total money now: " + storedMoney + " coins.");
                    sb.binCount++;
                } else {
                    System.out.println("Player: Cannot sell " + sellingItem.getName() + ", no selling price.");
                }
            } else {
                System.out.println("Player: No " + sellingItem.getName() + " to sell.");
            }
        }
        else{
            System.out.println("Player: Shipping bin is full, cannot sell more items.");
        }
    }

    public int getStoredMoney() {
        return storedMoney;
    }
    public void setStoredMoney(int storedMoney) {
        this.storedMoney = storedMoney;
    }
}