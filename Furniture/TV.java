package Furniture;

public class TV extends Furniture{
    public TV(String name, int width, int height, int symbol){
        super(name, width, height, symbol);
    }
    @Override
    public void Action() {
        System.out.println("TV is being watched");
    }
}