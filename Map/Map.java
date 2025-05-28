package Map;

import Furniture.Bed;
import Furniture.Stove;
import Furniture.TV;
import Items.Seeds;
import java.awt.Graphics2D; // Pastikan import ini dan lainnya sesuai dengan yang Anda gunakan
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import main.GamePanel;

public class Map {
    GamePanel gp;

    // Tetap menggunakan tileImage sesuai kode yang Anda berikan,
    // pastikan Soil.java juga menggunakan gp.map.tileImage
    public Tile[] tileImage;
    public Tile[][] currentMapTiles; // Tiles untuk peta yang sedang aktif

    private static class MapState {
        Tile[][] tiles;
        int worldCol;
        int worldRow;

        MapState(Tile[][] tiles, int worldCol, int worldRow) {
            this.tiles = tiles;
            this.worldCol = worldCol;
            this.worldRow = worldRow;
        }
    }
    private HashMap<Integer, MapState> loadedMapStates;

    public int currentMapWorldCol;
    public int currentMapWorldRow;
    public int currentMapID = -1; // Inisialisasi agar load pertama selalu fresh

    public String[] mapFilePaths = {
            "/Map/maps/farm_map.txt",
            "/Map/maps/forest_map.txt",
            "/Map/maps/mountain_lake_map.txt",
            "/Map/maps/house_map.txt",
            "/Map/maps/npc_map.txt",
            "/Map/maps/mthouse_map.txt",
            "/Map/maps/chouse_map.txt",
    };

    public Map(GamePanel gp) {
        this.gp = gp;
        this.tileImage = new Tile[300]; // Sesuaikan ukuran jika perlu
        this.loadedMapStates = new HashMap<>();
        getTileImagePrototypes();
        loadMapByID(0); // Memuat peta default
    }

