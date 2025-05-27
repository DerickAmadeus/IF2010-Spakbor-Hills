package NPC;
import Items.Item;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class NPC{
    private String name;
    private String gender;
    private int heartPoints;

    private List<Item> lovedItems;
    private List<Item> likedItems;
    private List<Item> hatedItems;

    private String relationshipStatus;

    private int x, y;
    private BufferedImage image;

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
    public NPC(String name, String gender, int heartPoints, List<Item> lovedItems, List<Item> likedItems, List<Item> hatedItems, String relationshipStatus, int x, int y){
        this.name = name;
        this.gender = gender;
        setHP(heartPoints);
        this.lovedItems = (lovedItems != null) ? lovedItems : new ArrayList<>();
        this.likedItems = (likedItems != null) ? likedItems : new ArrayList<>();
        this.hatedItems = (hatedItems != null) ? hatedItems : new ArrayList<>();
        this.relationshipStatus = relationshipStatus;
        this.x = x;
        this.y = y;
        loadImage();
    }

    private void loadImage() {
        try {
            String imagePath = getImagePathBasedOnName();
            InputStream is = getClass().getResourceAsStream(imagePath);
            
            if(is != null) {
                image = ImageIO.read(is);
            } else {
                throw new IOException("Image not found: " + imagePath);
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error loading NPC image for " + name + ": " + e.getMessage());
        }
    }

    private String getImagePathBasedOnName() {
        return switch (this.name.toLowerCase()) {
            case "mayor tadi" -> "/NPC/npcmttile.png";
            case "caroline" -> "/NPC/npcctile.png";
            default -> "/NPC/npc_default.png";
        };
    }

    public void draw(Graphics2D g2, int playerX, int playerY, int screenX, int screenY, int tileSize) {
        if (image == null) return;
        int screenXPos = x - playerX + screenX;
        int screenYPos = y - playerY + screenY;
        g2.drawImage(image, screenXPos, screenYPos, tileSize, tileSize, null);
    }

    // Getters for position
    public int getX() { return x; }
    public int getY() { return y; }

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
}