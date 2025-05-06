package Items;

public class Crops extends Item implements Sellable, Buyable, Edible{
    private int jumlahPerPanen;

    public Crops(String name, String description, int hargaJual, int hargaBeli, int jumlahPerPanen) {
        super(name, description, hargaJual, hargaBeli);
        this.jumlahPerPanen = jumlahPerPanen; //setter
    }
    

    public void buy() {

    }

    public void sell() {

    }

    public void eat() {
        
    }
}
