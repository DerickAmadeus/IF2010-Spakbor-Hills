package Items;

import main.GamePanel;

public class Misc extends Item implements Buyable {
    
    public Misc(String name, String description, int hargaJual, int hargaBeli) {
        super(name, description, hargaJual, hargaBeli);
    }

    public void vanishItem(){
        System.out.println(getName() + " has vanished from existence.");
    }
    
    @Override
    public void buy(GamePanel gp, Item item, int amount) {
        if (gp.player.getMoney() - (item.getHargaBeli() * amount) < 0) {
            System.out.println("Insufficient Balance!");
        } else {
            gp.player.setMoney(gp.player.getMoney() - (item.getHargaBeli() * amount));
            gp.player.getInventory().addItem(item, amount);
            gp.seller.getInventory().removeItem(item, amount);
            System.out.println("Bought " + getName());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Misc misc = (Misc) o;

        return this.getName().equals(misc.getName()); // Atau sesuaikan dengan ID unik yang kamu punya
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode(); // Atau kombinasi field yang unik
    }
}
