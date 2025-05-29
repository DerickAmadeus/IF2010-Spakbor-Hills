package Furniture;
import Items.Misc;
import Items.Food;
import main.GamePanel;

public class Stove extends Furniture{
    private Misc fuel;
    private int cookingFuel = 0;
    private Food food = null;
    private int amount = 0;
    public int timestampMinute = 0;
    public int timestampHour = 0;
    public int timestampDay = 0;

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
    public Food getFood() {
        return food;
    }
    public int getAmount() {
        return amount;
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

    public void cook(GamePanel gp, Food food) {
        gp.player.setEnergy(gp.player.getEnergy() - 10);
        this.food = food;
        amount = cookingFuel;
        timestampDay = gp.gameDay;
        timestampHour = gp.gameHour;
        timestampMinute = gp.gameMinute;
        fuel = null;
        cookingFuel = 0;
    }
    @Override
    public void update(GamePanel gp) {
        if (food != null) {
            int currentTotalMinutes = gp.gameDay * 24 * 60 + gp.gameHour * 60 + gp.gameMinute;
            int timestampTotalMinutes = this.timestampDay * 24 * 60 + this.timestampHour * 60 + this.timestampMinute;

            if (currentTotalMinutes >= timestampTotalMinutes + 60) {
                gp.player.getInventory().addItem(food, amount);
                food = null;
                amount = 0;
                timestampDay = 0;
                timestampHour = 0;
                timestampMinute = 0;
            }
        }
    }
}