package Items;

public class Seeds extends Item implements Buyable {
    private int daysToHarvest;
    private String season;
    private int tileIndex;

    public Seeds(String name, String description, int hargaJual, int hargaBeli, int daysToHarvest, String season, int tileIndex) {
        super(name, description, hargaJual, hargaBeli);
        if (daysToHarvest < 1) {
            this.daysToHarvest = 1;
        } else {
            this.daysToHarvest = daysToHarvest;
        }
        this.season = season;
        this.tileIndex = tileIndex;
    }

    public int getDaysToHarvest() {
        return daysToHarvest;
    }

    public String getSeason() {
        return season;
    }

    public int getTileIndex() {
        return tileIndex;
    }

    @Override
    public void buy() {
        System.out.println("Bought " + getName());
    }
}
