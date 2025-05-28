package Furniture;

public class TV extends Furniture{
    public TV(String name, Boolean isWalkable) {
        super(name, false);
    }

    public TV(TV other) {
        super(other);
    }
    @Override
    public void Action() {
        System.out.println("TV is being watched");
    }
}