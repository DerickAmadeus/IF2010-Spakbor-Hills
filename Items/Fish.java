package Items;

import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;
import java.awt.BasicStroke;
import java.util.ArrayList;

import main.GamePanel;
import player.Player;

public class Fish extends Item implements Sellable, Edible{
    private ArrayList<String> season;
    private ArrayList<String> weather;
    private ArrayList<String> location;
    private String rarity;
    private ArrayList<Integer> appearTime;
    private ArrayList<Integer> disappearTime;

    public Fish(String name, int hargaJual, int hargaBeli, ArrayList<String> season, ArrayList<String> weather,
        ArrayList<String> location, String rarity, ArrayList<Integer> appearTime, ArrayList<Integer> disappearTime) {
        super(name, "", hargaJual, hargaBeli);
        this.season = season;
        this.weather = weather;
        this.location = location;
        this.rarity = rarity;
        this.appearTime = appearTime;
        this.disappearTime = disappearTime;

        String seasonStr = String.join(", ", season);
        String weatherStr = String.join(", ", weather);
        String locationStr = String.join(", ", location);

        StringBuilder timeBuilder = new StringBuilder();
        for (int i = 0; i < appearTime.size(); i++) {
            String appear = (appearTime.get(i) < 10 ? "0" : "") + appearTime.get(i) + ":00";
            String disappear = (disappearTime.get(i) < 10 ? "0" : "") + disappearTime.get(i) + ":00";
            timeBuilder.append(appear).append(" - ").append(disappear);
            if (i < appearTime.size() - 2) {
                timeBuilder.append(", ");
            } else if (i == appearTime.size() - 2) {
                timeBuilder.append(" and ");
            }
        }

        this.setDescription(
            "Sell Price: " + hargaJual +
            " | Season: " + seasonStr +
            " | Weather: " + weatherStr +
            " | Location: " + locationStr +
            " | Rarity: " + rarity +
            " | Time: " + timeBuilder.toString()
        );
    }    

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
    public void sell(GamePanel gp, Item item) {
        gp.player.getInventory().removeItem(item, 1);
        gp.player.setStoredMoney(gp.player.getStoredMoney() + item.getHargaJual());
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

        int totalJam = 0;
        for (int i = 0; i < appearTime.size(); i++) {
            int start = appearTime.get(i);
            int end = disappearTime.get(i);

            if (end >= start) {
                totalJam += end - start;
            } else {
                totalJam += (24 - start) + end;
            }
        }

        if (jumlahSeason == 0 || jumlahWeather == 0 || jumlahLokasi == 0 || totalJam == 0) {
            return 0; 
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
    public void fishingWin(GamePanel gp, Graphics2D g2) {
        int x = 200, y = 100, w = 400, h = 100;

        Color c = new Color(0,0,0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, w, h, 35, 35);
        c = new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, w-10, h-10, 25, 25);

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24F));
        g2.drawImage(getIcon(), x + 20, y + 40, gp.tileSize - 2 * 10, gp.tileSize - 2 * 10, null);
        g2.drawString("You Caught " + this.getName() + "!", x + 60, y + 60);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fish fish = (Fish) o;

        return this.getName().equals(fish.getName()); 
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode(); 
    }
}