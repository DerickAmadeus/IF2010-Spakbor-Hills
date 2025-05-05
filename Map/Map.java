package Map;

public class Map {
    private int width;
    private int height;
    private Tile[][] tiles;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new Tile[width][height];
    }

    public void setTile(int x, int y, Tile tile) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            tiles[x][y] = tile;
        } else {
            System.out.println("Invalid tile coordinates.");
        }
    }

    public Tile getTile(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return tiles[x][y];
        } else {
            System.out.println("Invalid tile coordinates.");
            return null;
        }
    }

    public void displayMap() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (tiles[x][y] != null) {
                    System.out.print(tiles[x][y].getTileSymbol() + " ");
                } else {
                    System.out.print(". "); // Empty space
                }
            }
            System.out.println();
        }
    }
    
}
