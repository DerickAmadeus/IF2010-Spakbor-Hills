package Universe;

import Playerstuffs.Player;
import Map.Map;
import player.Player;
import java.time.LocalDateTime;

public class Universe {
    private String farmName;
    private Player player;
    private Map farmMap;
    private String currentTime = "00:00";
    private String[] season = {"Spring", "Summer", "Fall", "Winter"};
    private String[] weather = {"Sunny", "Rainy", "Snowy", "Windy"};


    public Universe(String farmName, Player player, int width, int height) {
        this.farmName = farmName;
        this.player = player;
        this.farmMap = new Map(width, height);
    }


    // Getters

    public String getFarmName() {
        return farmName;
    }
    public Player getPlayer() {
        return player;
    }
    public Map getFarmMap() {
        return farmMap;
    } 
    public String getCurrentTime() {
        return currentTime;
    }

    public String getCurrentSeason() {
        int currentMonth = Integer.parseInt(currentTime.split(":")[0]) / 3; // Assuming each season lasts 3 months
        return season[currentMonth % season.length];
    }
    public String getCurrentWeather() {
        int currentHour = Integer.parseInt(currentTime.split(":")[0]);
        return weather[currentHour % weather.length];
    }

    // Setters
    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    public void setFarmMap(Map farmMap) {
        this.farmMap = farmMap;
    }
    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
    public void setCurrentSeason(String[] season) {
        this.season = season;
    }
    public void setCurrentWeather(String[] weather) {
        this.weather = weather;
    }
    // Methods
    public void displayFarmInfo() {
        System.out.println("Farm Name: " + farmName);
        System.out.println("Player Name: " + player.getName());
        System.out.println("Current Time: " + currentTime);
        System.out.println("Current Season: " + getCurrentSeason());
        System.out.println("Current Weather: " + getCurrentWeather());
    }

    public void addTime(int hours, int minutes) {
        String[] timeParts = currentTime.split(":");
        int currentHours = Integer.parseInt(timeParts[0]);
        int currentMinutes = Integer.parseInt(timeParts[1]);

        currentMinutes += minutes;
        if (currentMinutes >= 60) {
            currentHours += currentMinutes / 60;
            currentMinutes %= 60;
        }

        currentHours += hours;
        if (currentHours >= 24) {
            currentHours %= 24;
        }

        currentTime = String.format("%02d:%02d", currentHours, currentMinutes);
    }





    
}
