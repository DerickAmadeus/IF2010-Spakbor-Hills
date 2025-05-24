package player;

import main.GamePanel;
import main.KeyHandler;
import Map.Tile;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import Items.Equipment;
import Items.Fish;
import Items.Item;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

//error jir

public class Player {
    public int x, y; // Player's world X and Y coordinates
    public int speed;
    public int screenX; // Player's X position on the screen (usually center)
    public int screenY; // Player's Y position on the screen (usually center)
    public Rectangle solidArea; // Collision area for the player
    public Rectangle interactionArea; // Area for checking interactions, will now align with a tile
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    GamePanel gp;
    KeyHandler keyH;
    public String direction; // Current animation/movement state (e.g., "up", "idleDown")
    String lastMoveDirection; // Last actual direction of movement input ("up", "down", "left", "right")

    public BufferedImage[] idleDownFrames, idleUpFrames, idleLeftFrames, idleRightFrames,
                           leftFrames, rightFrames, upFrames, downFrames;

    private int spriteCounter = 0;
    private int spriteNum = 0;
    private final int ANIMATION_SPEED = 10; // Frames per animation sprite
    private boolean isActuallyMoving = false;
    private Inventory<Item> inventory;
    private Equipment equippedItem;
    private int energy;
    private static final int MAX_ENERGY = 100; 
    private Tile tile; 


    // Cooldown for interaction to prevent multiple interactions from a single long key press
    private int interactionCooldown = 0;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        this.energy = MAX_ENERGY;
        this.inventory = new Inventory<>(gp);
        loadInitialEquipment();
        loadInitialFish();

        this.screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        this.screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        interactionArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        setDefaultValues();
        getPlayerImage();
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

    public void loadInitialFish() {
        Fish bullhead = new Fish("Bullhead", "Ikan Bullhead, mudah ditemukan.", 50, 50, "Any", "Any", "Mountain Lake", "Common");
        Fish carp = new Fish("Carp", "Ini Carp.", 50, 50, "Any", "Any", "Pond", "Common");
        Fish chub = new Fish("Chub", "Ikan Chub, cukup umum.", 50, 50, "Any", "Any", "Forest River", "Common");
        Fish largemouthBass = new Fish("Largemouth Bass", "Ikan besar dari danau pegunungan.", 100, 100, "Any", "Any", "Mountain Lake", "Uncommon");
        Fish rainbowTrout = new Fish("Rainbow Trout", "Ikan berwarna pelangi yang muncul saat cuaca cerah.", 120, 120, "Summer", "Sunny", "Forest River", "Uncommon");
        Fish sturgeon = new Fish("Sturgeon", "Ikan langka dari danau pegunungan.", 200, 200, "Summer", "Any", "Mountain Lake", "Rare");
        Fish midnightCarp = new Fish("Midnight Carp", "Ikan malam dari danau atau kolam.", 150, 150, "Fall", "Any", "Mountain Lake", "Uncommon");
        Fish flounder = new Fish("Flounder", "Ikan pipih dari laut.", 90, 90, "Spring", "Any", "Ocean", "Common");
        Fish halibut = new Fish("Halibut", "Ikan laut besar aktif pagi dan malam.", 110, 110, "Any", "Any", "Ocean", "Uncommon");
        Fish octopus = new Fish("Octopus", "Gurita laut yang aktif siang hari.", 180, 180, "Summer", "Any", "Ocean", "Rare");
        Fish pufferfish = new Fish("Pufferfish", "Ikan buntal beracun saat cuaca cerah.", 160, 160, "Summer", "Sunny", "Ocean", "Uncommon");
        Fish sardine = new Fish("Sardine", "Ikan kecil dari laut.", 40, 40, "Any", "Any", "Ocean", "Common");
        Fish superCucumber = new Fish("Super Cucumber", "Ikan misterius aktif malam hari.", 250, 250, "Summer", "Any", "Ocean", "Rare");
        Fish catfish = new Fish("Catfish", "Ikan lele liar saat hujan.", 130, 130, "Spring", "Rainy", "Forest River", "Uncommon");
        Fish salmon = new Fish("Salmon", "Ikan migrasi dari sungai.", 120, 120, "Fall", "Any", "Forest River", "Uncommon");
        Fish angler = new Fish("Angler", "Ikan legendaris yang hanya muncul di musim gugur.", 1000, 1000, "Fall", "Any", "Pond", "Legendary");
        Fish crimsonfish = new Fish("Crimsonfish", "Ikan legendaris dari laut tropis.", 1000, 1000, "Summer", "Any", "Ocean", "Legendary");
        Fish glacierfish = new Fish("Glacierfish", "Ikan legendaris dari sungai beku.", 1000, 1000, "Winter", "Any", "Forest River", "Legendary");
        Fish legend = new Fish("Legend", "Ikan legendaris tertinggi di danau gunung saat hujan.", 1200, 1200, "Spring", "Rainy", "Mountain Lake", "Legendary");


        // Tambahkan ke inventory
        inventory.addItem(bullhead, 13);
        inventory.addItem(carp, 3); // Misalnya ingin 3
        inventory.addItem(chub, 121);
        inventory.addItem(largemouthBass, 91);
        inventory.addItem(rainbowTrout, 51);
        inventory.addItem(sturgeon, 13);
        inventory.addItem(midnightCarp, 12);
        inventory.addItem(flounder, 10);
        inventory.addItem(halibut, 11);
        inventory.addItem(octopus, 34);
        inventory.addItem(pufferfish, 96);
        inventory.addItem(sardine, 16);
        inventory.addItem(superCucumber, 100);
        inventory.addItem(catfish, 1000);
        inventory.addItem(salmon, 112);
        inventory.addItem(angler, 1);
        inventory.addItem(crimsonfish, 1);
        inventory.addItem(glacierfish, 1);
        inventory.addItem(legend, 1);
    }


//test

