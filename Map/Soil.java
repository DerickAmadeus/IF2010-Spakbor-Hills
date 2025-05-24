package Map;
import Items.Seeds;


public class Soil extends Tile {
    private int wetCooldown;
    private Seeds seedPlanted;


    public Soil(Seeds seedPlanted) {
        super("Soil", 'S', true);
        this.wetCooldown = 5;
        this.seedPlanted = null;
    }

    //Getters
    public int getWetCooldown() {
        return wetCooldown;
    }
    public Seeds getSeedPlanted() {
        return seedPlanted;
    }
    //Actions

    public void plantSeed(Seeds seed) {
        if (this.seedPlanted == null) {
            this.seedPlanted = seed;
            System.out.println("Seed " + seed.getName() + " has been planted.");
        } else {
            System.out.println("Soil is already occupied by " + this.seedPlanted.getName() + ".");
        }
    }

    public void harvestSeed() {
        if (this.seedPlanted != null) {
            System.out.println("Harvesting " + this.seedPlanted.getName() + ".");
            // Masukin ke Inventory
            this.seedPlanted = null; // Remove the seed after harvesting
        } else {
            System.out.println("No seed planted to harvest.");
        }
    }

    public void waterSeed() {
        if (this.seedPlanted != null) {
            System.out.println("Watering " + this.seedPlanted.getName() + ".");
            // Pake watering can 
            wetCooldown = 5; // Reset the wet cooldown
            
        } else {
            System.out.println("No seed planted to water.");
        }
    }

    public void plantDead() {
        if (this.seedPlanted != null && wetCooldown <= 0) {
            System.out.println("Seed " + this.seedPlanted.getName() + " has died due to lack of water.");
            this.setTileSymbol('D'); // Remove the seed after it dies
        } else {
            wetCooldown--;//Buat ngurangin cooldown (Aku gatau sih)
        }
    }
    
}
