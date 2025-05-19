package BuildingStuff;

public class Pond extends Building{
    public Pond(String tilename, char tileSymbol, boolean isWalkable, String buildingName, int width, int height, char symbol, int longitude, int latitude){
        super(tilename, tileSymbol, isWalkable, buildingName, width, height, symbol, longitude, latitude);
    }
    public void fishing(){
        System.out.println("Fishing.....");
        
    }
}