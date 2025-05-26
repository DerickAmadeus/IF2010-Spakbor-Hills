package Furniture;

public class TV extends Furniture{
    public TV(String name, Boolean isWalkable) {
        super(name, true);
    }
    @Override
    public void Action() {
        System.out.println("TV is being watched");
    }
}