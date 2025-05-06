package Items;

public class Food extends Item implements Sellable, Buyable, Edible{
    private int energyGain;

    public Food(String name, String description, int hargaJual, int hargaBeli, int energyGain) {
        super(name, description, hargaJual, hargaBeli);
        this.energyGain = energyGain; //setter
    }
    
    public int getEnergyGain() {
        return energyGain;
    }

    public void buy() {

    }

    public void sell() {

    }

    public void eat() {

    }
}
