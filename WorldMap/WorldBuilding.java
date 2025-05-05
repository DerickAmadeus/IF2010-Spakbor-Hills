package WorldMap;

abstract class WorldBuilding {
    private String buildingName;
    


    public WorldBuilding(String buildingName) {
        this.buildingName = buildingName;
    }
    public String getBuildingName() {
        return buildingName;
    }
    @Override
    public void interact() {
        System.out.println("Interacting with " + buildingName + ".");
    }
}
