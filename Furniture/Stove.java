package Furniture;
import Items.Item;
import Items.Misc;

public class Stove extends Furniture{
    private Misc fuel;
    public Stove(String name, int width, int height, char symbol, Misc fuel){
        super(name, width, height, symbol);
        this.fuel = fuel;
    }
    public Misc getFuel(){
        return fuel;
    }
    @Override
    public void Action() {
        System.out.println("Stove is being used");
    }
    public void isiFuel(Misc fuel) {
        this.fuel = fuel;
    }
    public void masak(Item Recipe){
        System.out.println("Cooking " + Recipe.getName());
    }
}