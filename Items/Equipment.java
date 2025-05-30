package Items;

import main.GamePanel;

public class Equipment extends Item implements Buyable{
    
    public Equipment(String name, String description, int hargaJual, int hargaBeli) {
        super(name, description, hargaJual, hargaBeli);
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
}
