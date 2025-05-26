package Map;

import main.GamePanel;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import Items.Seeds;
import Furniture.Bed;
import Furniture.Stove;
import Furniture.TV;
// import Items.Misc; // Misc tidak digunakan secara langsung di sini, bisa dihapus jika tidak ada rencana

import java.awt.Graphics2D;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap; // Untuk menyimpan state peta

public class Map {
    GamePanel gp;
    public Tile[] tilePrototypes; // Array untuk PROTOTYPE tile (diganti nama dari tileimage)
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
    public int currentMapID = -1; // Inisialisasi ke -1 agar peta pertama pasti dimuat fresh

    public String[] mapFilePaths = {
            "/Map/maps/farm_map.txt",
            "/Map/maps/forest_map.txt",
            "/Map/maps/mountain_lake_map.txt",
            "/Map/maps/house_map.txt"
    };

    public Map(GamePanel gp) {
        this.gp = gp;
        this.tilePrototypes = new Tile[100];
        this.loadedMapStates = new HashMap<>();
        getTileImagePrototypes();
        loadMapByID(0); // Memuat peta default (misalnya ID 0)
    }

    public void getTileImagePrototypes() {
        try {
            // GRASS
            tilePrototypes[0] = new Tile("grass", true);
            tilePrototypes[0].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grass.png"));
            tilePrototypes[1] = new Tile("grass_kiri_atas", true);
            tilePrototypes[1].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskiriatas.png"));
            tilePrototypes[2] = new Tile("grass_atas", true);
            tilePrototypes[2].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grassatas.png"));
            tilePrototypes[3] = new Tile("grass_kiri", true);
            tilePrototypes[3].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskiri.png"));
            tilePrototypes[4] = new Tile("grass_kiri_bawah", true);
            tilePrototypes[4].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskiribawah.png"));
            tilePrototypes[5] = new Tile("grass_bawah", true);
            tilePrototypes[5].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grassbawah.png"));
            tilePrototypes[6] = new Tile("grass_kanan", true);
            tilePrototypes[6].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskanan.png"));
            tilePrototypes[7] = new Tile("grass_kanan_atas", true);
            tilePrototypes[7].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskananatas.png"));
            tilePrototypes[8] = new Tile("grass_kanan_bawah", true);
            tilePrototypes[8].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskananbawah.png"));
            tilePrototypes[11] = new Tile("grass_atas_air", true);
            tilePrototypes[11].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grassatasair.png"));
            tilePrototypes[46] = new Tile("grass_bawah_air", true);
            tilePrototypes[46].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grassbawahair.png"));
            tilePrototypes[47] = new Tile("grass_kiri_air", true);
            tilePrototypes[47].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskiriair.png"));
            tilePrototypes[48] = new Tile("grass_kanan_air", true);
            tilePrototypes[48].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskananair.png"));

            // HOUSE & FLOOR
            tilePrototypes[49] = new Tile("floor", true);
            tilePrototypes[49].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/floor.png"));
            tilePrototypes[50] = new Tile("wallkiriatas", false);
            tilePrototypes[50].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/wallkiriatas.png"));
            tilePrototypes[51] = new Tile("wallatas", false);
            tilePrototypes[51].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/wallatas.png"));
            tilePrototypes[52] = new Tile("wallkananatas", false);
            tilePrototypes[52].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/wallkananatas.png"));
            tilePrototypes[53] = new Tile("wall", false);
            tilePrototypes[53].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/wall.png"));
            tilePrototypes[54] = new Tile("opendoor", true);
            tilePrototypes[54].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/opendoor.png"));
            tilePrototypes[55] = new Tile("wallkiribawah", false);
            tilePrototypes[55].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/wallkiribawah.png"));
            tilePrototypes[56] = new Tile("wallkananbawah", false);
            tilePrototypes[56].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/wallkananbawah.png"));
            tilePrototypes[57] = new Tile("door", true); // Asumsi 'door' adalah tile yang bisa ditransisikan, bukan actual door object
            tilePrototypes[57].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/door.png"));
            tilePrototypes[58] = new Tile("window", false);
            tilePrototypes[58].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/window.png"));
            tilePrototypes[59] = new Tile("pertigaan", false); // Asumsi ini adalah bagian dari dinding
            tilePrototypes[59].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/pertigaan.png"));
            tilePrototypes[60] = new Tile("mentokbawah", false); // Asumsi ini adalah bagian dari dinding
            tilePrototypes[60].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/mentokbawah.png"));

            // Carpet
            tilePrototypes[61] = new Tile("karpetpojokkiri", true);
            tilePrototypes[61].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetpojokkiri.png"));
            tilePrototypes[62] = new Tile("karpetkiri", true);
            tilePrototypes[62].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetkiri.png"));
            tilePrototypes[63] = new Tile("karpetatas", true);
            tilePrototypes[63].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetatas.png"));
            tilePrototypes[64] = new Tile("karpetpojokkanan", true);
            tilePrototypes[64].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetpojokkanan.png"));
            tilePrototypes[65] = new Tile("karpetkanan", true);
            tilePrototypes[65].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetkanan.png"));
            tilePrototypes[66] = new Tile("belokkanan", true); // Nama tile mungkin perlu disesuaikan
            tilePrototypes[66].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/belokkanan.png"));
            tilePrototypes[67] = new Tile("karpetmentok", true);
            tilePrototypes[67].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetmentok.png"));
            tilePrototypes[68] = new Tile("karpetbawah", true);
            tilePrototypes[68].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetbawah.png"));
            tilePrototypes[69] = new Tile("karpetpojokiribawah", true);
            tilePrototypes[69].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetpojokiribawah.png"));
            tilePrototypes[70] = new Tile("belokhehe", true); // Nama tile mungkin perlu disesuaikan
            tilePrototypes[70].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/belokhehe.png"));
            tilePrototypes[71] = new Tile("karpet", true);
            tilePrototypes[71].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpet.png"));

            // Furnitures (sebagai Tile khusus)
            tilePrototypes[72] = new Bed("Bed Part 1", false, "king_ul"); // Nama unik untuk tiap bagian
            tilePrototypes[72].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed1.png"));
            tilePrototypes[73] = new Bed("Bed Part 2", false, "king_um");
            tilePrototypes[73].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed2.png"));
            tilePrototypes[74] = new Bed("Bed Part 3", false, "king_ur");
            tilePrototypes[74].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed3.png"));
            tilePrototypes[75] = new Bed("Bed Part 4", false, "king_ml");
            tilePrototypes[75].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed4.png"));
            tilePrototypes[76] = new Bed("Bed Part 5 (Interact)", true, "king_mm"); // Bagian interaksi bisa walkable true jika interactionArea di atasnya
            tilePrototypes[76].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed5.png"));
            tilePrototypes[77] = new Bed("Bed Part 6", false, "king_mr");
            tilePrototypes[77].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed6.png"));
            tilePrototypes[78] = new Bed("Bed Part 7", false, "king_bl");
            tilePrototypes[78].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed7.png"));
            tilePrototypes[79] = new Bed("Bed Part 8", false, "king_bm");
            tilePrototypes[79].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed8.png"));
            tilePrototypes[80] = new Bed("Bed Part 9", false, "king_br");
            tilePrototypes[80].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed9.png"));

            tilePrototypes[81] = new Stove("stove"); // walkable default false di Furniture
            tilePrototypes[81].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/stove.png"));
            tilePrototypes[82] = new TV("tv", false); // TV biasanya non-walkable
            tilePrototypes[82].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/TV.png"));

            // WATER
            tilePrototypes[9] = new Tile("Water", false);
            tilePrototypes[9].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/water.png"));

            // SOIL
            tilePrototypes[10] = new Soil("soil", true, "/Map/tiles/dirt.png");

            // PLACEHOLDER untuk Building/Door jika belum ada kelasnya
            tilePrototypes[12] = new Tile("Door Visual Placeholder", false); // Sebaiknya walkable true jika ini adalah tile di bawah pintu yang bisa dilewati
            tilePrototypes[12].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/door.png")); // Gambar pintu

            // VISUAL PROTOTYPES untuk Benih yang Ditanam
            tilePrototypes[13] = new Tile("Planted Parsnip Visual", true);
            tilePrototypes[13].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Parsnip Seeds.png"));
            tilePrototypes[14] = new Tile("Planted Cauliflower Visual", true);
            tilePrototypes[14].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Cauliflower Seeds.png"));
            tilePrototypes[15] = new Tile("Planted Potato Visual", true);
            tilePrototypes[15].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Potato Seeds.png"));
            tilePrototypes[16] = new Tile("Planted Wheat Visual", true);
            tilePrototypes[16].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Wheat Seeds.png"));
            tilePrototypes[17] = new Tile("Planted Blueberry Visual", true);
            tilePrototypes[17].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Blueberry Seeds.png"));
            tilePrototypes[18] = new Tile("Planted Tomato Visual", true);
            tilePrototypes[18].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Tomato Seeds.png"));
            tilePrototypes[19] = new Tile("Planted Hot Pepper Visual", true);
            tilePrototypes[19].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Hot Pepper Seeds.png"));
            tilePrototypes[20] = new Tile("Planted Melon Visual", true);
            tilePrototypes[20].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Melon Seeds.png"));
            tilePrototypes[21] = new Tile("Planted Cranberry Visual", true);
            tilePrototypes[21].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Cranberry Seeds.png"));
            tilePrototypes[22] = new Tile("Planted Pumpkin Visual", true);
            tilePrototypes[22].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Pumpkin Seeds.png"));
            tilePrototypes[23] = new Tile("Planted Grape Visual", true);
            tilePrototypes[23].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Grape Seeds.png"));

            tilePrototypes[24] = new Tile("Wet Parsnip Visual", true);
            tilePrototypes[24].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Parsnip Seeds.png"));
            tilePrototypes[25] = new Tile("Wet Cauliflower Visual", true);
            tilePrototypes[25].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Cauliflower Seeds.png"));
            // ... (lanjutkan untuk semua wet seeds)
            tilePrototypes[26] = new Tile("Wet Potato Visual", true);
            tilePrototypes[26].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Potato Seeds.png"));
            tilePrototypes[27] = new Tile("Wet Wheat Visual", true);
            tilePrototypes[27].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Wheat Seeds.png"));
            tilePrototypes[28] = new Tile("Wet Blueberry Visual", true);
            tilePrototypes[28].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Blueberry Seeds.png"));
            tilePrototypes[29] = new Tile("Wet Tomato Visual", true);
            tilePrototypes[29].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Tomato Seeds.png"));
            tilePrototypes[30] = new Tile("Wet Hot Pepper Visual", true);
            tilePrototypes[30].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Hot Pepper Seeds.png"));
            tilePrototypes[31] = new Tile("Wet Melon Visual", true);
            tilePrototypes[31].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Melon Seeds.png"));
            tilePrototypes[32] = new Tile("Wet Cranberry Visual", true);
            tilePrototypes[32].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Cranberry Seeds.png"));
            tilePrototypes[33] = new Tile("Wet Pumpkin Visual", true);
            tilePrototypes[33].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Pumpkin Seeds.png"));
            tilePrototypes[34] = new Tile("Wet Grape Visual", true);
            tilePrototypes[34].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Wet Grape Seeds.png"));


            // VISUAL PROTOTYPES untuk Tanaman Siap Panen
            tilePrototypes[35] = new Tile("Harvestable Parsnip Visual", true);
            tilePrototypes[35].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Parsnip.png"));
            tilePrototypes[36] = new Tile("Harvestable Cauliflower Visual", true);
            tilePrototypes[36].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Cauliflower.png"));
            // ... (lanjutkan untuk semua harvestable crops)
            tilePrototypes[37] = new Tile("Harvestable Potato Visual", true);
            tilePrototypes[37].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Potato.png"));
            tilePrototypes[38] = new Tile("Harvestable Wheat Visual", true);
            tilePrototypes[38].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Wheat.png"));
            tilePrototypes[39] = new Tile("Harvestable Blueberry Visual", true);
            tilePrototypes[39].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Blueberry.png"));
            tilePrototypes[40] = new Tile("Harvestable Tomato Visual", true);
            tilePrototypes[40].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Tomato.png"));
            tilePrototypes[41] = new Tile("Harvestable Hot Pepper Visual", true);
            tilePrototypes[41].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Hot Pepper.png"));
            tilePrototypes[42] = new Tile("Harvestable Melon Visual", true);
            tilePrototypes[42].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Melon.png"));
            tilePrototypes[43] = new Tile("Harvestable Cranberry Visual", true);
            tilePrototypes[43].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Cranberry.png"));
            tilePrototypes[44] = new Tile("Harvestable Pumpkin Visual", true);
            tilePrototypes[44].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Pumpkin.png"));
            tilePrototypes[45] = new Tile("Harvestable Grape Visual", true);
            tilePrototypes[45].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/crops/Harvestable Grape.png"));

        } catch (IOException e) {
            System.err.println("Error loading tile prototype images: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error in getTileImagePrototypes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Tile createTileInstance(int prototypeID) {
        if (prototypeID < 0 || prototypeID >= tilePrototypes.length || tilePrototypes[prototypeID] == null) {
            System.err.println("Warning: Invalid prototypeID " + prototypeID + " in createTileInstance. Using default tile 0.");
            prototypeID = 0;
            if (tilePrototypes[prototypeID] == null) { // Fallback jika prototype 0 juga null
                Tile errorTile = new Tile("Error Tile", false);
                try {
                    errorTile.Image = new BufferedImage(gp.tileSize, gp.tileSize, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = errorTile.Image.createGraphics();
                    g.setColor(java.awt.Color.MAGENTA);
                    g.fillRect(0,0,gp.tileSize, gp.tileSize);
                    g.dispose();
                } catch (Exception e) { /* abaikan */ }
                return errorTile;
            }
        }

        Tile prototype = tilePrototypes[prototypeID];
        Tile newInstance;

        // Gunakan copy constructor jika tersedia untuk state yang lebih kompleks
        if (prototype instanceof Soil) {
            newInstance = new Soil((Soil) prototype);
        } else if (prototype instanceof Bed) {
            newInstance = new Bed((Bed) prototype);
        }
        // Tambahkan else if untuk kelas Tile kustom lainnya yang memiliki copy constructor
        // else if (prototype instanceof YourCustomTile) {
        //     newInstance = new YourCustomTile((YourCustomTile) prototype);
        // }
        else {
            // Untuk Tile standar, buat instance baru dan copy properti dasar
            newInstance = new Tile(prototype.getTileName(), prototype.isWalkable());
            newInstance.Image = prototype.Image; // Berbagi gambar dari prototype itu OK untuk Tile sederhana
            // Jika Tile memiliki state lain yang perlu di-copy, lakukan di sini atau buat copy constructor di Tile.java
        }
        return newInstance;
    }

    private void loadFreshMapFromFile(String mapFilePath, int mapIdToLoad) {
        try {
            InputStream is = getClass().getResourceAsStream(mapFilePath);
            if (is == null) {
                System.err.println("FATAL ERROR: File " + mapFilePath + " tidak ditemukan!");
                createEmptyMapAsCurrent(gp.maxScreenCol, gp.maxScreenRow, mapIdToLoad); // Buat peta kosong jika file tidak ada
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            ArrayList<String> lines = new ArrayList<>();
            String currentLine;
            while ((currentLine = br.readLine()) != null) { lines.add(currentLine.trim()); }
            br.close();

            if (lines.isEmpty()) {
                System.err.println("Map file is empty: " + mapFilePath);
                createEmptyMapAsCurrent(gp.maxScreenCol, gp.maxScreenRow, mapIdToLoad);
                return;
            }

            int rows = lines.size();
            int cols = 0;
            if (rows > 0 && !lines.get(0).isEmpty()) {
                cols = lines.get(0).split("\\s+").length;
            } else {
                System.err.println("Map file has empty lines or zero rows: " + mapFilePath);
                createEmptyMapAsCurrent(gp.maxScreenCol, gp.maxScreenRow, mapIdToLoad);
                return;
            }

            Tile[][] newMapTiles = new Tile[cols][rows];

            for (int row = 0; row < rows; row++) {
                String lineData = lines.get(row);
                String[] numbers = lineData.split("\\s+");
                if (numbers.length < cols) {
                    System.err.println("Warning: Map file " + mapFilePath + " row " + (row+1) + " has fewer columns (" + numbers.length +") than expected (" + cols + "). Padding with default tiles.");
                }
                for (int col = 0; col < cols; col++) {
                    if (col < numbers.length && !numbers[col].isEmpty()) {
                        try {
                            int tilePrototypeID = Integer.parseInt(numbers[col]);
                            newMapTiles[col][row] = createTileInstance(tilePrototypeID);
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing number in " + mapFilePath + " at row " + (row + 1) + ", col " + (col + 1) + ". Val: '" + numbers[col] + "'. Using default.");
                            newMapTiles[col][row] = createTileInstance(0);
                        }
                    } else {
                        newMapTiles[col][row] = createTileInstance(0);
                    }
                }
            }

            this.currentMapTiles = newMapTiles;
            this.currentMapWorldCol = cols;
            this.currentMapWorldRow = rows;
            this.currentMapID = mapIdToLoad;

            // Simpan state peta yang baru dimuat ke cache
            loadedMapStates.put(mapIdToLoad, new MapState(newMapTiles, cols, rows));
            System.out.println("Map loaded from file and cached: " + mapFilePath + " (ID: " + mapIdToLoad + ") Dimensions: " + cols + "x" + rows);

        } catch (IOException e) {
            System.err.println("IOException loading map " + mapFilePath + ": " + e.getMessage());
            createEmptyMapAsCurrent(gp.maxScreenCol, gp.maxScreenRow, mapIdToLoad);
        } catch (Exception e) {
            System.err.println("Unexpected error loading map " + mapFilePath + ": " + e.getMessage());
            e.printStackTrace();
            createEmptyMapAsCurrent(gp.maxScreenCol, gp.maxScreenRow, mapIdToLoad);
        }
    }


    public boolean loadMapByID(int mapID) {
        if (mapID < 0 || mapID >= mapFilePaths.length || mapFilePaths[mapID] == null) {
            System.err.println("Error: Invalid mapID (" + mapID + ") or map file path not configured.");
            // Coba load peta default jika ada, jika tidak, jangan lakukan apa-apa atau buat peta kosong
            if (currentMapID == -1 && mapFilePaths.length > 0 && mapFilePaths[0] != null) { // Hanya jika belum ada peta yang dimuat
                 return loadMapByID(0); // Rekursif panggil dengan ID 0
            } else if (currentMapID == -1) { // Jika benar-benar tidak ada peta default
                createEmptyMapAsCurrent(gp.maxScreenCol, gp.maxScreenRow, 0); // Buat peta kosong sebagai ID 0
            }
            return false; // Gagal memuat mapID yang diminta
        }

        if (loadedMapStates.containsKey(mapID)) {
            // Peta sudah ada di cache, muat dari cache
            MapState cachedState = loadedMapStates.get(mapID);
            this.currentMapTiles = cachedState.tiles;
            this.currentMapWorldCol = cachedState.worldCol;
            this.currentMapWorldRow = cachedState.worldRow;
            this.currentMapID = mapID;
            System.out.println("Map loaded from cache. ID: " + mapID + ". World size: " + currentMapWorldCol + "x" + currentMapWorldRow);
            return true;
        } else {
            // Peta belum ada di cache, muat dari file
            loadFreshMapFromFile(mapFilePaths[mapID], mapID);
            // currentMapID, currentMapTiles, dll sudah diatur di dalam loadFreshMapFromFile
            return true; // Asumsi loadFreshMapFromFile berhasil atau menangani errornya sendiri
        }
    }

    // Digunakan sebagai fallback jika file peta tidak ditemukan atau error saat load awal
    private void createEmptyMapAsCurrent(int cols, int rows, int mapIdForCache) {
        this.currentMapWorldCol = cols;
        this.currentMapWorldRow = rows;
        this.currentMapTiles = new Tile[cols][rows];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                this.currentMapTiles[c][r] = createTileInstance(0); // Isi dengan instance tile default
            }
        }
        this.currentMapID = mapIdForCache; // Tetapkan ID untuk peta kosong ini
        // Simpan peta kosong ini ke cache juga agar tidak dibuat ulang terus menerus jika ada masalah
        loadedMapStates.put(mapIdForCache, new MapState(this.currentMapTiles, cols, rows));
        System.out.println("Created/Reverted to empty map (ID: " + mapIdForCache + ") (" + cols + "x" + rows + ") and cached.");
    }


    public void draw(Graphics2D g2) {
        if (currentMapTiles == null) return;

        int worldCol = 0;
        int worldRow = 0;

        while (worldRow < currentMapWorldRow) {
            worldCol = 0;
            while (worldCol < currentMapWorldCol) {
                Tile currentTile = currentMapTiles[worldCol][worldRow];

                if (currentTile != null && currentTile.Image != null) {
                    int worldX = worldCol * gp.tileSize;
                    int worldY = worldRow * gp.tileSize;
                    int screenX = worldX - gp.player.x + gp.player.screenX;
                    int screenY = worldY - gp.player.y + gp.player.screenY;

                    if (worldX + gp.tileSize > gp.player.x - gp.player.screenX &&
                        worldX - gp.tileSize < gp.player.x + gp.player.screenX && // Sedikit toleransi agar tile di tepi tetap tergambar
                        worldY + gp.tileSize > gp.player.y - gp.player.screenY &&
                        worldY - gp.tileSize < gp.player.y + gp.player.screenY) {
                        g2.drawImage(currentTile.Image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    }
                }
                worldCol++;
            }
            worldRow++;
        }
    }

    public void updateTiles() {
        if (currentMapTiles == null) return;
        for (int r = 0; r < currentMapWorldRow; r++) {
            for (int c = 0; c < currentMapWorldCol; c++) {
                if (currentMapTiles[c][r] != null) { // Cek null untuk keamanan
                    // Panggil update generik dari Tile, yang bisa di-override oleh subclass
                    currentMapTiles[c][r].update();

                    // Logika spesifik Soil untuk update gambar berdasarkan state
                    if (currentMapTiles[c][r] instanceof Soil) {
                        Soil soilTile = (Soil) currentMapTiles[c][r];
                        if (soilTile.getSeedPlanted() != null ) { // Hanya update gambar jika ada perubahan relevan
                            soilTile.updateImageBasedOnState(gp);
                        }
                    }
                    // Anda bisa menambahkan logika update spesifik untuk tile lain di sini jika perlu
                }
            }
        }
    }
    
    public void advanceDay() {
        System.out.println("Map: Advancing to a new day.");
        // Iterasi semua map yang ada di cache
        for (MapState mapState : loadedMapStates.values()) {
            if (mapState != null && mapState.tiles != null) {
                for (int r = 0; r < mapState.worldRow; r++) {
                    for (int c = 0; c < mapState.worldCol; c++) {
                        Tile tile = mapState.tiles[c][r];
                        if (tile instanceof Soil) {
                            Soil soilTile = (Soil) tile;
                            // soilTile.advanceDay(gp); // Panggil method advanceDay pada setiap Soil tile
                        }
                        // Anda bisa menambahkan logika advanceDay untuk tile lain di sini
                    }
                }
            }
        }
        // Pastikan peta yang sedang aktif juga terupdate gambarnya jika ada perubahan state
        updateTiles(); // Ini akan memanggil updateImageBasedOnState untuk soil di peta aktif
    }


    public Tile getTile(int worldX, int worldY) {
        int col = worldX / gp.tileSize;
        int row = worldY / gp.tileSize;

        if (currentMapTiles != null && col >= 0 && col < currentMapWorldCol && row >= 0 && row < currentMapWorldRow) {
            return currentMapTiles[col][row];
        }
        return null;
    }

    public void setTileType(int worldX, int worldY, int newTilePrototypeID) {
        int col = worldX / gp.tileSize;
        int row = worldY / gp.tileSize;

        if (currentMapTiles != null && col >= 0 && col < currentMapWorldCol && row >= 0 && row < currentMapWorldRow) {
            currentMapTiles[col][row] = createTileInstance(newTilePrototypeID);
            // Tidak perlu update cache di sini karena currentMapTiles adalah referensi ke tiles di dalam MapState yang ada di cache.
            // Perubahan pada currentMapTiles[c][r] akan langsung tercermin di cache.
        }
    }

    public void plantSeedAtTile(int worldX, int worldY, Seeds seedToPlant) {
        Tile targetTile = getTile(worldX, worldY); // Menggunakan currentMapTiles

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
            String tileType = (targetTile != null) ? targetTile.getClass().getSimpleName() : "Out of bounds or null";
            System.out.println("Cannot plant at ("+ (worldX/gp.tileSize) + "," + (worldY/gp.tileSize) +"): Tile is not Soil. It is " + tileType);
        }
    }

    public void harvestSeedAtTile(int worldX, int worldY) {
        Tile targetTile = getTile(worldX, worldY); // Menggunakan currentMapTiles

        if (targetTile instanceof Soil) {
            Soil soilTile = (Soil) targetTile;
            // Logika panen sudah ada di Soil.harvest(), panggil saja itu
            soilTile.harvest(gp, gp.player);
        } else {
            System.out.println("Cannot harvest at ("+ (worldX/gp.tileSize) + "," + (worldY/gp.tileSize) +"): Not a soil tile or nothing to harvest.");
        }
    }
}