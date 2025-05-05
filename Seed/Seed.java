package Seed;
public class Seed {
    private String seedName;
    private int growthTime;
    private int harvestTime;
    private int waterRequirement;
    private int fertilizerRequirement;
    private int currentGrowthStage;

    public Seed(String seedName, int growthTime, int harvestTime, int waterRequirement, int fertilizerRequirement) {
        this.seedName = seedName;
        this.growthTime = growthTime;
        this.harvestTime = harvestTime;
        this.waterRequirement = waterRequirement;
        this.fertilizerRequirement = fertilizerRequirement;
        this.currentGrowthStage = 0; // Initial growth stage
    }

    // Getters and Setters
    public String getSeedName() {
        return seedName;
    }

    public void setSeedName(String seedName) {
        this.seedName = seedName;
    }

    public int getGrowthTime() {
        return growthTime;
    }

    public void setGrowthTime(int growthTime) {
        this.growthTime = growthTime;
    }

    public int getHarvestTime() {
        return harvestTime;
    }

    public void setHarvestTime(int harvestTime) {
        this.harvestTime = harvestTime;
    }

    public int getWaterRequirement() {
        return waterRequirement;
    }

    public void setWaterRequirement(int waterRequirement) {
        this.waterRequirement = waterRequirement;
    }

    public int getFertilizerRequirement() {
        return fertilizerRequirement;
    }

    public void setFertilizerRequirement(int fertilizerRequirement) {
        this.fertilizerRequirement = fertilizerRequirement;
    }

    public int getCurrentGrowthStage() {
        return currentGrowthStage;
    }

    public void setCurrentGrowthStage(int currentGrowthStage) {
        this.currentGrowthStage = currentGrowthStage;
    }
    
}
