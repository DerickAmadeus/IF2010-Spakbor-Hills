public class Point {
    private int x;
    private int y;
    private Tile tile;

    public Point(int x, int y, Tile tile) {
        this.x = x;
        this.y = y;
        this.tile = tile;
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

    public char getTile() {
        return tile.getTileSymbol();
    }

    public void setTileInfo(char tileSymbol) {
        this.tile.setTileSymbol(tileSymbol);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    
}
