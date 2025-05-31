package Items;
import main.GamePanel;
import player.Player;

public class Food extends Item implements Sellable, Buyable, Edible{
    private int energyGain;

    public Food(String name, int hargaJual, int hargaBeli, int energyGain) {
        super(name, "", hargaJual, hargaBeli);
        this.energyGain = energyGain;

        this.setDescription(
            "Sell Price: " + hargaJual + " | Buy Price: " + hargaBeli +
            " | Energy Gain: " + energyGain 
        );
    }
    
    public int getEnergyGain() {
        return energyGain;
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
            if (item.getName().equals("Fish n' Chips")) {
                gp.allRecipes[0].setUnlockInfo(true);
            } else if (item.getName().equals("Fish Sandwich")) {
                gp.allRecipes[9].setUnlockInfo(true);
            }
        }
    }

    @Override
    public void sell(GamePanel gp, Item item) {
        gp.player.getInventory().removeItem(item, 1);
        gp.player.setStoredMoney(gp.player.getStoredMoney() + item.getHargaJual());
    }

    public void eat(Player player, Item get) {
        player.getInventory().removeItem(get, 1);
        player.setEnergy(player.getEnergy() + energyGain);
        System.out.println("Eating " + getName() + " restores " + getEnergyGain());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Food food = (Food) o;

        return this.getName().equals(food.getName()); 
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode(); 
    }
}