    public void setDefaultValues() {
        this.x = gp.tileSize * 10; // Example: Start at tile (10,10) in world coordinates
        this.y = gp.tileSize * 10; // Example: Start at tile (10,10) in world coordinates
        this.speed = 4;
        this.lastMoveDirection = "down"; // Default facing direction
        this.direction = "idleDown";     // Default animation state
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
            if (keyH.upPressed) {
                direction = "up"; lastMoveDirection = "up";
            } else if (keyH.downPressed) {
                direction = "down"; lastMoveDirection = "down";
            } else if (keyH.leftPressed) {
                direction = "left"; lastMoveDirection = "left";
            } else if (keyH.rightPressed) {
                direction = "right"; lastMoveDirection = "right";
            }

            collisionOn = false;
            gp.cChecker.checkTile(this); // Check for tile collisions based on 'direction'

            if (!collisionOn) {
                switch (direction) { // Use 'direction' for actual movement
                    case "up":    y -= speed; isActuallyMoving = true; break;
                    case "down":  y += speed; isActuallyMoving = true; break;
                    case "left":  x -= speed; isActuallyMoving = true; break;
                    case "right": x += speed; isActuallyMoving = true; break;
                }
            }
        }

        // Enforce world boundaries
        int worldWidth = gp.worldCol * gp.tileSize;
        int worldHeight = gp.worldRow * gp.tileSize;
        if (x < 0) x = 0;
        if (x > worldWidth - gp.tileSize) x = worldWidth - gp.tileSize; // Player's width is tileSize
        if (y < 0) y = 0;
        if (y > worldHeight - gp.tileSize) y = worldHeight - gp.tileSize; // Player's height is tileSize

        // Update interaction area to be the tile in front of the player
        // Player's current tile (center point for calculation)
        int playerCurrentTileCol = (x + gp.tileSize / 2) / gp.tileSize;
        int playerCurrentTileRow = (y + gp.tileSize / 2) / gp.tileSize;

