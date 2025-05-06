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
        System.out.println("Bought " + getName());
    }

    public void sell() {
        System.out.println("Sold " + getName() + " for " + getHargaJual());
    }

    public void eat() {
        System.out.println("Eating " + getName() + " restores " + getEnergyGain());
    }
}
