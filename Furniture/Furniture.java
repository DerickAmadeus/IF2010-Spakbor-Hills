package Furniture;
import Map.Tile;

abstract class Furniture extends Tile {
    private String name;
    private boolean isWalkable;

    public Furniture(String name, boolean isWalkable){
        super(name, isWalkable);
        this.name = name;
        this.isWalkable = isWalkable;
    }
    
    
    
    
    
    
    
    
    abstract void Action();
}