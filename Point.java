public class Point {
    private int x;
    private int y;
    private String tileInfo;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

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

    public String getTileInfo() {
        return tileInfo;
    }

    public void setTileInfo(String tileInfo) {
        this.tileInfo = tileInfo;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    
}