        int targetTileCol = playerCurrentTileCol;
        int targetTileRow = playerCurrentTileRow;

        switch (lastMoveDirection) { // Use lastMoveDirection to determine where the player is "facing"
            case "up":
                targetTileRow = playerCurrentTileRow - 1;
                break;
            case "down":
                targetTileRow = playerCurrentTileRow + 1;
                break;
            case "left":
                targetTileCol = playerCurrentTileCol - 1;
                break;
            case "right":
                targetTileCol = playerCurrentTileCol + 1;
                break;
        }

        // Set interactionArea to the world coordinates and size of the target tile
        interactionArea.x = targetTileCol * gp.tileSize;
        interactionArea.y = targetTileRow * gp.tileSize;
        interactionArea.width = gp.tileSize;  // Ensure it's exactly one tile wide
        interactionArea.height = gp.tileSize; // Ensure it's exactly one tile high


        // Handle Interaction Key Press
        if (keyH.interactPressed && interactionCooldown == 0) {
            interact();
            keyH.interactPressed = false;
        } 



        // Determine final animation state
        if (isAttemptingMoveByKeyPress && !collisionOn && isActuallyMoving) {
            // 'direction' is already "up", "down", etc. for walking animations
        } else {
            // Not moving or collided, switch to idle animation based on lastMoveDirection
            switch (lastMoveDirection) {
                case "up":    direction = "idleUp";    break;
                case "down":  direction = "idleDown";  break;
                case "left":  direction = "idleLeft";  break;
                case "right": direction = "idleRight"; break;
                default:      direction = "idleDown";  break; // Fallback idle state
            }
        }

        if (!prevAnimationState.equals(direction)) {
            spriteNum = 0;
            spriteCounter = 0;
        }

