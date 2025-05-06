package NPC;

import java.util.ArrayList;
import java.util.List;

public class NPC{
    private String name;
    private String gender;
    private int heartPoints;

    private List<Item> lovedItems;
    private List<Item> likedItems;
    private List<Item> hatedItems;

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
    public NPC(String name, String gender, int heartPoints, List<Item> lovedItems, List<Item> likedItems, List<Item> hatedItems, String relationshipStatus){
        this.name = name;
        this.gender = gender;
        setHP(heartPoints);
        this.lovedItems = lovedItems != null ? lovedItems : new ArrayList<>(); // not sure but semoga bener
        this.likedItems = likedItems != null ? likedItems : new ArrayList<>();
        this.hatedItems = hatedItems != null ? hatedItems : new ArrayList<>();
        this.relationshipStatus = relationshipStatus;
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
}