package Furniture;
import Items.Item;
import Items.Misc;

public class Stove extends Furniture{
    private Misc fuel;
    private int cookingFuel = 0;

    public Stove(String name) {
        super(name, false);
        this.fuel = null;
    }
    public Stove(Stove other) {
        super(other);
        this.fuel = other.fuel;
    }
    public Misc getFuel() {
        return fuel;
    }
    public int getcookingFuel(){
        return cookingFuel;
    }
    @Override
    public void Action() {
        System.out.println("Stove is being used");
    }
    public void isiFuel(Misc fuel) {
        if (fuel.getName().equals("Firewood")) {
            this.fuel = fuel;
            cookingFuel = 1;
        } else if (fuel.getName().equals("Coal")) {
            this.fuel = fuel;
            cookingFuel = 2;
        }
    }
    public void masak(Item Recipe){
        System.out.println("Cooking " + Recipe.getName());
    }
}