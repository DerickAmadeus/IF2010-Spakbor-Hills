package Map;
import Items.Seeds;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import main.GamePanel;
import Items.Crops;


public class Soil extends Tile {
    private Seeds seedPlanted;
    private int wetCooldown;
    private String emptySoilImagePath; // Path to the image for empty soil
    private int dayHarvest;



    public Soil(String name, boolean isWalkable, String emptySoilImagePath) {
        super(name, isWalkable);
        this.emptySoilImagePath = emptySoilImagePath;
        this.seedPlanted = null; 
        this.wetCooldown = 0; 
        this.dayHarvest = 0;
    }

    public Soil(Soil other) {
        super(other);
        this.emptySoilImagePath = other.emptySoilImagePath;
        this.seedPlanted = null;
        this.wetCooldown = 0;
        this.dayHarvest = 0;

        if (this.Image == null && this.emptySoilImagePath != null) {
            loadImage(emptySoilImagePath);
        }
    }

    //Getters
    public boolean canPlant() {
        return seedPlanted == null;
    }

    public Seeds getSeedPlanted() {
        return this.seedPlanted;
    }

    public void plantSeed(Seeds seed, GamePanel gp) {
        if (canPlant()) {
            this.seedPlanted = seed;
            this.wetCooldown = 5; // Set the wet cooldown based on the seed
            this.dayHarvest = seed.getDaysToHarvest();
            updateImageBasedOnState(gp);
        }
    }


    public void watering(GamePanel gp) {
        this.wetCooldown = 5;
        updateImageBasedOnState(gp);
    }

    // public Crops harvest() {
    //     if (seedPlanted != null && dayHarvest <= 0) {
    //         Crops harvestcrop = null;  // Masalah nanti
    //         this.seedPlanted = null;
    //         this.wetCooldown = 0;
    //         updateImageBasedOnState(null);
    //         return harvestcrop; // Return the harvested crop


    //     }
    // }

    public void updateImageBasedOnState(GamePanel gp) { 
        if (seedPlanted != null) {
            int visualID = seedPlanted.getTileIndex(); 
            if (gp != null && visualID != -1 && visualID < gp.map.tileimage.length && gp.map.tileimage[visualID] != null) {
                 this.Image = gp.map.tileimage[visualID].Image; // Gunakan image dari prototype visual
            } else {
                System.err.println("Failed to update image for planted seed: " + seedPlanted.getName());

            }
        } else {
            // Jika tidak ada benih, tampilkan gambar tanah kosong
            loadImage(this.emptySoilImagePath);
        }
    }



    
}
