import java.util.ArrayList;
import java.util.List;

public class Map {
    private int width;
    private int height;
    private List<Point> points;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        this.points = new ArrayList<>();
    }

    public void addPoint(int x, int y, Tile tile) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            Point point = new Point(x, y, tile);
            points.add(point);
        } else {
            System.out.println("Point out of bounds: (" + x + ", " + y + ")");
        }
    }

    public void changePoint(int x, int y, Tile tile) {
        for (Point point : points) {
            if (point.getX() == x && point.getY() == y) {
                point.setTileInfo(tile.getTileSymbol());
                return;
            }
        }
        System.out.println("Point not found: (" + x + ", " + y + ")");
    }

    public List<Point> getPoints() {
        return points;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setEarlyMap() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                addPoint(x, y, new Tile("Soil", 'S', true));
            }
        }
    }





    public void printMap() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean found = false;
                for (Point point : points) {
                    if (point.getX() == x && point.getY() == y) {
                        System.out.print(point.getTile() + " ");
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.print(". "); // Empty space
                }
            }
            System.out.println();
        }
    }
    
}
