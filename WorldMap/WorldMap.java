package WorldMap;
import java.util.ArrayList;



public class WorldMap {
    private ArrayList<WorldBuilding> place;


    public WorldMap() {
        place = new ArrayList<>();
    }
    public void addPlace(WorldBuilding worldBuilding) {
        place.add(worldBuilding);
    }
    public void removePlace(WorldBuilding WorldBuilding) {
        place.remove(WorldBuilding);
    }

    public void showPlaces() {
        System.out.println("Available places on the map:");
        for (WorldBuilding worldBuilding : place) {
            System.out.println("- " + worldBuilding.getBuildingName());
        }
    }

    public void interactWithPlace(String buildingName) {
        for (WorldBuilding worldBuilding : place) {
            if (worldBuilding.getBuildingName().equalsIgnoreCase(buildingName)) {
                worldBuilding.interact();
                return;
            }
        }
        System.out.println("Place not found on the map.");
    }
}
