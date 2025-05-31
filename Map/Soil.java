package Map;
import Items.Seeds;
import player.Player;
import main.GamePanel;
import Items.Crops;


public class Soil extends Tile {
    private Seeds seedPlanted;
    private int wetCooldown;
    private  final int MAX_COOLDOWN = 3;
    private String emptySoilImagePath; 
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
            this.wetCooldown = 2; 
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
            new Crops("Parsnip", 35, 50, 1),
            new Crops("Cauliflower", 150, 200, 1),
            new Crops("Potato", 80, 0, 1),
            new Crops("Wheat", 30, 50, 3),
            new Crops("Blueberry", 40, 150, 3),
            new Crops("Tomato", 60, 90, 1),
            new Crops("Hot Pepper", 40, 0, 1),
            new Crops("Melon", 250, 0, 1),
            new Crops("Cranberry", 25, 0, 10),
            new Crops("Pumpkin", 250, 300, 1),
            new Crops("Grape", 10, 100, 20)
        };
    }

    public void harvest(GamePanel gp, Player player) {
        Crops[] crops = loadInitialCrops();
        player.getInventory().addItem(crops[seedPlanted.getTileIndex() - 13], crops[seedPlanted.getTileIndex() - 13].getJumlahPerPanen());
        player.cropsHarvested += crops[seedPlanted.getTileIndex() - 13].getJumlahPerPanen();
        if (player.cropsHarvested > 0) {
            gp.allRecipes[6].setUnlockInfo(true);
        }
        if (crops[seedPlanted.getTileIndex() - 13].getName().equals("Hot Pepper")) {
            gp.allRecipes[7].setUnlockInfo(true);
        }
        seedPlanted = null;
        updateImageBasedOnState(gp);
    }

    public void updateImageBasedOnState(GamePanel gp) { 
        if (seedPlanted != null) {
            int visualID = seedPlanted.getTileIndex(); 
            if (gp != null && visualID != -1 && visualID < gp.map.tileImage.length && gp.map.tileImage[visualID] != null) {
                if (dayHarvest > 0) {
                    if (wetCooldown == 3) {
                        this.Image = gp.map.tileImage[visualID + Seeds.getTotalSeeds()].Image; 
                    } else if (wetCooldown > 0) {
                        this.Image = gp.map.tileImage[visualID].Image;
                    } else {
                        seedPlanted = null; 
                        loadImage(this.emptySoilImagePath); 
                        return;
                    }
                } else {
                    if (wetCooldown >= 0) {
                        wetCooldown = MAX_COOLDOWN;
                        this.Image = gp.map.tileImage[visualID + Seeds.getTotalSeeds() * 2].Image;
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
