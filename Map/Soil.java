package Map;
import Seed.Seed;


public class Soil extends Tile {
    private int wetCooldown;
    private Seed seedPlanted;
    private int fertilizeCooldown;
    private boolean dead;


    public Soil() {
        super("Soil", 'S', true);
        this.wetCooldown = 5;
        this.fertilizeCooldown = 0;
        this.seedPlanted = null;
        this.dead = false;
    }

    //Getters
    public int getWetCooldown() {
        return wetCooldown;
    }
    public Seed getSeedPlanted() {
        return seedPlanted;
    }
    //Actions

    public void plantSeed(Seed seed) {
        if (this.seedPlanted == null) {
            this.seedPlanted = seed;
            System.out.println("Seed " + seed.getSeedName() + " has been planted.");
            boolean dead = false;
        } else {
            System.out.println("Soil is already occupied by " + this.seedPlanted.getSeedName() + ".");
        }
    }

    public void harvestSeed() {
        if (this.dead == true) {
            System.out.println("Seed " + this.seedPlanted.getSeedName() + " has died and cannot be harvested.");
            return;
        }


        if (this.seedPlanted != null) {
            System.out.println("Harvesting " + this.seedPlanted.getSeedName() + ".");
            // Masukin ke Inventory
            this.seedPlanted = null; // Remove the seed after harvesting
        } else {
            System.out.println("No seed planted to harvest.");
        }
    }

    public void waterSeed() {
        if (this.seedPlanted != null) {
            System.out.println("Watering " + this.seedPlanted.getSeedName() + ".");
            // Pake watering can 
            wetCooldown = 5; // Reset the wet cooldown
            
        } else {
            System.out.println("No seed planted to water.");
        }
    }

    public void plantDead() {
        if (this.seedPlanted != null && wetCooldown <= 0) {
            System.out.println("Seed " + this.seedPlanted.getSeedName() + " has died due to lack of water.");
            this.setTileSymbol('D'); // Remove the seed after it dies
        } else {
            wetCooldown--;//Buat ngurangin cooldown (Aku gatau sih)
        }
    }

    public void fertilizeSeed() {
        if (this.seedPlanted != null) {
            System.out.println("Fertilizing " + this.seedPlanted.getSeedName() + ".");
            // Pake fertilizer
            fertilizeCooldown = 5; // Reset the fertilize cooldown
        } else {
            System.out.println("No seed planted to fertilize.");
        }
    }
    
}
