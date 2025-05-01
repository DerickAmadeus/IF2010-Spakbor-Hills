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

    public void addPoint(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            points.add(new Point(x, y));
        } else {
            throw new IllegalArgumentException("Point is out of bounds");
        }
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
    
}