    public void getTileImagePrototypes() {
        try {
            // GRASS
            tileImage[0] = new Tile("grass", true);
            tileImage[0].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grass.png"));
            tileImage[1] = new Tile("grass_kiri_atas", true);
            tileImage[1].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskiriatas.png"));
            tileImage[2] = new Tile("grass_atas", true);
            tileImage[2].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grassatas.png"));
            tileImage[3] = new Tile("grass_kiri", true);
            tileImage[3].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskiri.png"));
            tileImage[4] = new Tile("grass_kiri_bawah", true);
            tileImage[4].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskiribawah.png"));
            tileImage[5] = new Tile("grass_bawah", true);
            tileImage[5].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grassbawah.png"));
            tileImage[6] = new Tile("grass_kanan", true);
            tileImage[6].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskanan.png"));
            tileImage[7] = new Tile("grass_kanan_atas", true);
            tileImage[7].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskananatas.png"));
            tileImage[8] = new Tile("grass_kanan_bawah", true);
            tileImage[8].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskananbawah.png"));
            tileImage[11] = new Tile("grass_atas_air", true);
            tileImage[11].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grassatasair.png"));
            tileImage[46] = new Tile("grass_bawah_air", true);
            tileImage[46].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grassbawahair.png"));
            tileImage[47] = new Tile("grass_kiri_air", true);
            tileImage[47].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskiriair.png"));
            tileImage[48] = new Tile("grass_kanan_air", true);
            tileImage[48].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskananair.png"));

            // HOUSE & FLOOR
            tileImage[49] = new Tile("floor", true);
            tileImage[49].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/floor.png"));
            tileImage[50] = new Tile("wallkiriatas", false);
            tileImage[50].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/wallkiriatas.png"));
            tileImage[51] = new Tile("wallatas", false);
            tileImage[51].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/wallatas.png"));
            tileImage[52] = new Tile("wallkananatas", false);
            tileImage[52].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/wallkananatas.png"));
            tileImage[53] = new Tile("wall", false);
            tileImage[53].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/wall.png"));
            tileImage[54] = new Tile("opendoor", true);
            tileImage[54].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/opendoor.png"));
            tileImage[55] = new Tile("wallkiribawah", false);
            tileImage[55].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/wallkiribawah.png"));
            tileImage[56] = new Tile("wallkananbawah", false);
            tileImage[56].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/wallkananbawah.png"));
            tileImage[57] = new Tile("door", true);
            tileImage[57].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/door.png"));
            tileImage[58] = new Tile("window", false);
            tileImage[58].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/window.png"));
            tileImage[59] = new Tile("pertigaan", false);
            tileImage[59].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/pertigaan.png"));
            tileImage[60] = new Tile("mentokbawah", false);
            tileImage[60].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/mentokbawah.png"));

            // Carpet
            tileImage[61] = new Tile("karpetpojokkiri", true);
            tileImage[61].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetpojokkiri.png"));
            tileImage[62] = new Tile("karpetkiri", true);
            tileImage[62].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetkiri.png"));
            tileImage[63] = new Tile("karpetatas", true);
            tileImage[63].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetatas.png"));
            tileImage[64] = new Tile("karpetpojokkanan", true);
            tileImage[64].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetpojokkanan.png"));
            tileImage[65] = new Tile("karpetkanan", true);
            tileImage[65].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetkanan.png"));
            tileImage[66] = new Tile("belokkanan", true);
            tileImage[66].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/belokkanan.png"));
            tileImage[67] = new Tile("karpetmentok", true);
            tileImage[67].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetmentok.png"));
            tileImage[68] = new Tile("karpetbawah", true);
            tileImage[68].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetbawah.png"));
            tileImage[69] = new Tile("karpetpojokiribawah", true);
            tileImage[69].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetpojokiribawah.png"));
            tileImage[70] = new Tile("belokhehe", true);
            tileImage[70].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/belokhehe.png"));
            tileImage[71] = new Tile("karpet", true);
            tileImage[71].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpet.png"));

            // Furnitures
            tileImage[72] = new Bed("Bed", false, "king_ul");
            tileImage[72].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed1.png"));
            tileImage[73] = new Bed("Bed", false, "king_um");
            tileImage[73].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed2.png"));
            // ... (Lanjutkan untuk semua furniture prototypes)
            tileImage[74] = new Bed("Bed", false, "king_ur");
            tileImage[74].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed3.png"));
            tileImage[75] = new Bed("Bed", false, "king_ml");
            tileImage[75].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed4.png"));
            tileImage[76] = new Bed("Bed", true, "king_mm");
            tileImage[76].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed5.png"));
            tileImage[77] = new Bed("Bed", false, "king_mr");
            tileImage[77].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed6.png"));
            tileImage[78] = new Bed("Bed", false, "king_bl");
            tileImage[78].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed7.png"));
            tileImage[79] = new Bed("Bed", false, "king_bm");
            tileImage[79].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed8.png"));
            tileImage[80] = new Bed("Bed", false, "king_br");
            tileImage[80].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed9.png"));
            tileImage[81] = new Stove("stove");
            tileImage[81].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/stove.png"));
            tileImage[82] = new TV("tv", false);
            tileImage[82].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/TV.png"));

            // WATER
            tileImage[9] = new Tile("Water", false);
            tileImage[9].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/water.png"));

            // SOIL
            tileImage[10] = new Soil("soil", true, "/Map/tiles/dirt.png");

            // DOOR
            tileImage[12] = new Tile("Door Visual Placeholder", true); // Walkable agar bisa transisi
            InputStream doorStream = getClass().getResourceAsStream("/Map/tiles/house/door.png");
             if (doorStream != null) {
                tileImage[12].Image = ImageIO.read(doorStream);
            } else {
                System.err.println("Warning: Door image /Map/tiles/house/door.png not found for tile 12.");
                tileImage[12].Image = createPlaceholderImageOnError(gp.tileSize);
            }


            // SEEDS
            tileImage[13] = new Tile("Planted Parsnip Visual", true);
            tileImage[13].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Parsnip Seeds.png"));
            // ... (Lanjutkan untuk semua seed visual prototypes)
            tileImage[14] = new Tile("Planted Cauliflower Visual", true);
            tileImage[14].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Cauliflower Seeds.png"));
            tileImage[15] = new Tile("Planted Potato Visual", true);
            tileImage[15].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Potato Seeds.png"));
            tileImage[16] = new Tile("Planted Wheat Visual", true);
            tileImage[16].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Wheat Seeds.png"));
            tileImage[17] = new Tile("Planted Blueberry Visual", true);
            tileImage[17].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Blueberry Seeds.png"));
            tileImage[18] = new Tile("Planted Tomato Visual", true);
            tileImage[18].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Tomato Seeds.png"));
            tileImage[19] = new Tile("Planted Hot Pepper Visual", true);
            tileImage[19].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Hot Pepper Seeds.png"));
            tileImage[20] = new Tile("Planted Melon Visual", true);
            tileImage[20].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Melon Seeds.png"));
            tileImage[21] = new Tile("Planted Cranberry Visual", true);
            tileImage[21].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Cranberry Seeds.png"));
            tileImage[22] = new Tile("Planted Pumpkin Visual", true);
            tileImage[22].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Pumpkin Seeds.png"));
            tileImage[23] = new Tile("Planted Grape Visual", true);
            tileImage[23].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Grape Seeds.png"));

            tileImage[24] = new Tile("Wet Parsnip Visual", true);
            tileImage[24].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Parsnip Seeds.png"));
            tileImage[25] = new Tile("Wet Cauliflower Visual", true);
            tileImage[25].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Cauliflower Seeds.png"));
            tileImage[26] = new Tile("Wet Potato Visual", true);
            tileImage[26].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Potato Seeds.png"));
            tileImage[27] = new Tile("Wet Wheat Visual", true);
            tileImage[27].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Wheat Seeds.png"));
            tileImage[28] = new Tile("Wet Blueberry Visual", true);
            tileImage[28].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Blueberry Seeds.png"));
            tileImage[29] = new Tile("Wet Tomato Visual", true);
            tileImage[29].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Tomato Seeds.png"));
            tileImage[30] = new Tile("Wet Hot Pepper Visual", true);
            tileImage[30].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Hot Pepper Seeds.png"));
            tileImage[31] = new Tile("Wet Melon Visual", true);
            tileImage[31].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Melon Seeds.png"));
            tileImage[32] = new Tile("Wet Cranberry Visual", true);
            tileImage[32].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Cranberry Seeds.png"));
            tileImage[33] = new Tile("Wet Pumpkin Visual", true);
            tileImage[33].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Pumpkin Seeds.png"));
            tileImage[34] = new Tile("Wet Grape Visual", true);
            tileImage[34].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Grape Seeds.png"));

            tileImage[35] = new Tile("Harvestable Parsnip Visual", true);
            tileImage[35].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Parsnip.png"));
            tileImage[36] = new Tile("Harvestable Cauliflower Visual", true);
            tileImage[36].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Cauliflower.png"));
            tileImage[37] = new Tile("Harvestable Potato Visual", true);
            tileImage[37].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Potato.png"));
            tileImage[38] = new Tile("Harvestable Wheat Visual", true);
            tileImage[38].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Wheat.png"));
            tileImage[39] = new Tile("Harvestable Blueberry Visual", true);
            tileImage[39].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Blueberry.png"));
            tileImage[40] = new Tile("Harvestable Tomato Visual", true);
            tileImage[40].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Tomato.png"));
            tileImage[41] = new Tile("Harvestable Hot Pepper Visual", true);
            tileImage[41].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Hot Pepper.png"));
            tileImage[42] = new Tile("Harvestable Melon Visual", true);
            tileImage[42].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Melon.png"));
            tileImage[43] = new Tile("Harvestable Cranberry Visual", true);
            tileImage[43].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Cranberry.png"));
            tileImage[44] = new Tile("Harvestable Pumpkin Visual", true);
            tileImage[44].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Pumpkin.png"));
            tileImage[45] = new Tile("Harvestable Grape Visual", true);
            tileImage[45].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Grape.png"));


            // Mayor Tadi House Tiles Exterior
            tileImage[101] = new Tile("Top Right Corner Roof", false);
            tileImage[101].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/righttopcornerroof.png"));
            tileImage[102] = new Tile("Bottom Left Right Roof", false);
            tileImage[102].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/rightbottomcornerroof.png"));
            tileImage[103] = new Tile("Bottom Roof", false);
            tileImage[103].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/bottomroof.png"));
            tileImage[104] = new Tile("Top Left Corner Roof", false);
            tileImage[104].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/lefttopcornerroof.png"));
            tileImage[105] = new Tile("Bottom Left Corner Roof", false);
            tileImage[105].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/leftbottomcornerroof.png"));
            tileImage[106] = new Tile("MTHouse Wall Top", false);
            tileImage[106].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/walltop.png"));
            tileImage[107] = new Tile("MTHouse Left Corner", false);
            tileImage[107].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/leftcorner.png"));
            tileImage[108] = new Tile("MTHouse Right Corner", false);
            tileImage[108].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/rightcorner.png"));
            tileImage[109] = new Tile("MTHouse Left Window Bottom", false);
            tileImage[109].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/leftwindowbottom.png"));
            tileImage[110] = new Tile("MTHouse Right Window Bottom", false);
            tileImage[110].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/rightwindowbottom.png"));
            tileImage[111] = new Tile("MTHouse Left Window Top", false);
            tileImage[111].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/lefttopwindow.png"));
            tileImage[112] = new Tile("MTHouse Right Window Top", false);
            tileImage[112].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/righttopwindow.png"));
            tileImage[113] = new Tile("Left Wall Top", false);
            tileImage[113].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/leftwalltop.png"));
            tileImage[114] = new Tile("Right Wall Top", false);
            tileImage[114].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/rightwalltop.png"));

        } catch (IOException e) {
            System.err.println("Error loading tile prototype images: " + e.getMessage());
            e.printStackTrace();
            for(int i=0; i<tileImage.length; i++) { // Menggunakan tileImage
                if(tileImage[i] == null) {
                    tileImage[i] = new Tile("Error Proto "+i, false);
                    tileImage[i].Image = createPlaceholderImageOnError(gp.tileSize);
                }
            }
        } catch (Exception e) {
            System.err.println("Unexpected error in getTileImagePrototypes: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private BufferedImage createPlaceholderImageOnError(int tileSize) {
        BufferedImage placeholder = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = placeholder.createGraphics();
        g.setColor(java.awt.Color.PINK);
        g.fillRect(0, 0, tileSize, tileSize);
        g.setColor(java.awt.Color.BLACK);
        g.drawString("ERR", tileSize / 2 - 10, tileSize / 2 + 5);
        g.dispose();
        return placeholder;
    }

    // classExists tidak lagi digunakan dalam konteks ini
    // private boolean classExists(String className) { ... }

    private Tile createTileInstance(int prototypeID) {
        if (prototypeID < 0 || prototypeID >= tileImage.length || tileImage[prototypeID] == null) {
            System.err.println("Warning: Invalid prototypeID " + prototypeID + " in createTileInstance. Using default tile 0.");
            prototypeID = 0;
            if (tileImage[prototypeID] == null) {
                Tile errorTile = new Tile("Error Tile", false);
                errorTile.Image = createPlaceholderImageOnError(gp.tileSize);
                return errorTile;
            }
        }

        Tile prototype = tileImage[prototypeID]; // Menggunakan tileImage
        Tile newInstance;

        if (prototype instanceof Soil) {
            newInstance = new Soil((Soil) prototype);
        } else if (prototype instanceof Bed) { // Pastikan Bed adalah subclass Tile dan punya copy constructor
            newInstance = new Bed((Bed) prototype);
        }
        else {
            // Pastikan Tile memiliki copy constructor: public Tile(Tile other)
            newInstance = new Tile(prototype);
        }
        return newInstance;
    }

    // Metode ini akan memuat peta dari file dan menyimpannya ke cache jika belum ada
    private void loadFreshMapFromFileAndCache(String mapFilePath, int mapIdToLoad) {
        try {
            InputStream is = getClass().getResourceAsStream(mapFilePath);
            if (is == null) {
                System.err.println("FATAL ERROR: File " + mapFilePath + " tidak ditemukan!");
                createEmptyMapAndCache(gp.maxScreenCol, gp.maxScreenRow, mapIdToLoad);
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            ArrayList<String> lines = new ArrayList<>();
            String currentLine;
            while ((currentLine = br.readLine()) != null) { lines.add(currentLine.trim()); }
            br.close();

            if (lines.isEmpty()) {
                System.err.println("Map file is empty: " + mapFilePath);
                createEmptyMapAndCache(gp.maxScreenCol, gp.maxScreenRow, mapIdToLoad);
                return;
            }

            int rows = lines.size();
            int cols = 0;
            if (rows > 0 && !lines.get(0).isEmpty()) {
                cols = lines.get(0).split("\\s+").length;
            } else {
                System.err.println("Map file has empty lines or zero rows: " + mapFilePath);
                createEmptyMapAndCache(gp.maxScreenCol, gp.maxScreenRow, mapIdToLoad);
                return;
            }

            Tile[][] newMapTileInstances = new Tile[cols][rows];

            for (int row = 0; row < rows; row++) {
                String lineData = lines.get(row);
                String[] numbers = lineData.split("\\s+");
                if (numbers.length < cols) {
                     System.err.println("Warning: Map file " + mapFilePath + " row " + (row+1) + " has fewer columns (" + numbers.length +") than expected (" + cols + "). Padding with default tiles.");
                }
                for (int col = 0; col < cols; col++) {
                    if (col < numbers.length && !numbers[col].isEmpty()) {
                        try {
                            int tilePrototypeIDFromFile = Integer.parseInt(numbers[col]);
                            newMapTileInstances[col][row] = createTileInstance(tilePrototypeIDFromFile);
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing number in " + mapFilePath + " at row " + (row + 1) + ", col " + (col + 1) + ". Val: '" + numbers[col] + "'. Using default.");
                            newMapTileInstances[col][row] = createTileInstance(0);
                        }
                    } else {
                        newMapTileInstances[col][row] = createTileInstance(0);
                    }
                }
            }

            // Set peta saat ini
            this.currentMapTiles = newMapTileInstances;
            this.currentMapWorldCol = cols;
            this.currentMapWorldRow = rows;
            this.currentMapID = mapIdToLoad;

            // Simpan ke cache
            loadedMapStates.put(mapIdToLoad, new MapState(newMapTileInstances, cols, rows));
            System.out.println("Map loaded from file and cached: " + mapFilePath + " (ID: " + mapIdToLoad + ") Dimensions: " + cols + "x" + rows);

        } catch (IOException e) {
            System.err.println("IOException loading map " + mapFilePath + ": " + e.getMessage());
            createEmptyMapAndCache(gp.maxScreenCol, gp.maxScreenRow, mapIdToLoad);
        } catch (Exception e) {
            System.err.println("Unexpected error loading map " + mapFilePath + ": " + e.getMessage());
            e.printStackTrace();
            createEmptyMapAndCache(gp.maxScreenCol, gp.maxScreenRow, mapIdToLoad);
        }
    }

    public boolean loadMapByID(int mapID) {
        if (mapID < 0 || mapID >= mapFilePaths.length || mapFilePaths[mapID] == null) {
            System.err.println("Error: Invalid mapID (" + mapID + ") or map file path not configured.");
            if (currentMapID == -1 && mapFilePaths.length > 0 && mapFilePaths[0] != null) { // Hanya jika belum ada peta yang dimuat
                 return loadMapByID(0); // Rekursif panggil dengan ID 0
            } else if (currentMapID == -1) { // Jika benar-benar tidak ada peta default
                createEmptyMapAndCache(gp.maxScreenCol, gp.maxScreenRow, 0); // Buat peta kosong sebagai ID 0
            }
            return false;
        }

        if (loadedMapStates.containsKey(mapID)) {
            MapState cachedState = loadedMapStates.get(mapID);
            this.currentMapTiles = cachedState.tiles; // Muat instance dari cache
            this.currentMapWorldCol = cachedState.worldCol;
            this.currentMapWorldRow = cachedState.worldRow;
            this.currentMapID = mapID;
            System.out.println("Map loaded from cache. ID: " + mapID + ". World size: " + currentMapWorldCol + "x" + currentMapWorldRow);
            return true;
        } else {
            // Peta belum ada di cache, muat dari file dan simpan ke cache
            loadFreshMapFromFileAndCache(mapFilePaths[mapID], mapID);
            // currentMapID, currentMapTiles, dll sudah diatur di dalam loadFreshMapFromFileAndCache
            return true;
        }
    }

    // Membuat peta kosong dan menyimpannya ke cache
    private void createEmptyMapAndCache(int cols, int rows, int mapIdForCache) {
        Tile[][] emptyTiles = new Tile[cols][rows];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                emptyTiles[c][r] = createTileInstance(0);
            }
        }
        // Set peta saat ini ke peta kosong yang baru dibuat
        this.currentMapTiles = emptyTiles;
        this.currentMapWorldCol = cols;
        this.currentMapWorldRow = rows;
        this.currentMapID = mapIdForCache;

        loadedMapStates.put(mapIdForCache, new MapState(emptyTiles, cols, rows));
        System.out.println("Created/Reverted to empty map (ID: " + mapIdForCache + ") (" + cols + "x" + rows + ") and cached.");
    }
    
    // Menghapus loadMapByPath lama karena logikanya sudah di loadFreshMapFromFileAndCache
    // private void loadMapByPath(String mapFilePath) { ... }

    // Menghapus createEmptyMap lama karena logikanya sudah di createEmptyMapAndCache
    // private void createEmptyMap(int cols, int rows) { ... }


    public void draw(Graphics2D g2) {
        if (currentMapTiles == null) return; // Menggunakan currentMapTiles

        for (int worldRow = 0; worldRow < currentMapWorldRow; worldRow++) {
            for (int worldCol = 0; worldCol < currentMapWorldCol; worldCol++) {
                Tile currentTileToDraw = currentMapTiles[worldCol][worldRow]; // Menggunakan currentMapTiles
                if (currentTileToDraw != null && currentTileToDraw.Image != null) {
                    int worldX = worldCol * gp.tileSize;
                    int worldY = worldRow * gp.tileSize;
                    int screenX = worldX - gp.player.x + gp.player.screenX;
                    int screenY = worldY - gp.player.y + gp.player.screenY;

                    if (worldX + gp.tileSize > gp.player.x - gp.player.screenX &&
                        worldX - gp.tileSize < gp.player.x + gp.player.screenX &&
                        worldY + gp.tileSize > gp.player.y - gp.player.screenY &&
                        worldY - gp.tileSize < gp.player.y + gp.player.screenY) {
                        g2.drawImage(currentTileToDraw.Image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    }
                }
            }
        }
    }

    public void updateTiles() {
        if (currentMapTiles == null) return; // Menggunakan currentMapTiles
        for (int r = 0; r < currentMapWorldRow; r++) {
            for (int c = 0; c < currentMapWorldCol; c++) {
                Tile tileToUpdate = currentMapTiles[c][r]; // Menggunakan currentMapTiles
                if (tileToUpdate != null) { // Pastikan tile tidak null
                    tileToUpdate.update(gp); // Memanggil update(gp)
                    if (tileToUpdate instanceof Soil) {
                        Soil soilTile = (Soil) tileToUpdate;
                        if (soilTile.getSeedPlanted() != null) { // Update gambar jika ada benih ATAU tanah basah
                            soilTile.updateImageBasedOnState(gp);
                        }
                    }
                }
            }
        }
    }
    
    public void advanceDay() {
        System.out.println("Map: Advancing to a new day for all cached maps.");
        for (MapState mapState : loadedMapStates.values()) {
            if (mapState != null && mapState.tiles != null) {
                for (int r = 0; r < mapState.worldRow; r++) {
                    for (int c = 0; c < mapState.worldCol; c++) {
                        Tile tile = mapState.tiles[c][r];
                        if (tile instanceof Soil) {
                            Soil soilTile = (Soil) tile;
                        }
                    }
                }
            }
        }
        // Panggil updateTiles untuk me-refresh gambar peta yang sedang aktif jika ada perubahan
        if (currentMapID != -1) { // Hanya jika ada peta yang aktif
             System.out.println("Refreshing visuals for current map after advancing day.");
            updateTiles();
        }
    }


    public Tile getTile(int worldX, int worldY) {
        int col = worldX / gp.tileSize;
        int row = worldY / gp.tileSize;

        if (currentMapTiles != null && col >= 0 && col < currentMapWorldCol && row >= 0 && row < currentMapWorldRow) {
            return currentMapTiles[col][row]; // Menggunakan currentMapTiles
        }
        return null;
    }

    public void setTileType(int worldX, int worldY, int newTilePrototypeID) {
        int col = worldX / gp.tileSize;
        int row = worldY / gp.tileSize;

        if (currentMapTiles != null && col >= 0 && col < currentMapWorldCol && row >= 0 && row < currentMapWorldRow) {
            currentMapTiles[col][row] = createTileInstance(newTilePrototypeID); // Menggunakan currentMapTiles
            // Perubahan pada currentMapTiles akan langsung tercermin di MapState yang ada di cache
            // karena currentMapTiles adalah referensi ke array tiles di dalam MapState.
        }
    }

    public void plantSeedAtTile(int worldX, int worldY, Seeds seedToPlant) {
        Tile targetTile = getTile(worldX, worldY); // getTile() sudah menggunakan currentMapTiles
        if (targetTile instanceof Soil) {
            Soil soilTile = (Soil) targetTile;
            if (soilTile.canPlant()) {
                soilTile.plantSeed(seedToPlant, gp);
            } else {
                String plantedInfo = "is already planted";
                if (soilTile.getSeedPlanted() != null) {
                    plantedInfo += " with " + soilTile.getSeedPlanted().getName();
                }
                System.out.println("Cannot plant at ("+ (worldX/gp.tileSize) + "," + (worldY/gp.tileSize) +"): Tile " + plantedInfo);
            }
        } else {
            String tileType = (targetTile != null) ? targetTile.getTileName() : "Out of bounds or null";
            System.out.println("Cannot plant at ("+ (worldX/gp.tileSize) + "," + (worldY/gp.tileSize) +"): Tile is not Soil. It is " + tileType);
        }
    }

    public void harvestSeedAtTile(int worldX, int worldY) {
        Tile targetTile = getTile(worldX, worldY); // getTile() sudah menggunakan currentMapTiles
        if (targetTile instanceof Soil) {
            Soil soilTile = (Soil) targetTile;
            // Logika harvest ada di Soil.harvest()
            soilTile.harvest(gp, gp.player);
        } else {
            System.out.println("Cannot harvest at ("+ (worldX/gp.tileSize) + "," + (worldY/gp.tileSize) +"): Not a soil tile or nothing to harvest.");
        }
    }
}