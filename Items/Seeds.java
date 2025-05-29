package Items;

import java.util.ArrayList;

public class Seeds extends Item implements Buyable {
    private int daysToHarvest;
    private ArrayList<String> season;
    private int tileIndex;
    private int wetIndex;
    private static int totalSeeds = 11;

    public Seeds(String name, String description, int hargaJual, int hargaBeli, int daysToHarvest, ArrayList<String> season, int tileIndex) {
        super(name, description, hargaJual, hargaBeli);
        if (daysToHarvest < 1) {
            this.daysToHarvest = 1;
        } else {
            this.daysToHarvest = daysToHarvest;
        }
        this.season = season;
        this.tileIndex = tileIndex;
        this.wetIndex = tileIndex + totalSeeds;
    }

    public int getDaysToHarvest() {
        return daysToHarvest;
    }

    public ArrayList<String> getSeason() {
        return season;
    }

    public int getTileIndex() {
        return tileIndex;
    }

    public int getWetIndex() {
        return wetIndex;
    }

    public static int getTotalSeeds() {
        return totalSeeds;
    }

    @Override
    public void buy() {
        System.out.println("Bought " + getName());
    }
}
