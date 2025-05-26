package Items;

//Time nya belum
import player.Player;

public class Fish extends Item implements Sellable, Edible{
    private String season;
    private String weather;
    private String location;
    private String rarity;

    public Fish(String name, String description, int hargaJual, int hargaBeli, String season, String weather, String location, String rarity){
        super(name, description, hargaJual, hargaBeli);
        this.season = season;
        this.weather = weather;
        this.location = location;
        this.rarity = rarity;
    }    

    //Test

    //Getter
    public String getSeason(){
        return season;
    }

    public String getWeather(){
        return weather;
    }
    
    public String getLocation(){
        return location;
    }

    public String getRarity(){
        return rarity;
    }


    @Override
    public void sell() {
        System.out.println("Sold " + getName() + " for " + getHargaJual());
    }

    public void eat(Player player, Item get) {
        player.getInventory().removeItem(get, 1);
        player.setEnergy(player.getEnergy() + 1);
        System.out.println("Eating " + getName() + " restores 1 energy.");
    }
}