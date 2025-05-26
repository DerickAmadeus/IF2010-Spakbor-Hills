package Map;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

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
    public Fish[] loadInitialFish() {
        ArrayList<String> any = new ArrayList<>(Arrays.asList("Spring", "Summer", "Fall", "Winter"));
        ArrayList<String> spring = new ArrayList<>(Arrays.asList("Spring"));
        ArrayList<String> summer = new ArrayList<>(Arrays.asList("Summer"));
        ArrayList<String> fall = new ArrayList<>(Arrays.asList("Fall"));
        ArrayList<String> winter = new ArrayList<>(Arrays.asList("Winter"));
        ArrayList<String> sturgeonSeason = new ArrayList<>(Arrays.asList("Summer", "Winter"));
        ArrayList<String> midnightCarpSeason = new ArrayList<>(Arrays.asList("Fall", "Winter"));
        ArrayList<String> flounderSeason = new ArrayList<>(Arrays.asList("Spring", "Summer"));
        ArrayList<String> superCucumberSeason = new ArrayList<>(Arrays.asList("Summer", "Fall", "Winter"));
        ArrayList<String> catfishSeason = new ArrayList<>(Arrays.asList("Spring", "Summer", "Fall"));

        return new Fish[] {
            new Fish("Bullhead", "Ikan Bullhead, mudah ditemukan.", 50, 50, any, "Any", "Mountain Lake", "Common"),
            new Fish("Carp", "Ini Carp.", 50, 50, any, "Any", "Pond", "Common"),
            new Fish("Chub", "Ikan Chub, cukup umum.", 50, 50, any, "Any", "Forest River", "Common"),
            new Fish("Largemouth Bass", "Ikan besar dari danau pegunungan.", 100, 100, any, "Any", "Mountain Lake", "Regular"),
            new Fish("Rainbow Trout", "Ikan berwarna pelangi yang muncul saat cuaca cerah.", 120, 120, summer, "Sunny", "Forest River", "Regular"),
            new Fish("Sturgeon", "Ikan langka dari danau pegunungan.", 200, 200, sturgeonSeason, "Any", "Mountain Lake", "Regular"),
            new Fish("Midnight Carp", "Ikan malam dari danau atau kolam.", 150, 150, midnightCarpSeason, "Any", "Mountain Lake", "Regular"),
            new Fish("Flounder", "Ikan pipih dari laut.", 90, 90, flounderSeason, "Any", "Ocean", "Regular"),
            new Fish("Halibut", "Ikan laut besar aktif pagi dan malam.", 110, 110, any, "Any", "Ocean", "Regular"),
            new Fish("Octopus", "Gurita laut yang aktif siang hari.", 180, 180, summer, "Any", "Ocean", "Regular"),
            new Fish("Pufferfish", "Ikan buntal beracun saat cuaca cerah.", 160, 160, summer, "Sunny", "Ocean", "Regular"),
            new Fish("Sardine", "Ikan kecil dari laut.", 40, 40, any, "Any", "Ocean", "Common"),
            new Fish("Super Cucumber", "Ikan misterius aktif malam hari.", 250, 250, superCucumberSeason, "Any", "Ocean", "Regular"),
            new Fish("Catfish", "Ikan lele liar saat hujan.", 130, 130, catfishSeason, "Rainy", "Forest River", "Regular"),
            new Fish("Salmon", "Ikan migrasi dari sungai.", 120, 120, fall, "Any", "Forest River", "Regular"),
            new Fish("Angler", "Ikan legendaris yang hanya muncul di musim gugur.", 1000, 1000, fall, "Any", "Pond", "Legendary"),
            new Fish("Crimsonfish", "Ikan legendaris dari laut tropis.", 1000, 1000, summer, "Any", "Ocean", "Legendary"),
            new Fish("Glacierfish", "Ikan legendaris dari sungai beku.", 1000, 1000, winter, "Any", "Forest River", "Legendary"),
            new Fish("Legend", "Ikan legendaris tertinggi di danau gunung saat hujan.", 1200, 1200, spring, "Rainy", "Mountain Lake", "Legendary")
        };
    }
    public void update(GamePanel gp) {
        if (this instanceof Soil) {
            Soil update = (Soil) this;
            update.update(gp);
        }
    }

}