package Items;
import main.GamePanel;
import player.Player;

public class Crops extends Item implements Sellable, Buyable, Edible{
    private int jumlahPerPanen;

    public Crops(String name, int hargaJual, int hargaBeli, int jumlahPerPanen) {
        super(name, "", hargaJual, hargaBeli);
        this.jumlahPerPanen = jumlahPerPanen; 

        this.setDescription(
            "Sell Price: " + hargaJual + " | Buy Price: " + hargaBeli
            + " | Amount Harvested: " + jumlahPerPanen
        );
    }
    
    public int getJumlahPerPanen() {
        return jumlahPerPanen;
    }

    @Override
    public void buy(GamePanel gp, Item item, int amount) {
        if (gp.player.getMoney() - (item.getHargaBeli() * amount) < 0) {
            System.out.println("Insufficient Balance!");
        } else {
            gp.player.setMoney(gp.player.getMoney() - (item.getHargaBeli() * amount));
            gp.player.getInventory().addItem(item, amount);
            gp.seller.getInventory().removeItem(item, amount);
            gp.player.totalExpenditure += (item.getHargaBeli() * amount);
            System.out.println("Bought " + getName());
        }
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

        return this.getName().equals(crops.getName()); 
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode(); 
    }

}
