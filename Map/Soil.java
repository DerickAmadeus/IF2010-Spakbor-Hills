package Map;
import Items.Seeds;
import player.Player;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import main.GamePanel;
import Items.Crops;


public class Soil extends Tile {
    private Seeds seedPlanted;
    private int wetCooldown;
    private  final int MAX_COOLDOWN = 3;
    private String emptySoilImagePath; // Path to the image for empty soil
    private int dayHarvest;
    public int timestampMinute = 0;
    public int timestampHour = 0;
    public int timestampDay = 0;
    public int waterTimestampMinute = 0;
    public int waterTimestampHour = 0;
    public int waterTimestampDay = 0;
    

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

    public boolean canWater() {
        return wetCooldown < MAX_COOLDOWN;
    }

    public Seeds getSeedPlanted() {
        return this.seedPlanted;
    }

    public int getWetCooldown() {
        return this.wetCooldown;
    }

    public int getDaysToHarvest() {
        return dayHarvest;
    }

    public void plantSeed(Seeds seed, GamePanel gp) {
        if (canPlant()) {
            this.seedPlanted = seed;
            this.wetCooldown = 2; // Set the wet cooldown based on the seed
            this.dayHarvest = seed.getDaysToHarvest();
            this.timestampMinute = gp.gameMinute;
            this.timestampHour = gp.gameHour;
            this.timestampDay = gp.gameDay;
            this.waterTimestampMinute = gp.gameMinute;
            this.waterTimestampHour = gp.gameHour;
            this.waterTimestampDay = gp.gameDay;
            updateImageBasedOnState(gp);
        }
    }

    public void water(GamePanel gp) {
        this.wetCooldown = MAX_COOLDOWN;
        this.waterTimestampMinute = gp.gameMinute;
        this.waterTimestampHour = gp.gameHour;
        this.waterTimestampDay = gp.gameDay;
        updateImageBasedOnState(gp);
    }

    public Crops[] loadInitialCrops() {
        return new Crops[] {
            new Crops("Parsnip", "Sayuran akar musim semi", 35, 50, 1),
            new Crops("Cauliflower", "Sayuran bunga putih", 150, 200, 1),
            new Crops("Potato", "Umbi penghasil karbohidrat", 80, 0, 1),
            new Crops("Wheat", "Serealia untuk dijadikan tepung", 30, 50, 3),
            new Crops("Blueberry", "Buah kecil biru musim panas", 40, 150, 3),
            new Crops("Tomato", "Buah merah serbaguna", 60, 90, 1),
            new Crops("Hot Pepper", "Cabai pedas untuk musim panas", 40, 0, 1),
            new Crops("Melon", "Buah musim panas besar dan manis", 250, 0, 1),
            new Crops("Cranberry", "Buah musim gugur asam", 25, 0, 10),
            new Crops("Pumpkin", "Buah besar untuk musim gugur", 250, 300, 1),
            new Crops("Grape", "Buah ungu yang bisa dijadikan wine", 10, 100, 20)
        };
    }

    public void harvest(GamePanel gp, Player player) {
        Crops[] crops = loadInitialCrops();
        player.getInventory().addItem(crops[seedPlanted.getTileIndex() - 13], crops[seedPlanted.getTileIndex() - 13].getJumlahPerPanen());
        seedPlanted = null;
        updateImageBasedOnState(gp);
    }

    public void updateImageBasedOnState(GamePanel gp) { 
        if (seedPlanted != null) {
            int visualID = seedPlanted.getTileIndex(); 
            if (gp != null && visualID != -1 && visualID < gp.map.tileimage.length && gp.map.tileimage[visualID] != null) {
                if (dayHarvest > 0) {
                    if (wetCooldown == MAX_COOLDOWN) {
                        this.Image = gp.map.tileimage[visualID + Seeds.getTotalSeeds()].Image; // Gunakan image dari prototype visual
                    } else if (wetCooldown > 0) {
                        this.Image = gp.map.tileimage[visualID].Image; // Gunakan image dari prototype visual
                    } else {
                        seedPlanted = null;
                        loadImage(this.emptySoilImagePath);
                        return;
                    }
                } else {
                    if (wetCooldown >= 0) {
                        wetCooldown = MAX_COOLDOWN;
                        this.Image = gp.map.tileimage[visualID + Seeds.getTotalSeeds() * 2].Image;
                    }
                }
                if (!seedPlanted.getSeason().contains(gp.currentSeason)) {
                    seedPlanted = null;
                    loadImage(this.emptySoilImagePath);
                    return;
                }
            } else {
                System.err.println("Failed to update image for planted seed: " + seedPlanted.getName());

            }
        } else {
            // Jika tidak ada benih, tampilkan gambar tanah kosong
            loadImage(this.emptySoilImagePath);
        }
    }

    @Override
    public void update(GamePanel gp) {
        if (wetCooldown > 0 && dayHarvest > 0 && gp.gameMinute >= this.waterTimestampMinute && gp.gameHour >= this.waterTimestampHour && gp.gameDay >= this.waterTimestampDay + 1) {
            wetCooldown--;
            this.waterTimestampDay++;
        }

        if (seedPlanted != null) {
            if (dayHarvest > 0 && gp.gameMinute >= this.timestampMinute && gp.gameHour >= this.timestampHour && gp.gameDay >= this.timestampDay + 1) {
                dayHarvest--;
                this.timestampDay++;
            }
        }
    }
}
