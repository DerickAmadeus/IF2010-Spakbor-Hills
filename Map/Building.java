package Map;

public class Building extends Tile {
    private String buildingName;
    private int longitude;
    private int latitude;


    public Building(String tilename, char tileSymbol, boolean isWalkable, String buildingName, int longitude, int latitude) {
        super("Building", 'B', false);
        this.buildingName = buildingName;
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
    public void buildingInfo() {
        System.out.println("Building Name: " + buildingName);
        System.out.println("Longitude: " + longitude);
        System.out.println("Latitude: " + latitude);
    }

    // Tambahin override kalo mau bikin building satu satu
    public void actios() {
        System.out.println("Building " + buildingName + " is being used.");
    }
    
}
