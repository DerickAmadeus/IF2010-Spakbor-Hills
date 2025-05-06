package BuildingStuff;

public class Pond extends Building{
    public OutsideHouse(String tilename, char tileSymbol, boolean isWalkable, String buildingName, int width, int height, char symbol, int longitude, int latitude){
        super(tilename, tileSymbol, isWalkable, buildingName, width, height, symbol, longitude, latitude);
    }
    public void getInside(){
        System.out.println("Entering the inside of the house!");
    }
}