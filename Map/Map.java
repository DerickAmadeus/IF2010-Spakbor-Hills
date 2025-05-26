package Map;

import main.GamePanel;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage; // Untuk error tile & prototype image
import Items.Seeds;
import Furniture.Bed;
import Furniture.Stove;
import Furniture.TV;
import Items.Misc;

import java.awt.Graphics2D;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Map {
    GamePanel gp;

    public Tile[] tileImage; // Array untuk PROTOTYPE tile (diganti nama dari tileimage)
    public Tile[][] currentMapTiles;  // Array 2D untuk INSTANCE tile di peta AKTIF

    // Struktur data untuk menyimpan tile dan dimensi setiap peta yang telah dimuat
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
    private HashMap<Integer, MapState> loadedMapStates; // Cache untuk state peta

    public int currentMapWorldCol;
    public int currentMapWorldRow;

    public int currentMapID = 0;
    public String[] mapFilePaths = {
            "/Map/maps/farm_map.txt",
            "/Map/maps/forest_map.txt",
            "/Map/maps/mountain_lake_map.txt",
            "/Map/maps/house_map.txt"
            // ... tambahkan semua path file peta kamu di sini
    };

    public Map(GamePanel gp) {
        this.gp = gp;

        this.tileImage = new Tile[100];
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
            tileImage[57] = new Tile("door", true); // Asumsi 'door' adalah tile yang bisa ditransisikan, bukan actual door object
            tileImage[57].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/door.png"));
            tileImage[58] = new Tile("window", false);
            tileImage[58].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/window.png"));
            tileImage[59] = new Tile("pertigaan", false); // Asumsi ini adalah bagian dari dinding
            tileImage[59].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/pertigaan.png"));
            tileImage[60] = new Tile("mentokbawah", false); // Asumsi ini adalah bagian dari dinding
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
            tileImage[66] = new Tile("belokkanan", true); // Nama tile mungkin perlu disesuaikan
            tileImage[66].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/belokkanan.png"));
            tileImage[67] = new Tile("karpetmentok", true);
            tileImage[67].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetmentok.png"));
            tileImage[68] = new Tile("karpetbawah", true);
            tileImage[68].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetbawah.png"));
            tileImage[69] = new Tile("karpetpojokiribawah", true);
            tileImage[69].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetpojokiribawah.png"));
            tileImage[70] = new Tile("belokhehe", true); // Nama tile mungkin perlu disesuaikan
            tileImage[70].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/belokhehe.png"));
            tileImage[71] = new Tile("karpet", true);
            tileImage[71].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpet.png"));

            // Furnitures (sebagai Tile khusus)
            tileImage[72] = new Bed("Bed Part 1", false, "king_ul"); // Nama unik untuk tiap bagian
            tileImage[72].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed1.png"));
            tileImage[73] = new Bed("Bed Part 2", false, "king_um");
            tileImage[73].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed2.png"));
            tileImage[74] = new Bed("Bed Part 3", false, "king_ur");
            tileImage[74].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed3.png"));
            tileImage[75] = new Bed("Bed Part 4", false, "king_ml");
            tileImage[75].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed4.png"));
            tileImage[76] = new Bed("Bed Part 5 (Interact)", true, "king_mm"); // Bagian interaksi bisa walkable true jika interactionArea di atasnya
            tileImage[76].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed5.png"));
            tileImage[77] = new Bed("Bed Part 6", false, "king_mr");
            tileImage[77].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed6.png"));
            tileImage[78] = new Bed("Bed Part 7", false, "king_bl");
            tileImage[78].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed7.png"));
            tileImage[79] = new Bed("Bed Part 8", false, "king_bm");
            tileImage[79].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed8.png"));
            tileImage[80] = new Bed("Bed Part 9", false, "king_br");
            tileImage[80].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed9.png"));

            tileImage[81] = new Stove("stove"); // walkable default false di Furniture
            tileImage[81].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/stove.png"));
            tileImage[82] = new TV("tv", false); // TV biasanya non-walkable
            tileImage[82].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/TV.png"));

            // WATER
            tileImage[9] = new Tile("Water", false);
            tileImage[9].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/water.png"));

            // SOIL
            tileImage[10] = new Soil("soil", true, "/Map/tiles/dirt.png");

            // PLACEHOLDER untuk Building/Door jika belum ada kelasnya
            tileImage[12] = new Tile("Door Visual Placeholder", false); // Sebaiknya walkable true jika ini adalah tile di bawah pintu yang bisa dilewati
            tileImage[12].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/door.png")); // Gambar pintu

            // VISUAL PROTOTYPES untuk Benih yang Ditanam
            tileImage[13] = new Tile("Planted Parsnip Visual", true);
            tileImage[13].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Parsnip Seeds.png"));
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
            // ... (lanjutkan untuk semua wet seeds)
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


            // VISUAL PROTOTYPES untuk Tanaman Siap Panen
            tileImage[35] = new Tile("Harvestable Parsnip Visual", true);
            tileImage[35].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Parsnip.png"));
            tileImage[36] = new Tile("Harvestable Cauliflower Visual", true);
            tileImage[36].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Cauliflower.png"));
            // ... (lanjutkan untuk semua harvestable crops)
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


            // VISUAL PROTOTYPES untuk Benih yang Ditanam (sebagai Tile biasa)
            // Objek Soil di peta akan menggunakan .Image dari prototype Tile ini
            tileimage[13] = new Tile("Planted Parsnip Visual", true); // walkable true agar player bisa lewat
            tileimage[13].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Parsnip Seeds.png"));
            tileimage[14] = new Tile("Planted Cauliflower Visual", true);
            tileimage[14].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Cauliflower Seeds.png"));
            tileimage[15] = new Tile("Planted Potato Visual", true);
            tileimage[15].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Potato Seeds.png"));
            tileimage[16] = new Tile("Planted Wheat Visual", true);
            tileimage[16].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Wheat Seeds.png"));
            tileimage[17] = new Tile("Planted Blueberry Visual", true);
            tileimage[17].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Blueberry Seeds.png"));
            tileimage[18] = new Tile("Planted Tomato Visual", true);
            tileimage[18].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Tomato Seeds.png"));
            tileimage[19] = new Tile("Planted Hot Pepper Visual", true);
            tileimage[19].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Hot Pepper Seeds.png"));
            tileimage[20] = new Tile("Planted Melon Visual", true);
            tileimage[20].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Melon Seeds.png"));
            tileimage[21] = new Tile("Planted Cranberry Visual", true);
            tileimage[21].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Cranberry Seeds.png"));
            tileimage[22] = new Tile("Planted Pumpkin Visual", true);
            tileimage[22].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Pumpkin Seeds.png"));
            tileimage[23] = new Tile("Planted Grape Visual", true);
            tileimage[23].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Grape Seeds.png"));
            tileimage[24] = new Tile("Wet Parsnip Visual", true);
            tileimage[24].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Parsnip Seeds.png"));
            tileimage[25] = new Tile("Wet Cauliflower Visual", true);
            tileimage[25].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Cauliflower Seeds.png"));
            tileimage[26] = new Tile("Wet Potato Visual", true);
            tileimage[26].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Potato Seeds.png"));
            tileimage[27] = new Tile("Wet Wheat Visual", true);
            tileimage[27].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Wheat Seeds.png"));
            tileimage[28] = new Tile("Wet Blueberry Visual", true);
            tileimage[28].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Blueberry Seeds.png"));
            tileimage[29] = new Tile("Wet Tomato Visual", true);
            tileimage[29].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Tomato Seeds.png"));
            tileimage[30] = new Tile("Wet Hot Pepper Visual", true);
            tileimage[30].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Hot Pepper Seeds.png"));
            tileimage[31] = new Tile("Wet Melon Visual", true);
            tileimage[31].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Melon Seeds.png"));
            tileimage[32] = new Tile("Wet Cranberry Visual", true);
            tileimage[32].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Cranberry Seeds.png"));
            tileimage[33] = new Tile("Wet Pumpkin Visual", true);
            tileimage[33].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Pumpkin Seeds.png"));
            tileimage[34] = new Tile("Wet Grape Visual", true);
            tileimage[34].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Grape Seeds.png"));
            tileimage[35] = new Tile("Harvestable Parsnip Visual", true);
            tileimage[35].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Parsnip.png"));
            tileimage[36] = new Tile("Harvestable Cauliflower Visual", true);
            tileimage[36].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Cauliflower.png"));
            tileimage[37] = new Tile("Harvestable Potato Visual", true);
            tileimage[37].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Potato.png"));
            tileimage[38] = new Tile("Harvestable Wheat Visual", true);
            tileimage[38].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Wheat.png"));
            tileimage[39] = new Tile("Harvestable Blueberry Visual", true);
            tileimage[39].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Blueberry.png"));
            tileimage[40] = new Tile("Harvestable Tomato Visual", true);
            tileimage[40].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Tomato.png"));
            tileimage[41] = new Tile("Harvestable Hot Pepper Visual", true);
            tileimage[41].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Hot Pepper.png"));
            tileimage[42] = new Tile("Harvestable Melon Visual", true);
            tileimage[42].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Melon.png"));
            tileimage[43] = new Tile("Harvestable Cranberry Visual", true);
            tileimage[43].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Cranberry.png"));
            tileimage[44] = new Tile("Harvestable Pumpkin Visual", true);
            tileimage[44].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Pumpkin.png"));
            tileimage[45] = new Tile("Harvestable Grape Visual", true);
            tileimage[45].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Grape.png"));
        } catch (IOException e) { // Lebih spesifik menangkap IOException untuk ImageIO
            System.err.println("Error loading tile prototype images: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) { // Menangkap error lain
            System.err.println("Unexpected error in getTileImagePrototypes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper untuk cek kelas Building (opsional)
    private boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private Tile createTileInstance(int prototypeID) {

        if (prototypeID < 0 || prototypeID >= tileImage.length || tileImage[prototypeID] == null) {
            System.err.println("Warning: Invalid prototypeID " + prototypeID + " in createTileInstance. Using default tile 0.");
            prototypeID = 0;
            if (tileImage[prototypeID] == null) { // Fallback jika prototype 0 juga null
                Tile errorTile = new Tile("Error Tile", false);
                try {

                    errorTile.Image = new BufferedImage(gp.tileSize, gp.tileSize, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = errorTile.Image.createGraphics();
                    g.setColor(java.awt.Color.MAGENTA);
                    g.fillRect(0,0,gp.tileSize, gp.tileSize);
                    g.dispose();
                 } catch (Exception e) { /* abaikan jika gagal buat gambar error */ }
                 return errorTile;
            }
        }


        Tile prototype = tileImage[prototypeID];

        Tile newInstance;

        if (prototype instanceof Soil) {
            newInstance = new Soil((Soil) prototype); 
        // } else if (prototype instanceof Building) { 
        //     newInstance = new Building((Building) prototype); 
        } else {
            newInstance = new Tile(prototype); 
        }
        return newInstance;
    }

    private void loadMapByPath(String mapFilePath) {
        try {
            InputStream is = getClass().getResourceAsStream(mapFilePath);
            if (is == null) {
                System.err.println("FATAL ERROR: File " + mapFilePath + " tidak ditemukan!");
                createEmptyMap(gp.maxScreenCol, gp.maxScreenRow);
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            ArrayList<String> lines = new ArrayList<>();
            String currentLine;
            while ((currentLine = br.readLine()) != null) { lines.add(currentLine.trim()); }
            br.close();

            if (lines.isEmpty()) {
                System.err.println("Map file is empty: " + mapFilePath);
                createEmptyMap(gp.maxScreenCol, gp.maxScreenRow);
                return;
            }

            this.currentMapWorldRow = lines.size();
            if (this.currentMapWorldRow > 0 && !lines.get(0).isEmpty()) {
                this.currentMapWorldCol = lines.get(0).split("\\s+").length; // Split by one or more spaces
            } else {
                this.currentMapWorldCol = 0;
                System.err.println("Map file has empty lines or zero rows: " + mapFilePath);
                createEmptyMap(gp.maxScreenCol, gp.maxScreenRow);
                return;
            }

            this.tiles = new Tile[this.currentMapWorldCol][this.currentMapWorldRow]; // Inisialisasi dengan tipe Tile[][]

            for (int row = 0; row < this.currentMapWorldRow; row++) {
                String lineData = lines.get(row);
                String[] numbers = lineData.split("\\s+"); // Split by one or more spaces
                if (numbers.length < this.currentMapWorldCol) {
                    System.err.println("Warning: Map file " + mapFilePath + " row " + (row+1) + " has fewer columns (" + numbers.length +") than expected (" + this.currentMapWorldCol + "). Padding with default tiles.");
                }
                for (int col = 0; col < this.currentMapWorldCol; col++) {
                    if (col < numbers.length && !numbers[col].isEmpty()) {
                        try {
                            int tilePrototypeID = Integer.parseInt(numbers[col]);
                            tiles[col][row] = createTileInstance(tilePrototypeID);
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing number in " + mapFilePath + " at row " + (row + 1) + ", col " + (col + 1) + ". Val: '" + numbers[col] + "'. Using default.");
                            tiles[col][row] = createTileInstance(0);
                        }
                    } else {
                        tiles[col][row] = createTileInstance(0); // Default jika data kurang atau string kosong
                    }
                }
            }
            System.out.println("Map loaded: " + mapFilePath + " Dimensions: " + this.currentMapWorldCol + "x" + this.currentMapWorldRow);

        } catch (IOException e) {
            System.err.println("IOException loading map " + mapFilePath + ": " + e.getMessage());
            createEmptyMap(gp.maxScreenCol, gp.maxScreenRow);
        } catch (Exception e) {
            System.err.println("Unexpected error loading map " + mapFilePath + ": " + e.getMessage());
            e.printStackTrace();
            createEmptyMap(gp.maxScreenCol, gp.maxScreenRow);
        }
    }

    public void draw(Graphics2D g2) {
        if (tiles == null) return; // Jika peta belum dimuat

        int worldCol = 0;
        int worldRow = 0;

        while (worldRow < currentMapWorldRow) { // Loop per baris dulu
            worldCol = 0;
            while (worldCol < currentMapWorldCol) {
                Tile currentTile = tiles[worldCol][worldRow];

                if (currentTile != null && currentTile.Image != null) { // Gunakan .Image sesuai Tile.java Anda
                    int worldX = worldCol * gp.tileSize;
                    int worldY = worldRow * gp.tileSize;
                    int screenX = worldX - gp.player.x + gp.player.screenX;
                    int screenY = worldY - gp.player.y + gp.player.screenY;

                    if (worldX + gp.tileSize > gp.player.x - gp.player.screenX &&
                        worldX < gp.player.x + gp.player.screenX + gp.tileSize && // Perbaikan batas kanan
                        worldY + gp.tileSize > gp.player.y - gp.player.screenY &&
                        worldY < gp.player.y + gp.player.screenY + gp.tileSize) { // Perbaikan batas bawah
                        
                        g2.drawImage(currentTile.Image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    }
                }
                worldCol++;
            }
            worldRow++;
        }
    }

    public void updateTiles() {
        if (tiles == null) return;
        for (int r = 0; r < currentMapWorldRow; r++) {
            for (int c = 0; c < currentMapWorldCol; c++) {
                if (tiles[c][r] != null && tiles[c][r] instanceof Soil) {
                    tiles[c][r].update(gp); 
                    Soil wet = (Soil) tiles[c][r];
                    if (wet.getSeedPlanted() != null) {
                        wet.updateImageBasedOnState(gp);
                    }
                    // Panggil update() dari Tile. Soil akan override jika perlu logika khusus.
                                          // Jika update di Soil butuh GamePanel: tiles[c][r].update(gp);
                                          // Maka Tile.update() juga harus terima GamePanel gp.
                }
            }
        }
    }

    public boolean loadMapByID(int mapID) {
        if (mapID >= 0 && mapID < mapFilePaths.length && mapFilePaths[mapID] != null) {
            loadMapByPath(mapFilePaths[mapID]);
            this.currentMapID = mapID;
            // Penting: Update dimensi dunia di GamePanel jika komponen lain menggunakannya
            // gp.worldCol = this.currentMapWorldCol;
            // gp.worldRow = this.currentMapWorldRow;
            System.out.println("Successfully switched to map ID: " + mapID + ". World size: " + currentMapWorldCol + "x" + currentMapWorldRow);
            return true;
        } else {
            System.err.println("Error: Invalid mapID (" + mapID + ") or map file path not configured.");
            if (mapFilePaths.length > 0 && mapFilePaths[0] != null) {
                loadMapByPath(mapFilePaths[0]); // Coba load peta default
                this.currentMapID = 0;
                // gp.worldCol = this.currentMapWorldCol;
                // gp.worldRow = this.currentMapWorldRow;
            } else {
                createEmptyMap(gp.maxScreenCol, gp.maxScreenRow); // Fallback ke peta kosong
            }
            return false;
        }
    }

    private void createEmptyMap(int cols, int rows) {
        this.currentMapWorldCol = cols;
        this.currentMapWorldRow = rows;
        this.tiles = new Tile[this.currentMapWorldCol][this.currentMapWorldRow]; // Tipe Tile[][]
        for (int r = 0; r < this.currentMapWorldRow; r++) {
            for (int c = 0; c < this.currentMapWorldCol; c++) {
                this.tiles[c][r] = createTileInstance(0); // Isi dengan instance tile default
            }
        }
        // gp.worldCol = this.currentMapWorldCol;
        // gp.worldRow = this.currentMapWorldRow;
        System.out.println("Created/Reverted to empty map (" + cols + "x" + rows + ") with Tile instances.");
    }

    public Tile getTile(int worldX, int worldY) {
        int col = worldX / gp.tileSize;
        int row = worldY / gp.tileSize;

        if (tiles != null && col >= 0 && col < currentMapWorldCol && row >= 0 && row < currentMapWorldRow) {
            return tiles[col][row]; // Kembalikan instance Tile
        }
        return null;
    }

    public void setTileType(int worldX, int worldY, int newTilePrototypeID) {
        int col = worldX / gp.tileSize;
        int row = worldY / gp.tileSize;

        if (tiles != null && col >= 0 && col < currentMapWorldCol && row >= 0 && row < currentMapWorldRow) {
            tiles[col][row] = createTileInstance(newTilePrototypeID);
        }
    }

    public void plantSeedAtTile(int worldX, int worldY, Seeds seedToPlant) {
        Tile targetTile = getTile(worldX, worldY);

        if (targetTile instanceof Soil) {
            Soil soilTile = (Soil) targetTile;
            if (soilTile.canPlant()) {
                soilTile.plantSeed(seedToPlant, gp); // Soil akan mengurus perubahan state & gambar
                // Pesan sudah ada di dalam soilTile.plantSeed jika berhasil
            } else {
                String plantedInfo = "is already planted";
                if (soilTile.getSeedPlanted() != null) {
                    plantedInfo += " with " + soilTile.getSeedPlanted().getName();
                }
                System.out.println("Cannot plant at ("+ (worldX/gp.tileSize) + "," + (worldY/gp.tileSize) +"): Tile " + plantedInfo);
            }
        } else {
            String tileType = (targetTile != null) ? targetTile.getClass().getSimpleName() : "Out of bounds or null";
            System.out.println("Cannot plant at ("+ (worldX/gp.tileSize) + "," + (worldY/gp.tileSize) +"): Tile is not Soil. It is " + tileType);
        }
    }
    public void harvestSeedAtTile(int worldX, int worldY) {
        Tile targetTile = getTile(worldX, worldY);

        if (targetTile instanceof Soil) {
            Soil soilTile = (Soil) targetTile;
            if (!soilTile.canPlant()) {
                soilTile.harvest(gp, gp.player); 
            } 
        } 
    }
}