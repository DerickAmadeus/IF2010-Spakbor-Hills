package Map;

public class Point {
    private int x;
    private int y;

    private Tile tileinfo;

    // Constructor
    public Point(int x, int y, Tile tileinfo) {
        this.x = x;
        this.y = y;
        this.tileinfo = tileinfo;
    }

    // Getters and Setters
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public Tile getTileinfo() {
        return tileinfo;
    }
    public void setTileinfo(Tile tileinfo) {
        this.tileinfo = tileinfo;
    }

    public void tiling(Soil soil) {
        if (tileinfo.getTilename().equals("Soil")){
            System.out.println("Tanah ini udah dibajak");
        } else {
            this.tileinfo = soil;
            System.out.println("Tanah ini udah dibajak");
        }
    }

    public void building(Building building) {
        if (tileinfo.getTilename().equals("Building")){
            System.out.println("Bangunan ini udah dibangun");
        } else {
            this.tileinfo = building;
            System.out.println("Bangunan ini udah dibangun");
        }
    }
    // Constructor

    
}
