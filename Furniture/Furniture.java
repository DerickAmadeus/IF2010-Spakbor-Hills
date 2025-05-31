package Furniture;
import Map.Tile;

abstract class Furniture extends Tile {

    public Furniture(String name, boolean isWalkable){
        super(name, isWalkable);
    }

    public Furniture(Furniture other) {
        super(other);
    }
    
    abstract void Action();
}