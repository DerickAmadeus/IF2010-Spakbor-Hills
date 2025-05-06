package Items;

public class Seeds extends Item implements Buyable {
    private int daysToHarvest;
    public String season;

    public Seeds(String name, String description, int hargaJual, int hargaBeli, int daysToHarvest, String season) {
        super(name, description, hargaJual, hargaBeli);
        if (daysToHarvest < 1) {
            this.daysToHarvest = 1;
        } else {
            this.daysToHarvest = daysToHarvest;
        }
        this.season = season;
    }

    public int getDaysToHarvest() {
        return daysToHarvest;
    }

    public String getSeason() {
        return season;
    }

    @Override
    public void buy() {
        System.out.println("Bought " + getName());
    }
}
