package Items;

import java.util.ArrayList;

import main.GamePanel;

public class Seeds extends Item implements Buyable, Sellable {
    private int daysToHarvest;
    private ArrayList<String> season;
    private int tileIndex;
    private int wetIndex;
    private static int totalSeeds = 11;

    public Seeds(String name, int hargaJual, int hargaBeli, int daysToHarvest, ArrayList<String> season, int tileIndex) {
        super(name, "", hargaJual, hargaBeli);
        if (daysToHarvest < 1) {
            this.daysToHarvest = 1;
        } else {
            this.daysToHarvest = daysToHarvest;
        }
        this.season = season;
        this.tileIndex = tileIndex;
        this.wetIndex = tileIndex + totalSeeds;
        
        String seasonString = String.join(", ", season);
        this.setDescription(
            "Sell Price: " + hargaJual + " | Buy Price: " + hargaBeli +
            " | Days to Harvest: " + this.daysToHarvest + " days" +
            " | Grows on: " + seasonString
        );
    }

    public int getDaysToHarvest() {
        return daysToHarvest;
    }

    public ArrayList<String> getSeason() {
        return season;
    }

    public int getTileIndex() {
        return tileIndex;
    }

    public int getWetIndex() {
        return wetIndex;
    }

    public static int getTotalSeeds() {
        return totalSeeds;
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

    @Override
    public void sell(GamePanel gp, Item item) {
        gp.player.getInventory().removeItem(item, 1);
        gp.player.setStoredMoney(gp.player.getStoredMoney() + item.getHargaJual());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Seeds food = (Seeds) o;

        return this.getName().equals(food.getName()); 
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode(); 
    }
}
