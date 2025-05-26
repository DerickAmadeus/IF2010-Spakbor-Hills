package Items;
import player.Player;

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

    public void eat(Player player, Item get) {        
        player.getInventory().removeItem(get, 1);
        player.setEnergy(player.getEnergy() + 3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Crops crops = (Crops) o;

        return this.getName().equals(crops.getName()); // Atau sesuaikan dengan ID unik yang kamu punya
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode(); // Atau kombinasi field yang unik
    }

}