        // Animate sprite
        spriteCounter++;
        if (spriteCounter > ANIMATION_SPEED) {
            spriteNum++;
            BufferedImage[] currentFrames = getCurrentAnimationFrames();
            if (currentFrames != null && currentFrames.length > 0) {
                if (spriteNum >= currentFrames.length) {
                    spriteNum = 0; // Loop animation
                }
            } else {
                spriteNum = 0; // Fallback if frames are null or empty
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
            image = idleDownFrames[0]; // Fallback to first frame of idleDown
        }


        if (image == null) {
            g2.setColor(Color.RED);
            g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
            return;
        }
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

        // DEBUG: Draw Solid Area and Interaction Area
        if (gp.debugMode) {
            // Draw Solid Area (relative to player's screen position)
            g2.setColor(new Color(255, 0, 0, 100)); // Semi-transparent red
            g2.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

            // Draw Interaction Area (needs to be converted from world to screen coordinates)
            // Since interactionArea.x and .y are already world coordinates of the target tile,
            // the conversion to screen coordinates remains the same.
            g2.setColor(new Color(0, 255, 0, 100)); // Semi-transparent green
            int interactionScreenX = interactionArea.x - x + screenX;
            int interactionScreenY = interactionArea.y - y + screenY;
            g2.fillRect(interactionScreenX, interactionScreenY, interactionArea.width, interactionArea.height);
        }
        if (equippedItem != null && equippedItem.getIcon() != null) {
            int handX = screenX + gp.tileSize / 2; // atur posisi relatif tangan
            int handY = screenY + gp.tileSize / 2;

            g2.drawImage(equippedItem.getIcon(), handX, handY, gp.tileSize / 2, gp.tileSize / 2, null);
        }

    }
    public void drawEnergyBar(Graphics2D g2) {
        int barX = 20;
        int barY = 20;
        int barWidth = 200;
        int barHeight = 30;

        // Hitung proporsi energi
        double percent = (double) energy / MAX_ENERGY;
        int energyWidth = (int) (barWidth * percent);

        // Gambar background bar (gelap)
        g2.setColor(Color.darkGray);
        g2.fillRoundRect(barX, barY, barWidth, barHeight, 10, 10);

        // Tentukan warna berdasarkan persentase energi
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

        // Gambar bar energi dengan warna yang sesuai
        g2.setColor(energyColor);
        g2.fillRoundRect(barX, barY, energyWidth, barHeight, 10, 10);

        // Gambar border luar
        g2.setColor(Color.black);
        g2.drawRoundRect(barX, barY, barWidth, barHeight, 10, 10);

        // Tampilkan teks energi (di tengah bar)
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18f));
        String energyText = energy + " / " + MAX_ENERGY;
        int textX = barX + (barWidth - g2.getFontMetrics().stringWidth(energyText)) / 2;
        int textY = barY + barHeight - 8;
        g2.drawString(energyText, textX, textY);
    }


    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getSpeed() { return speed; }
    public Rectangle getSolidArea() { return solidArea; }
    public String getDirectionForCollision() { return direction; } // Current intended move direction for collision
    public String getLastMoveDirection() { return lastMoveDirection; } // Last direction player moved or faced
    public Rectangle getInteractionArea() { return interactionArea; } // The tile-aligned interaction area
    public Inventory<Item> getInventory() { return inventory;}


    // Action method for interaction
    public void interact()  {
        // Placeholder for interaction logic
        tile = gp.map.getTile(interactionArea.x, interactionArea.y);
        if (tile == null) {
            System.out.println("No tile found at interaction area.");
            return;
        } else {
        System.out.println("Interacting with tile at: " + interactionArea.x + ", " + interactionArea.y + " (Tile: " + tile.getTileName() + ")");}

        if (tile.getTileName().equals("Soil")) {
            System.out.println("Interacting with soil tile.");
            try {
                Thread.sleep(5000); // Delay for 5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Add logic for interacting with soil tile
        } else if (tile.getTileName().equals("Building")) {
            System.out.println("Interacting with building tile.");
            // Add logic for interacting with building tile
        } else {
            System.out.println("No interaction available for this tile.");
            setEnergy(getEnergy()+10);
        }
    }

    public void openInventory(Graphics2D g2) {
        inventory.drawInventory(g2);
    }

    public Item getEquippedItem() {
        return equippedItem;
    }
    public void equipItem(Equipment equipment) {
        if (equipment == null) {
            // Unequip: kosongkan equipment
            equippedItem = null;
            System.out.println("Unequipped.");
            return;
        }

        // Equip item baru
        equippedItem = equipment;
        System.out.println("Equipped: " + equipment.getName());
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        if (energy > MAX_ENERGY) {
            this.energy = MAX_ENERGY;
        } else if (energy < 0 && energy > -20) {
            // masih print
            System.out.println("Warning: Energy is low! Action can still be performed, but consider sleeping.");
            this.energy = energy;
        } else if (energy < -20) {
            // msdih print
            System.out.println("Error: Energy is too low! sleeping rn...");
        } else {
            this.energy = energy;
        }
    }

    public void tiling() {
        Tile tile = gp.map.getTile(interactionArea.x, interactionArea.y);
        if (equippedItem != null && equippedItem.getName().equals("Hoe") && energy > -20 && keyH.enterPressed) {
            if (tile != null && tile.getTileName().equals("Grass")) {
                gp.map.setTile(interactionArea.x, interactionArea.y, 10);
                setEnergy(getEnergy() - 5);
            }
        }
    }

    public void recoverLand() {
        Tile tile = gp.map.getTile(interactionArea.x, interactionArea.y);
        if ( equippedItem != null && equippedItem.getName().equals("Pickaxe") && energy > -20 && keyH.enterPressed) {
            if ( tile != null && tile.getTileName().equals("Soil")) {
                gp.map.setTile(interactionArea.x, interactionArea.y, 0);
                setEnergy(getEnergy() - 5);
            }
        }
    }

}
