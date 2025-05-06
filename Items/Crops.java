package Items;

public class Crops extends Item implements Sellable, Buyable, Edible{
    private int jumlahPerPanen;

    public Crops(String name, String description, int hargaJual, int hargaBeli, int jumlahPerPanen) {
        super(name, description, hargaJual, hargaBeli);
        this.jumlahPerPanen = jumlahPerPanen; //setter
    }
    
    public int getJumlahPerPanen() {
        return jumlahPerPanen;
    }


    public void buy() {
        System.out.println("Bought " + getName());
    }

    public void sell() {
        System.out.println("Sold " + getName() + " for " + getHargaJual());
    }

    public void eat() {
        
    }
}
