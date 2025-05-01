public class Tile {
    private String tileName;
    private char tileSymbol;
    private boolean isWalkable;


    public Tile(String tileName, char tileSymbol, boolean isWalkable) {
        this.tileName = tileName;
        this.tileSymbol = tileSymbol;
        this.isWalkable = isWalkable;
    }
    public String getTileName() {
        return tileName;
    }
    public void setTileName(String tileName) {
        this.tileName = tileName;
    }
    public char getTileSymbol() {
        return tileSymbol;
    }
    public void setTileSymbol(char tileSymbol) {
        this.tileSymbol = tileSymbol;
    }
    public boolean isWalkable() {
        return isWalkable;
    }
    public void setWalkable(boolean isWalkable) {
        this.isWalkable = isWalkable;
    }
    
}
