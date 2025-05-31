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
import java.util.Random;

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
    public int angka = new Random().nextInt(3) + 1;
    public int currentMapID = -1; // Inisialisasi agar load pertama selalu fresh

    public String[] mapFilePaths = {
            "/Map/maps/farm_map_" + angka +".txt",
            "/Map/maps/forest_map.txt",
            "/Map/maps/mountain_lake_map.txt",
            "/Map/maps/house_map.txt",
            "/Map/maps/npc_map.txt",
            "/Map/maps/mthouse_map.txt",
            "/Map/maps/chouse_map.txt",
            "/Map/maps/phouse_map.txt",
            "/Map/maps/dhouse_map.txt",
            "/Map/maps/ahouse_map.txt",
            "/Map/maps/store_map.txt",
            "/Map/maps/ocean_map.txt"
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
            tileImage[83] = new Tile("grass_atas_air_kiri", true);
            tileImage[83].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskiriatasair.png"));
            tileImage[84] = new Tile("grass_atas_air_kanan", true);
            tileImage[84].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskananatasair.png"));
            tileImage[85] = new Tile("grass_bawah_air_kiri", true);
            tileImage[85].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskiribawahair.png"));
            tileImage[182] = new Tile("grass_bawah_air_kanan", true);
            tileImage[182].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskananbawahair.png"));

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
            tileImage[12] = new Tile("Door", true); // Walkable agar bisa transisi
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
            tileImage[86] = new Tile("Top Right Corner Roof", false);
            tileImage[86].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/righttopcornerroof.png"));
            tileImage[87] = new Tile("Bottom Left Right Roof", false);
            tileImage[87].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/rightbottomcornerroof.png"));
            tileImage[88] = new Tile("Bottom Roof", false);
            tileImage[88].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/bottomroof.png"));
            tileImage[89] = new Tile("Top Left Corner Roof", false);
            tileImage[89].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/lefttopcornerroof.png"));
            tileImage[90] = new Tile("Bottom Left Corner Roof", false);
            tileImage[90].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/leftbottomcornerroof.png"));
            tileImage[91] = new Tile("MTHouse Wall Top", false);
            tileImage[91].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/walltop.png"));
            tileImage[92] = new Tile("MTHouse Left Corner", false);
            tileImage[92].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/leftcorner.png"));
            tileImage[93] = new Tile("MTHouse Right Corner", false);
            tileImage[93].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/rightcorner.png"));
            tileImage[94] = new Tile("MTHouse Left Window Bottom", false);
            tileImage[94].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/leftwindowbottom.png"));
            tileImage[95] = new Tile("MTHouse Right Window Bottom", false);
            tileImage[95].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/rightwindowbottom.png"));
            tileImage[96] = new Tile("MTHouse Left Window Top", false);
            tileImage[96].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/lefttopwindow.png"));
            tileImage[97] = new Tile("MTHouse Right Window Top", false);
            tileImage[97].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/righttopwindow.png"));
            tileImage[98] = new Tile("Left Wall Top", false);
            tileImage[98].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/leftwalltop.png"));
            tileImage[99] = new Tile("Right Wall Top", false);
            tileImage[99].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/mayortadihouse/rightwalltop.png"));

            // Caroline House Tiles Exterior
            tileImage[100] = new Tile("Top Right Corner Roof", false);
            tileImage[100].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/carolinehouse/righttopcornerroof.png"));
            tileImage[101] = new Tile("Bottom Left Right Roof", false);
            tileImage[101].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/carolinehouse/rightbottomcornerroof.png"));
            tileImage[102] = new Tile("Bottom Roof", false);
            tileImage[102].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/carolinehouse/bottomroof.png"));
            tileImage[103] = new Tile("Top Left Corner Roof", false);
            tileImage[103].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/carolinehouse/lefttopcornerroof.png"));
            tileImage[104] = new Tile("Bottom Left Corner Roof", false);
            tileImage[104].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/carolinehouse/leftbottomcornerroof.png"));
            tileImage[105] = new Tile("MTHouse Wall Top", false);
            tileImage[105].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/carolinehouse/walltop.png"));
            tileImage[106] = new Tile("MTHouse Left Corner", false);
            tileImage[106].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/carolinehouse/leftcorner.png"));
            tileImage[107] = new Tile("MTHouse Right Corner", false);
            tileImage[107].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/carolinehouse/rightcorner.png"));
            tileImage[108] = new Tile("MTHouse Left Window Bottom", false);
            tileImage[108].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/carolinehouse/leftwindowbottom.png"));
            tileImage[109] = new Tile("MTHouse Right Window Bottom", false);
            tileImage[109].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/carolinehouse/rightwindowbottom.png"));
            tileImage[110] = new Tile("MTHouse Left Window Top", false);
            tileImage[110].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/carolinehouse/lefttopwindow.png"));
            tileImage[111] = new Tile("MTHouse Right Window Top", false);
            tileImage[111].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/carolinehouse/righttopwindow.png"));
            tileImage[112] = new Tile("Left Wall Top", false);
            tileImage[112].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/carolinehouse/leftwalltop.png"));
            tileImage[113] = new Tile("Right Wall Top", false);
            tileImage[113].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/carolinehouse/rightwalltop.png"));

            // Perry House Tiles Exterior
            tileImage[114] = new Tile("Top Right Corner Roof", false);
            tileImage[114].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/perryhouse/righttopcornerroof.png"));
            tileImage[115] = new Tile("Bottom Left Right Roof", false);
            tileImage[115].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/perryhouse/rightbottomcornerroof.png"));
            tileImage[116] = new Tile("Bottom Roof", false);
            tileImage[116].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/perryhouse/bottomroof.png"));
            tileImage[117] = new Tile("Top Left Corner Roof", false);
            tileImage[117].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/perryhouse/lefttopcornerroof.png"));
            tileImage[118] = new Tile("Bottom Left Corner Roof", false);
            tileImage[118].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/perryhouse/leftbottomcornerroof.png"));
            tileImage[119] = new Tile("MTHouse Wall Top", false);
            tileImage[119].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/perryhouse/walltop.png"));
            tileImage[120] = new Tile("MTHouse Left Corner", false);
            tileImage[120].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/perryhouse/leftcorner.png"));
            tileImage[121] = new Tile("MTHouse Right Corner", false);
            tileImage[121].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/perryhouse/rightcorner.png"));
            tileImage[122] = new Tile("MTHouse Left Window Bottom", false);
            tileImage[122].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/perryhouse/leftwindowbottom.png"));
            tileImage[123] = new Tile("MTHouse Right Window Bottom", false);
            tileImage[123].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/perryhouse/rightwindowbottom.png"));
            tileImage[124] = new Tile("MTHouse Left Window Top", false);
            tileImage[124].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/perryhouse/lefttopwindow.png"));
            tileImage[125] = new Tile("MTHouse Right Window Top", false);
            tileImage[125].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/perryhouse/righttopwindow.png"));
            tileImage[126] = new Tile("Left Wall Top", false);
            tileImage[126].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/perryhouse/leftwalltop.png"));
            tileImage[127] = new Tile("Right Wall Top", false);
            tileImage[127].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/perryhouse/rightwalltop.png"));

            // Dasco House Tiles Exterior
            tileImage[128] = new Tile("Top Right Corner Roof", false);
            tileImage[128].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/dascohouse/righttopcornerroof.png"));
            tileImage[129] = new Tile("Bottom Left Right Roof", false);
            tileImage[129].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/dascohouse/rightbottomcornerroof.png"));
            tileImage[130] = new Tile("Bottom Roof", false);
            tileImage[130].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/dascohouse/bottomroof.png"));
            tileImage[131] = new Tile("Top Left Corner Roof", false);
            tileImage[131].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/dascohouse/lefttopcornerroof.png"));
            tileImage[132] = new Tile("Bottom Left Corner Roof", false);
            tileImage[132].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/dascohouse/leftbottomcornerroof.png"));
            tileImage[133] = new Tile("MTHouse Wall Top", false);
            tileImage[133].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/dascohouse/walltop.png"));
            tileImage[134] = new Tile("MTHouse Left Corner", false);
            tileImage[134].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/dascohouse/leftcorner.png"));
            tileImage[135] = new Tile("MTHouse Right Corner", false);
            tileImage[135].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/dascohouse/rightcorner.png"));
            tileImage[136] = new Tile("MTHouse Left Window Bottom", false);
            tileImage[136].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/dascohouse/leftwindowbottom.png"));
            tileImage[137] = new Tile("MTHouse Right Window Bottom", false);
            tileImage[137].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/dascohouse/rightwindowbottom.png"));
            tileImage[138] = new Tile("MTHouse Left Window Top", false);
            tileImage[138].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/dascohouse/lefttopwindow.png"));
            tileImage[139] = new Tile("MTHouse Right Window Top", false);
            tileImage[139].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/dascohouse/righttopwindow.png"));
            tileImage[140] = new Tile("Left Wall Top", false);
            tileImage[140].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/dascohouse/leftwalltop.png"));
            tileImage[141] = new Tile("Right Wall Top", false);
            tileImage[141].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/dascohouse/rightwalltop.png"));

            // Abi House Tiles Exterior
            tileImage[142] = new Tile("Top Right Corner Roof", false);
            tileImage[142].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/ahouse/righttopcornerroof.png"));
            tileImage[143] = new Tile("Bottom Left Right Roof", false);
            tileImage[143].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/ahouse/rightbottomcornerroof.png"));
            tileImage[144] = new Tile("Bottom Roof", false);
            tileImage[144].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/ahouse/bottomroof.png"));
            tileImage[145] = new Tile("Top Left Corner Roof", false);
            tileImage[145].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/ahouse/lefttopcornerroof.png"));
            tileImage[146] = new Tile("Bottom Left Corner Roof", false);
            tileImage[146].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/ahouse/leftbottomcornerroof.png"));
            tileImage[147] = new Tile("MTHouse Wall Top", false);
            tileImage[147].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/ahouse/walltop.png"));
            tileImage[148] = new Tile("MTHouse Left Corner", false);
            tileImage[148].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/ahouse/leftcorner.png"));
            tileImage[149] = new Tile("MTHouse Right Corner", false);
            tileImage[149].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/ahouse/rightcorner.png"));
            tileImage[150] = new Tile("MTHouse Left Window Bottom", false);
            tileImage[150].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/ahouse/leftwindowbottom.png"));
            tileImage[151] = new Tile("MTHouse Right Window Bottom", false);
            tileImage[151].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/ahouse/rightwindowbottom.png"));
            tileImage[152] = new Tile("MTHouse Left Window Top", false);
            tileImage[152].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/ahouse/lefttopwindow.png"));
            tileImage[153] = new Tile("MTHouse Right Window Top", false);
            tileImage[153].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/ahouse/righttopwindow.png"));
            tileImage[154] = new Tile("Left Wall Top", false);
            tileImage[154].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/ahouse/leftwalltop.png"));
            tileImage[155] = new Tile("Right Wall Top", false);
            tileImage[155].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/ahouse/rightwalltop.png"));

            // Store Tiles Exterior
            tileImage[156] = new Tile("Top Right Corner Roof", false);
            tileImage[156].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/store/righttopcornerroof.png"));
            tileImage[157] = new Tile("Bottom Left Right Roof", false);
            tileImage[157].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/store/rightbottomcornerroof.png"));
            tileImage[158] = new Tile("Bottom Roof", false);
            tileImage[158].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/store/bottomroof.png"));
            tileImage[159] = new Tile("Top Left Corner Roof", false);
            tileImage[159].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/store/lefttopcornerroof.png"));
            tileImage[160] = new Tile("Bottom Left Corner Roof", false);
            tileImage[160].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/store/leftbottomcornerroof.png"));
            tileImage[161] = new Tile("MTHouse Wall Top", false);
            tileImage[161].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/store/walltop.png"));
            tileImage[162] = new Tile("MTHouse Left Corner", false);
            tileImage[162].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/store/leftcorner.png"));
            tileImage[163] = new Tile("MTHouse Right Corner", false);
            tileImage[163].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/store/rightcorner.png"));
            tileImage[164] = new Tile("MTHouse Left Window Bottom", false);
            tileImage[164].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/store/leftwindowbottom.png"));
            tileImage[165] = new Tile("MTHouse Right Window Bottom", false);
            tileImage[165].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/store/rightwindowbottom.png"));
            tileImage[166] = new Tile("MTHouse Left Window Top", false);
            tileImage[166].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/store/lefttopwindow.png"));
            tileImage[167] = new Tile("MTHouse Right Window Top", false);
            tileImage[167].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/store/righttopwindow.png"));
            tileImage[168] = new Tile("Left Wall Top", false);
            tileImage[168].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/store/leftwalltop.png"));
            tileImage[169] = new Tile("Right Wall Top", false);
            tileImage[169].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/store/rightwalltop.png"));
            tileImage[170] = new Tile("Left Wall Top", false);
            tileImage[170].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/store/S.png"));
            tileImage[171] = new Tile("Right Wall Top", false);
            tileImage[171].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/store/T.png"));
            tileImage[172] = new Tile("Right Wall Top", false);
            tileImage[172].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/store/O.png"));
            tileImage[173] = new Tile("Right Wall Top", false);
            tileImage[173].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/store/R.png"));
            tileImage[174] = new Tile("Right Wall Top", false);
            tileImage[174].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/store/E.png"));


            //Shipping Bin
            tileImage[175] = new Tile("Shipping Bin NI", false);
            tileImage[175].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/shippingbin/SB1.png"));
            tileImage[176] = new Tile("Shipping Bin NI", false);
            tileImage[176].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/shippingbin/SB2.png"));
            tileImage[177] = new Tile("Shipping Bin NI", false);
            tileImage[177].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/shippingbin/SB3.png"));
            tileImage[178] = new Tile("Shipping Bin NI", false);
            tileImage[178].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/shippingbin/SB4.png"));
            tileImage[179] = new ShippingBin("Shipping Bin", false, gp);
            tileImage[179].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/shippingbin/SB5.png"));
            tileImage[180] = new Tile("Shipping Bin NI", false);
            tileImage[180].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/shippingbin/SB6.png"));
            tileImage[181] = new Tile("Pad", true);
            tileImage[181].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/shippingbin/pad.png"));



        } catch (IOException e) {
            System.err.println("Error loading tile prototype images: " + e.getMessage());
            e.printStackTrace();
            for(int i=0; i<tileImage.length; i++) { 
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

        Tile prototype = tileImage[prototypeID]; 
        Tile newInstance;

        if (prototype instanceof Soil) {
            newInstance = new Soil((Soil) prototype);
        } else if (prototype instanceof Bed) { 
            newInstance = new Bed((Bed) prototype);
        } else if (prototype instanceof Stove) {
            newInstance = new Stove((Stove) prototype);
        } else if (prototype instanceof TV) {
            newInstance = new TV((TV) prototype);
        } else if (prototype instanceof  ShippingBin) {
            newInstance = new ShippingBin((ShippingBin) prototype);
        }
        else {
            newInstance = new Tile(prototype);
        }
        return newInstance;
    }

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

            this.currentMapTiles = newMapTileInstances;
            this.currentMapWorldCol = cols;
            this.currentMapWorldRow = rows;
            this.currentMapID = mapIdToLoad;

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
            if (currentMapID == -1 && mapFilePaths.length > 0 && mapFilePaths[0] != null) { 
                 return loadMapByID(0); 
            } else if (currentMapID == -1) { 
                createEmptyMapAndCache(gp.maxScreenCol, gp.maxScreenRow, 0); 
            }
            return false;
        }

        if (loadedMapStates.containsKey(mapID)) {
            MapState cachedState = loadedMapStates.get(mapID);
            this.currentMapTiles = cachedState.tiles; 
            this.currentMapWorldCol = cachedState.worldCol;
            this.currentMapWorldRow = cachedState.worldRow;
            this.currentMapID = mapID;
            System.out.println("Map loaded from cache. ID: " + mapID + ". World size: " + currentMapWorldCol + "x" + currentMapWorldRow);
            return true;
        } else {
            loadFreshMapFromFileAndCache(mapFilePaths[mapID], mapID);
            return true;
        }
    }

    private void createEmptyMapAndCache(int cols, int rows, int mapIdForCache) {
        Tile[][] emptyTiles = new Tile[cols][rows];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                emptyTiles[c][r] = createTileInstance(0);
            }
        }
        this.currentMapTiles = emptyTiles;
        this.currentMapWorldCol = cols;
        this.currentMapWorldRow = rows;
        this.currentMapID = mapIdForCache;

        loadedMapStates.put(mapIdForCache, new MapState(emptyTiles, cols, rows));
        System.out.println("Created/Reverted to empty map (ID: " + mapIdForCache + ") (" + cols + "x" + rows + ") and cached.");
    }
    
    public void draw(Graphics2D g2) {
        if (currentMapTiles == null) return; 

        for (int worldRow = 0; worldRow < currentMapWorldRow; worldRow++) {
            for (int worldCol = 0; worldCol < currentMapWorldCol; worldCol++) {
                Tile currentTileToDraw = currentMapTiles[worldCol][worldRow]; 
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
                Tile tiles = currentMapTiles[c][r]; // Menggunakan currentMapTiles
                if (tiles != null && tiles instanceof Soil) {
                    tiles.update(gp); 
                    Soil wet = (Soil) tiles;
                    if (wet.getSeedPlanted() != null) {
                        if (gp.currentWeather == gp.initialWeather[0]) {
                            wet.water(gp);
                        }
                        wet.updateImageBasedOnState(gp);
                    }
                } else if (tiles != null && tiles instanceof Stove) {
                    Stove stove = (Stove) tiles;
                    stove.update(gp);
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
        Tile targetTile = getTile(worldX, worldY); 
        if (targetTile instanceof Soil) {
            Soil soilTile = (Soil) targetTile;
            soilTile.harvest(gp, gp.player);
        } else {
            System.out.println("Cannot harvest at ("+ (worldX/gp.tileSize) + "," + (worldY/gp.tileSize) +"): Not a soil tile or nothing to harvest.");
        }
    }

    public int getDoorLocationTileX() {
        if (currentMapTiles == null || currentMapWorldCol == 0 || currentMapWorldRow == 0) {
            System.err.println("Peringatan getDoorLocationTileX: Peta belum dimuat atau peta kosong.");
            return -1;
        }

        for (int col = 0; col < currentMapWorldCol; col++) {
            for (int row = 0; row < currentMapWorldRow; row++) {
                Tile tile = currentMapTiles[col][row];
                if (tile != null && "Door".equals(tile.getTileName())) {
                    return col; // Kembalikan indeks kolom (koordinat tile X)
                }
            }
        }

        System.err.println("Peringatan getDoorLocationTileX: Tidak ada tile 'Door' yang ditemukan di peta.");
        return -1; // Indikasi "Door" tidak ditemukan
    }

    /**
     * Mencari tile pertama dengan nama "Door" di peta saat ini dan mengembalikan
     * koordinat baris (tile Y) tempat tile tersebut ditemukan.
     *
     * @return Indeks baris (koordinat tile Y) dari "Door" pertama, atau -1 jika tidak ditemukan atau peta kosong.
     */
    public int getDoorLocationTileY() {
        if (currentMapTiles == null || currentMapWorldCol == 0 || currentMapWorldRow == 0) {
            System.err.println("Peringatan getDoorLocationTileY: Peta belum dimuat atau peta kosong.");
            return -1; // Indikasi error atau peta kosong
        }

        // Cari tile dengan nama "Door" di seluruh peta
        // Urutan pencarian harus konsisten dengan getDoorLocationTileX() jika Anda ingin
        // mendapatkan koordinat X dan Y dari pintu yang sama.
        for (int col = 0; col < currentMapWorldCol; col++) {
            for (int row = 0; row < currentMapWorldRow; row++) {
                Tile tile = currentMapTiles[col][row];
                // Pastikan nama "Door" konsisten.
                if (tile != null && "Door".equals(tile.getTileName())) {
                    return row; // Kembalikan indeks baris (koordinat tile Y)
                }
            }
        }

        System.err.println("Peringatan getDoorLocationTileY: Tidak ada tile 'Door' yang ditemukan di peta.");
        return -1; // Indikasi "Door" tidak ditemukan
    }
}