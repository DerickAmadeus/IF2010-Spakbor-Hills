package Map;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.*;

import Items.Fish;
import main.GamePanel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
        }
    }

}