package player;

import main.GamePanel;
import main.KeyHandler;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Player {
    public int x, y; // Player's world X and Y coordinates
    public int speed;
    public int screenX; // Player's X position on the screen (usually center)
    public int screenY; // Player's Y position on the screen (usually center)
    public Rectangle solidArea; // Collision area for the player
    public boolean collisionOn = false; // Flag set by CollisionChecker
    GamePanel gp;
    KeyHandler keyH;
    public String direction; // Current animation/movement state (e.g., "up", "idleDown")
    String lastMoveDirection; // Last actual direction of movement input ("up", "down", "left", "right")

    public BufferedImage[] idleDownFrames, idleUpFrames, idleLeftFrames, idleRightFrames,
                           leftFrames, rightFrames, upFrames, downFrames;

    private int spriteCounter = 0;
    private int spriteNum = 0;
    private final int ANIMATION_SPEED = 10;
    private boolean isActuallyMoving = false; // Tracks if the player's position changed in this frame

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        // Player's position on the screen is usually fixed (center) for a scrolling camera
        this.screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        this.screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        // Define the player's solid area relative to its top-left (x,y)
        // Adjust these values (8, 16, 32, 32) based on your sprite
        // (xOffset, yOffset, width, height)
        solidArea = new Rectangle(8, 10, 32, 32); // Example: 8px inset from left, 16px from top, 32x32 size

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        // Set player's starting position in the world
        this.x = gp.tileSize * 10; // Example: Start at tile (10,10)
        this.y = gp.tileSize * 10; // Example: Start at tile (10,10)
        this.speed = 4;
        this.lastMoveDirection = "down"; // Default facing direction when idle
        this.direction = "idleDown";     // Default animation state
    }

    public void getPlayerImage() {
        // (Your existing getPlayerImage logic - seems fine)
        // Example:
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
        // (Your existing loadAnimationFrames logic - seems fine)
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
                    System.err.println("Tidak dapat memuat gambar: " + imagePath);
                    frames[i] = createPlaceholderImage();
                } else {
                    frames[i] = ImageIO.read(is);
                    is.close();
                }
            }
        } catch (IOException e) {
            System.err.println("Error saat memuat animasi untuk identifier: " + animationIdentifier);
            e.printStackTrace();
            for (int i = 0; i < frameCount; i++) {
                if (frames[i] == null) frames[i] = createPlaceholderImage();
            }
        }
        return frames;
    }

    private BufferedImage createPlaceholderImage() {
        // (Your existing createPlaceholderImage logic - seems fine)
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
        String prevAnimationState = direction; // For resetting animation frames
        isActuallyMoving = false; // Reset: Did the player's position change this frame?

        boolean isAttemptingMoveByKeyPress = keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed;

        if (isAttemptingMoveByKeyPress) {
            // Determine intended direction from key press
            if (keyH.upPressed) {
                direction = "up";
                lastMoveDirection = "up";
            } else if (keyH.downPressed) {
                direction = "down";
                lastMoveDirection = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
                lastMoveDirection = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
                lastMoveDirection = "right";
            }

            // Check for tile collisions for the intended move
            collisionOn = false; // Reset before check
            // gp.cChecker.checkTile(this) should set this.collisionOn to true if the *next step* in
            // the current 'direction' would cause a collision. It should NOT move the player.
            gp.cChecker.checkTile(this);

            // If no tile collision, then update player's world coordinates
            if (!collisionOn) {
                switch (direction) {
                    case "up":    y -= speed; isActuallyMoving = true; break;
                    case "down":  y += speed; isActuallyMoving = true; break;
                    case "left":  x -= speed; isActuallyMoving = true; break;
                    case "right": x += speed; isActuallyMoving = true; break;
                }
            }
        }

        // Enforce world boundaries AFTER any potential movement
        // Player's x, y is top-left. Player's visual width/height is gp.tileSize.
        // The solidArea might be smaller, but for boundary checks, using the visual extent is safer.
        int worldWidth = gp.worldCol * gp.tileSize;
        int worldHeight = gp.worldRow * gp.tileSize;

        if (x < 0) {
            x = 0;
        }
        // Player's right edge (x + playerWidth) should not exceed worldWidth.
        if (x > worldWidth - gp.tileSize) { // Assumes player visual width is gp.tileSize
            x = worldWidth - gp.tileSize;
        }
        if (y < 0) {
            y = 0;
        }
        // Player's bottom edge (y + playerHeight) should not exceed worldHeight.
        if (y > worldHeight - gp.tileSize) { // Assumes player visual height is gp.tileSize
            y = worldHeight - gp.tileSize;
        }

        // Determine final animation state (idle or walking)
        if (isAttemptingMoveByKeyPress && !collisionOn && isActuallyMoving) {
            // Player is successfully moving, 'direction' is already "up", "down", etc.
            // No change needed to 'direction' for animation here.
        } else {
            // Not attempting to move, or was attempting but collided, or movement was clamped by boundary
            // Switch to idle animation based on the last intended movement direction
            switch (lastMoveDirection) {
                case "up":    direction = "idleUp";    break;
                case "down":  direction = "idleDown";  break;
                case "left":  direction = "idleLeft";  break;
                case "right": direction = "idleRight"; break;
                default:      direction = "idleDown";  break; // Default idle state
            }
        }

        // Reset animation frame if the animation state (e.g. "walkLeft" to "idleLeft") has changed
        if (!prevAnimationState.equals(direction)) {
            spriteNum = 0;
            spriteCounter = 0;
        }

        // Animate sprite
        // Only animate if the current state is a walking animation OR if it's an idle animation
        // (some idle animations might have multiple frames)
        // The currentFrames.length check will handle single-frame idle animations correctly.
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
        // (Your existing getCurrentAnimationFrames logic - seems fine)
        switch (direction) {
            case "up": return (upFrames != null && upFrames.length > 0) ? upFrames : null;
            case "down": return (downFrames != null && downFrames.length > 0) ? downFrames : null;
            case "left": return (leftFrames != null && leftFrames.length > 0) ? leftFrames : null;
            case "right": return (rightFrames != null && rightFrames.length > 0) ? rightFrames : null;
            case "idleUp": return (idleUpFrames != null && idleUpFrames.length > 0) ? idleUpFrames : null;
            case "idleDown": return (idleDownFrames != null && idleDownFrames.length > 0) ? idleDownFrames : null;
            case "idleLeft": return (idleLeftFrames != null && idleLeftFrames.length > 0) ? idleLeftFrames : null;
            case "idleRight": return (idleRightFrames != null && idleRightFrames.length > 0) ? idleRightFrames : null;
            default:
                if (idleDownFrames != null && idleDownFrames.length > 0) return idleDownFrames;
                return null;
        }
    }

    public void drawPlayer(Graphics2D g2) {
        // (Your existing drawPlayer logic - seems fine, draws at screenX, screenY)
        BufferedImage image = null;
        BufferedImage[] currentFrames = getCurrentAnimationFrames();

        if (currentFrames != null && currentFrames.length > 0) {
            if (spriteNum >= currentFrames.length) {
                spriteNum = 0;
            }
            image = currentFrames[spriteNum];
        }

        if (image == null) {
            // Fallback: draw a red square if image is somehow null
            g2.setColor(Color.RED);
            // Draw at screenX, screenY because player is usually centered on screen
            g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
            // System.err.println("Fallback: Player image null for direction: " + direction);
            return;
        }
        // Player is drawn at their screenX, screenY position (center of screen)
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }

    // Getters for world coordinates, if needed by other classes like CollisionChecker or Map
    public int getX() { return x; }
    public int getY() { return y; }
    // Getter for speed, if needed
    public int getSpeed() { return speed; }
    // Getter for solidArea, if needed
    public Rectangle getSolidArea() { return solidArea; }
    // Getter for direction, if needed by CollisionChecker
    public String getDirection() { return direction; }

}
