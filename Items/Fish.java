package Items;

import java.util.ArrayList;

//Time nya belum
import player.Player;

public class Fish extends Item implements Sellable, Edible{
    private ArrayList<String> season;
    private ArrayList<String> weather;
    private ArrayList<String> location;
    private String rarity;
    private ArrayList<Integer> appearTime;
    private ArrayList<Integer> disappearTime;

    public Fish(String name, String description, int hargaJual, int hargaBeli,  ArrayList<String> season, ArrayList<String> weather, ArrayList<String> location, String rarity, ArrayList<Integer> appearTime, ArrayList<Integer> disappearTime){
        super(name, description, hargaJual, hargaBeli);
        this.season = season;
        this.weather = weather;
        this.location = location;
        this.rarity = rarity;
        this.appearTime = appearTime;
        this.disappearTime = disappearTime;
    }    

    //Test

    //Getter
    public  ArrayList<String> getSeason(){
        return season;
    }

    public ArrayList<String> getWeather(){
        return weather;
    }
    
    public ArrayList<String> getLocation(){
        return location;
    }

    public String getRarity(){
        return rarity;
    }

    public ArrayList<Integer> getAppearTime() {
        return appearTime;
    }

    public ArrayList<Integer> getDisappearTime() {
        return disappearTime;
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

    public int calculateHargaJual() {
        int jumlahSeason = season.size();
        int jumlahWeather = weather.size();
        int jumlahLokasi = location.size();

        // Hitung total jam kemunculan
        int totalJam = 0;
        for (int i = 0; i < appearTime.size(); i++) {
            int start = appearTime.get(i);
            int end = disappearTime.get(i);

            if (end >= start) {
                totalJam += end - start;
            } else {
                // Jam melewati tengah malam, misalnya 19 -> 2 = 7 jam
                totalJam += (24 - start) + end;
            }
        }

        if (jumlahSeason == 0 || jumlahWeather == 0 || jumlahLokasi == 0 || totalJam == 0) {
            return 0; // Hindari pembagian dengan nol
        }

        double factorSeason = 4.0 / jumlahSeason;
        double factorTime = 24.0 / totalJam;
        double factorWeather = 2.0 / jumlahWeather;
        double factorLocation = 4.0 / jumlahLokasi;

        int C;
        switch (rarity) {
            case "Common":
                C = 10;
                break;
            case "Regular":
                C = 5;
                break;
            case "Legendary":
                C = 25;
                break;
            default:
                C = 0;
        }

        double harga = factorSeason * factorTime * factorWeather * factorLocation * C;
        return (int) Math.round(harga);
    }

}