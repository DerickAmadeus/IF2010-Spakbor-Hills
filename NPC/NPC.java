package NPC;
import Items.Item;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import main.GamePanel;

public class NPC{
    private String name;
    private String gender;
    private int heartPoints;

    private List<Item> lovedItems;
    private List<Item> likedItems;
    private List<Item> hatedItems;

    public int worldX, worldY;
    public int speed;
    public BufferedImage npcImage;
    public Rectangle solidArea;
    public String direction = "down";

    private String relationshipStatus;

    /**
     * @param name               NPC name
     * @param gender             NPC gender
     * @param heartPoints        initial heart‑point value (0‑150)
     * @param lovedItems         list of loved items
     * @param likedItems         list of liked items
     * @param hatedItems         list of hated items
     * @param relationshipStatus initial relationship status
     */

    //Constructor
    public NPC(String name, int x, int y, String gender, int heartPoints, 
              List<Item> lovedItems, List<Item> likedItems, List<Item> hatedItems, 
              String relationshipStatus){
        this.name = name;
        this.worldX = x;
        this.worldY = y;
        this.speed = 2;
        this.name = name;
        this.gender = gender;
        setHP(heartPoints);
        this.lovedItems = lovedItems != null ? lovedItems : new ArrayList<>(); // not sure but semoga bener
        this.likedItems = likedItems != null ? likedItems : new ArrayList<>();
        this.hatedItems = hatedItems != null ? hatedItems : new ArrayList<>();
        this.relationshipStatus = relationshipStatus;
        getNpcImage();
        initializeSolidArea();
    }

    //Getter
    public String getName(){
        return name;
    }

    public String getGender(){
        return gender;
    }

    public int getHP(){
        return heartPoints;
    }

    public String getrelationshipStatus(){
        return relationshipStatus;
    }

    //Setter
    public void setHP(int heartPoints){
        if (heartPoints < 0){
            this.heartPoints = 0;
        }
        else if (heartPoints > 150){
            this.heartPoints = 150;
        }
        else{
            this.heartPoints = heartPoints;
        }
    }

    public void setRelationship(String status){
        this.relationshipStatus = status;
    }

    //Actions
    public void printLovedItem(){
        printItemList("Loved Items", lovedItems);
    }

    public void printLikedItems(){
        printItemList("Liked Items", likedItems);
    }

    public void printHatedItems(){
        printItemList("Hated Items", hatedItems);
    }

    //tambahan
    private void printItemList(String title, List<Item> list){
        System.out.println(name +"'s" + title);
        if (list == null || list.isEmpty()){
            System.out.println("Empty");
        }
        else{
            for (Item item : list) {            
                System.out.println("- " + item.getName());
            }
        }
    }

    private void getNpcImage() {
        try {
            String imagePath = getImagePathBasedOnName();
            InputStream is = getClass().getResourceAsStream(imagePath);
            
            if(is != null) {
                npcImage = ImageIO.read(is);
            } else {
                throw new IOException("Image not found: " + imagePath);
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error loading NPC image for " + name + ": " + e.getMessage());
            createFallbackImage();
        }
    }

    private void createFallbackImage() {
        npcImage = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = npcImage.createGraphics();
        g.setColor(Color.PINK); // Easily noticeable fallback color
        g.fillRect(0, 0, 48, 48);
        g.dispose();
    }

    private String getImagePathBasedOnName() {
        return switch (this.name.toLowerCase()) {
            case "mayor" -> "/NPC/npcmttile.png";
            case "caroline" -> "/NPC/npcctile.png";
            default -> "/NPC/npc_default.png";
        };
    }

    private void initializeSolidArea() {
        solidArea = new Rectangle(8, 16, 32, 32); // Similar to player collision
    }

    public void draw(Graphics2D g2, GamePanel gp) {
        int screenX = worldX - gp.player.x + gp.player.screenX;
        int screenY = worldY - gp.player.y + gp.player.screenY;
        
        // Only draw NPCs that are within the screen
        if(worldX + gp.tileSize > gp.player.x - gp.player.screenX &&
           worldX - gp.tileSize < gp.player.x + gp.player.screenX &&
           worldY + gp.tileSize > gp.player.y - gp.player.screenY &&
           worldY - gp.tileSize < gp.player.y + gp.player.screenY) {
            
            g2.drawImage(npcImage, screenX, screenY, gp.tileSize, gp.tileSize, null);
            
            if(gp.debugMode) {
                // Draw collision box
                g2.setColor(new Color(255, 0, 0, 100));
                g2.fillRect(screenX + solidArea.x, screenY + solidArea.y, 
                          solidArea.width, solidArea.height);
            }
        }
    }
}