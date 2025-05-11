package Map;

import java.awt.image.BufferedImage;
public class Tile {
    private String tilename;
    private char tileSymbol;
    private boolean isWalkable;
    public BufferedImage Image; // Assuming you have an Image class for tile images

    public Tile(String tilename, char tileSymbol, boolean isWalkable) {
        this.tilename = tilename;
        this.tileSymbol = tileSymbol;
        this.isWalkable = isWalkable;
    }
    public String getTileName() {
        return tilename;
    }
    public void setTilename(String tilename) {
        this.tilename = tilename;
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
