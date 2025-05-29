package BuildingStuff;

import Map.Tile;

public class Building extends Tile {
    private String buildingName;
    private int width;
    private int height;
    private char symbol;
    private int longitude;
    private int latitude;


    public Building(String tilename, char tileSymbol, boolean isWalkable, String buildingName, int width, int height, char symbol, int longitude, int latitude){
        super("Building", 'B', false);
        this.buildingName = buildingName;
        this.width = width;
        this.height = height;
        this.symbol = symbol;
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public String getBuildingName() {
        return buildingName;
    }
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }
    public int getLongitude() {
        return longitude;
    }
    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }
    public int getLatitude() {
        return latitude;
    }
    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height; 
    }
    public char getSymbol() {
        return symbol;
    }
    public void buildingInfo() {
        System.out.println("Building Name: " + buildingName);
        System.out.println("Longitude: " + longitude);
        System.out.println("Latitude: " + latitude);
    }

    // Tambahin override kalo mau bikin building satu satu
    public void actions() {
        System.out.println("Building " + buildingName + " is being used.");
    }
    
}