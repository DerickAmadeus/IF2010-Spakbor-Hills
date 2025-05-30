package Map;

import Furniture.Stove;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class Tile {
    private String name;
    private boolean isWalkable;
    public BufferedImage Image; // Assuming you have an Image class for tile images

    public Tile(String tilename, boolean isWalkable) {
        this.name = tilename;
        this.isWalkable = isWalkable;
    }

    public Tile(Tile other) {
        this.name = other.name;
        this.isWalkable = other.isWalkable;
        this.Image = other.Image;
    }

    public void loadImage(String imagePath) {
        try {
            this.Image = ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTileName() {
        return name;
    }

    public boolean isWalkable() {
        return isWalkable;
    }
    
    public void update(GamePanel gp) {
        if (this instanceof Soil) {
            Soil update = (Soil) this;
            update.update(gp);
        } else if (this instanceof Stove) {
            Stove update = (Stove) this;
            update.update(gp);
        } else if (this instanceof ShippingBin) {
            ShippingBin update = (ShippingBin) this;
            update.update(gp);
        }
    }

}