package BuildingStuff;
import java.util.ArrayList;


public class InsideHouse {
    private Array<Furniture> furnishing;
    private int width;
    private int height;
    private Point grid;
    public InsideHouse(int width, int height) {
        this.width = width;
        this.height = height;
        this.furnishing = new ArrayList<>();
    }
    public void addFurniture(Furniture furniture, int x, int y) {
        furnishing.add(furniture);
    }
}