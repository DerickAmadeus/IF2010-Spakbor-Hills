package Furniture;
import Items.Item;
import Items.Misc;

public class Stove extends Furniture{
    private int fuelCapacity;
    public Stove(String name) {
        super(name, false);
        this.fuelCapacity = 0;
    }

    public int getFuelCapacity() {
        return fuelCapacity;
    }
    
    @Override
    public void Action() {
        System.out.println("Stove is being used");
        
    }
    public void isiFuel(Misc fuel) {

        if (fuel.getName().equals("FireWood")) {
            fuelCapacity = 1;

        } else if (fuel.getName().equals("Coal")){
            fuelCapacity = 2;
        }
    }
    public void masak(Item Recipe){
        System.out.println("Cooking " + Recipe.getName());
    }
}