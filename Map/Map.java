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
    public Tile[] tileimage; // Array untuk PROTOTYPE tile
    public Tile tiles[][];   // Array 2D untuk INSTANCE tile di peta

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
        this.tileimage = new Tile[100]; // Sesuaikan ukuran jika perlu lebih banyak prototype
        // tiles[][] akan diinisialisasi di loadMapByID() atau createEmptyMap()
        getTileImagePrototypes();
        loadMapByID(0); // Memuat peta default
    }

    public void getTileImagePrototypes() {
        try {
            // GRASS
            // Menggunakan constructor Tile(String name, boolean isWalkable)
            tileimage[0] = new Tile("grass", true); // walkable = true
            tileimage[0].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grass.png"));
            tileimage[1] = new Tile("grass_kiri_atas", true);
            tileimage[1].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskiriatas.png"));
            tileimage[2] = new Tile("grass_atas", true);
            tileimage[2].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grassatas.png"));
            tileimage[3] = new Tile("grass_kiri", true);
            tileimage[3].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskiri.png"));
            tileimage[4] = new Tile("grass_kiri_bawah", true);
            tileimage[4].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskiribawah.png"));
            tileimage[5] = new Tile("grass_bawah", true);
            tileimage[5].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grassbawah.png"));
            tileimage[6] = new Tile("grass_kanan", true);
            tileimage[6].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskanan.png"));
            tileimage[7] = new Tile("grass_kanan_atas", true);
            tileimage[7].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskananatas.png"));
            tileimage[8] = new Tile("grass_kanan_bawah", true);
            tileimage[8].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskananbawah.png"));
            tileimage[11] = new Tile("grass_atas_air", true);
            tileimage[11].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grassatasair.png"));
            tileimage[46] = new Tile("grass_bawah_air", true);
            tileimage[46].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grassbawahair.png"));
            tileimage[47] = new Tile("grass_kiri_air", true);
            tileimage[47].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskiriair.png"));
            tileimage[48] = new Tile("grass_kanan_air", true);
            tileimage[48].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskananair.png"));
            tileimage[49] = new Tile("floor", true);


            // HOUSE
            tileimage[49].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/floor.png"));
            tileimage[50] = new Tile("wallkiriatas", false);
            tileimage[50].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/wallkiriatas.png"));
            tileimage[51] = new Tile("wallatas", false);
            tileimage[51].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/wallatas.png"));
            tileimage[52] = new Tile("wallkananatas", false);
            tileimage[52].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/wallkananatas.png"));
            tileimage[53] = new Tile("wall", false);
            tileimage[53].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/wall.png"));
            tileimage[54] = new Tile("opendoor", true);
            tileimage[54].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/opendoor.png")); // Gambar pintu terbuka
            tileimage[55] = new Tile("wallkiribawah", false);
            tileimage[55].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/wallkiribawah.png"));
            tileimage[56] = new Tile("wallkananbawah", false);
            tileimage[56].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/wallkananbawah.png"));
            tileimage[57] = new Tile("door", true);
            tileimage[57].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/door.png")); // Gambar pintu
            tileimage[58] = new Tile("window", false);
            tileimage[58].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/window.png")); // Gambar jendela
            tileimage[59] = new Tile("pertigaan", false);
            tileimage[59].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/pertigaan.png")); // Gambar pertigaan
            tileimage[60] = new Tile("mentokbawah", false);
            tileimage[60].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/mentokbawah.png")); // Gambar mentok bawah

            // Carpet
            tileimage[61] = new Tile("karpetpojokkiri", true); // walkable = true
            tileimage[61].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetpojokkiri.png"));
            tileimage[62] = new Tile("karpetkiri", true); // walkable = true
            tileimage[62].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetkiri.png"));
            tileimage[63] = new Tile("karpetatas", true); // walkable = true
            tileimage[63].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetatas.png"));
            tileimage[64] = new Tile("karpetpojokkanan", true); // walkable = true
            tileimage[64].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetpojokkanan.png"));
            tileimage[65] = new Tile("karpetkanan", true); // walkable = true
            tileimage[65].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetkanan.png"));
            tileimage[66] = new Tile("belokkanan", true); // walkable = true
            tileimage[66].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/belokkanan.png"));
            tileimage[67] = new Tile("karpetmentok", true); // walkable = true
            tileimage[67].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetmentok.png"));
            tileimage[68] = new Tile("karpetbawah", true); // walkable = true
            tileimage[68].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetbawah.png"));
            tileimage[69] = new Tile("karpetpojokiribawah", true); // walkable = true
            tileimage[69].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpetpojokiribawah.png"));
            tileimage[70] = new Tile("belokhehe", true); // walkable = true
            tileimage[70].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/belokhehe.png"));
            tileimage[71] = new Tile("karpet", true); // walkable = true
            tileimage[71].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/karpet.png"));


            // Furnitures

            tileimage[72] = new Bed("Bed", false, "king"); // walkable = true
            tileimage[72].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed1.png")); // Gambar tempat tidur
            tileimage[73] = new Bed("Bed", false, "king"); // walkable = true
            tileimage[73].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed2.png")); // Gambar tempat tidur
            tileimage[74] = new Bed("Bed", false, "king"); // walkable = true
            tileimage[74].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed3.png")); // Gambar tempat tidur
            tileimage[75] = new Bed("Bed", false, "king"); // walkable = true
            tileimage[75].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed4.png")); // Gambar tempat tidur
            tileimage[76] = new Bed("Bed", false, "king"); // walkable = true
            tileimage[76].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed5.png")); // Gambar tempat tidur
            tileimage[77] = new Bed("Bed", false, "king"); // walkable = true
            tileimage[77].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed6.png")); // Gambar tempat tidur
            tileimage[78] = new Bed("Bed", false, "king"); // walkable = true
            tileimage[78].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed7.png")); // Gambar tempat tidur
            tileimage[79] = new Bed("Bed", false, "king"); // walkable = true
            tileimage[79].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed8.png")); // Gambar tempat tidur
            tileimage[80] = new Bed("Bed", false, "king"); // walkable = true
            tileimage[80].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/bed9.png")); // Gambar tempat tidur
            tileimage[81] = new Stove("stove"); // walkable = true
            tileimage[81].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/stove.png")); // Gambar kompor
            tileimage[82] = new Tile("tv", false); // walkable = true
            tileimage[82].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/house/TV.png")); // Gambar TV


            // WATER
            tileimage[9] = new Tile("Water", false); // walkable = false
            tileimage[9].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/water.png"));

            // DIRT/SOIL (Tanah kosong yang bisa ditanami)
            // Menggunakan constructor Soil(String name, boolean isWalkable, String emptySoilImagePath)
            tileimage[10] = new Soil("soil", true, "/Map/tiles/dirt.png"); // walkable = true
            // Gambar untuk Soil sudah di-load di dalam konstruktor Soil jika emptySoilImagePath disediakan

            // Door
            // Untuk Building, Anda perlu membuat kelas Building dengan constructor dan copy constructor yang sesuai
            // Untuk sementara, jika Building belum ada, bisa dibuat sebagai Tile biasa atau null
            if (classExists("Map.Building")) { // Cek jika kelas Building ada (Anda perlu helper method ini)
                 // tileimage[12] = new Building("Door", false, ...); // Sesuaikan dengan constructor Building Anda
                 // tileimage[12].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/door.png"));
                 // Untuk sekarang, fallback ke Tile biasa jika Building belum siap:
                 tileimage[12] = new Tile("Door Visual", false);
                 tileimage[12].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/dirt.png")); // Ganti gambar pintu
            } else {
                 tileimage[12] = new Tile("Door Placeholder", false);
                 tileimage[12].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/dirt.png")); // Ganti gambar pintu
            }


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
        if (prototypeID < 0 || prototypeID >= tileimage.length || tileimage[prototypeID] == null) {
            System.err.println("Warning: Invalid prototypeID " + prototypeID + " in createTileInstance. Using default tile 0.");
            prototypeID = 0;
            if (tileimage[prototypeID] == null) {
                 Tile errorTile = new Tile("Error Tile", false); // isWalkable = false
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

        Tile prototype = tileimage[prototypeID];
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