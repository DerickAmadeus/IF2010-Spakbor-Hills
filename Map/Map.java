package Map;

import main.GamePanel;
import javax.imageio.ImageIO;

import Items.Seeds;

import java.awt.Graphics2D;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Map {
    GamePanel gp;
    public Tile[] tileimage;
    public int tiles[][]; // Data tile untuk peta saat ini

    public int currentMapWorldCol;
    public int currentMapWorldRow;


    public int currentMapID = 0; // ID peta yang sedang aktif
    public String[] mapFilePaths = {
            "/Map/maps/farm_map.txt",   
            "/Map/maps/forest_map.txt", 
            "/Map/maps/mountain_lake_map.txt", 
            // ... tambahkan semua path file peta kamu di sini
    };

    public Map(GamePanel gp) {
        // this.width = width;
        // this.height = height;
        this.tileimage = new Tile[100]; // Initialize tile images array
        this.tiles = new int[gp.worldCol][gp.worldRow]; // Initialize tiles array
        this.gp = gp; // Initialize GamePanel if needed 
        getTileImage(); // Load tile images
        loadMap();
    }

    
    public void getTileImage() {
        try {
            // GRASS
            tileimage[0] = new Tile("grass", '.', true);
            tileimage[0].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grass.png"));

            tileimage[1] = new Tile("grass", '.', true);
            tileimage[1].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskiriatas.png"));

            tileimage[2] = new Tile("grass", '.', true);
            tileimage[2].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grassatas.png"));

            tileimage[3] = new Tile("grass", '.', true);
            tileimage[3].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskiri.png"));

            tileimage[4] = new Tile("grass", '.', true);
            tileimage[4].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskiribawah.png"));

            tileimage[5] = new Tile("grass", '.', true);
            tileimage[5].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grassbawah.png"));

            tileimage[6] = new Tile("grass", '.', true);
            tileimage[6].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskanan.png"));

            tileimage[7] = new Tile("grass", '.', true);       
            tileimage[7].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskananatas.png"));

            tileimage[8] = new Tile("grass", '.', true);
            tileimage[8].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grasskananbawah.png"));

            tileimage[11] = new Tile("grass", '.', true);
            tileimage[11].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/grass/grassatasair.png"));


            // // WATER

            tileimage[9] = new Tile("Water", 'W', false);
            tileimage[9].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/water.png"));

            // //DIRT

            tileimage[10] = new Soil(null);
            tileimage[10].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/dirt.png"));

            // Door
            tileimage[12] = new Building("Door", 'D', false, "House", 2, 4);
            tileimage[12].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/dirt.png")); //door blm ada

            // Planted Seeds
            tileimage[13] = new Soil(new Seeds("Parsnip Seeds", "Grows quickly in Spring", 10, 20, 1, "Spring",13));
            tileimage[13].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Parsnip Seeds.png"));

            tileimage[14] = new Soil(new Seeds("Cauliflower Seeds", "Takes time but very profitable", 40, 80, 5, "Spring",14));
            tileimage[14].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Cauliflower Seeds.png"));

            tileimage[15] = new Soil(new Seeds("Potato Seeds", "Decent crop with good value", 25, 50, 3, "Spring",15));
            tileimage[15].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Potato Seeds.png"));

            tileimage[16] = new Soil(new Seeds("Wheat Seeds", "Grows fast in multiple seasons", 30, 60, 1, "Spring",16));
            tileimage[16].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Wheat Seeds.png"));

            tileimage[17] = new Soil(new Seeds("Blueberry Seeds", "Highly productive summer crop", 40, 80, 7, "Summer",17));
            tileimage[17].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Blueberry Seeds.png"));

            tileimage[18] = new Soil(new Seeds("Tomato Seeds", "Summer favorite", 25, 50, 3, "Summer",18));
            tileimage[18].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Tomato Seeds.png"));

            tileimage[19] = new Soil(new Seeds("Hot Pepper Seeds", "Spicy and grows quickly", 20, 40, 1, "Summer",19));
            tileimage[19].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Hot Pepper Seeds.png"));

            tileimage[20] = new Soil(new Seeds("Melon Seeds", "Juicy summer crop", 40, 80, 4, "Summer",20));
            tileimage[20].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Melon Seeds.png"));

            tileimage[21] = new Soil(new Seeds("Cranberry Seeds", "Profitable fall crop", 50, 100, 2, "Fall",21));
            tileimage[21].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Cranberry Seeds.png"));

            tileimage[22] = new Soil(new Seeds("Pumpkin Seeds", "Fall favorite with high value", 75, 150, 7, "Fall",22));
            tileimage[22].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Pumpkin Seeds.png"));

            tileimage[23] = new Soil(new Seeds("Wheat Seeds", "Also grows well in fall", 30, 60, 1, "Fall",23));
            tileimage[23].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Wheat Seeds.png"));

            tileimage[24] = new Soil(new Seeds("Grape Seeds", "Climbing fall crop", 30, 60, 3, "Fall",24));
            tileimage[24].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/seeds/Planted Grape Seeds.png")); 

        } catch (Exception e) {
            System.out.println("Error loading tile images: " + e.getMessage());
        }
    }

    private void loadMapByPath(String mapFilePath) { // Perhatikan: 'private' jika hanya dipanggil dari loadMapByID
        try {
            InputStream is = getClass().getResourceAsStream(mapFilePath);
            if (is == null) {
                System.err.println("FATAL ERROR: File " + mapFilePath + " tidak ditemukan!");
                createEmptyMap(gp.maxScreenCol, gp.maxScreenRow); // Gunakan dimensi default GP jika file tidak ada
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            ArrayList<String> lines = new ArrayList<>();
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                lines.add(currentLine);
            }
            br.close();

            if (lines.isEmpty()) {
                System.err.println("Map file is empty: " + mapFilePath);
                createEmptyMap(gp.maxScreenCol, gp.maxScreenRow);
                return;
            }

            this.currentMapWorldRow = lines.size();
            if (this.currentMapWorldRow > 0 && lines.get(0) != null) {
                this.currentMapWorldCol = lines.get(0).split(" ").length;
            } else {
                this.currentMapWorldCol = 0;
            }

            this.tiles = new int[this.currentMapWorldCol][this.currentMapWorldRow];

            for (int row = 0; row < this.currentMapWorldRow; row++) {
                String lineData = lines.get(row);
                String[] numbers = lineData.split(" ");
                for (int col = 0; col < this.currentMapWorldCol; col++) {
                    if (col < numbers.length) {
                        try {
                            tiles[col][row] = Integer.parseInt(numbers[col].trim());
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing number in " + mapFilePath + " at row " + (row + 1) + ", col " + (col + 1) + ". Val: '" + numbers[col] + "'. Using 0.");
                            tiles[col][row] = 0;
                        }
                    } else {
                        tiles[col][row] = 0;
                    }
                }
            }
            System.out.println("Map loaded: " + mapFilePath + " Dimensions: " + this.currentMapWorldCol + "x" + this.currentMapWorldRow);

        } catch (IOException e) {
            System.err.println("IOException loading map " + mapFilePath + ": " + e.getMessage());
            e.printStackTrace();
            createEmptyMap(gp.maxScreenCol, gp.maxScreenRow);
        } catch (Exception e) {
            System.err.println("Unexpected error loading map " + mapFilePath + ": " + e.getMessage());
            e.printStackTrace();
            createEmptyMap(gp.maxScreenCol, gp.maxScreenRow);
        }
    }

    public void draw (Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.worldCol && worldRow < gp.worldRow) {
            int tileNum = tiles[worldCol][worldRow]; // Get the tile number from the array

            int worldX = worldCol * gp.tileSize; // Calculate the X position of the tile
            int worldY = worldRow * gp.tileSize; // Calculate the Y position of the tile
            int screenX = worldX - gp.player.x + gp.player.screenX; // Calculate the screen X position
            int screenY = worldY - gp.player.y + gp.player.screenY; // Calculate the screen Y position


            g2.drawImage(tileimage[tileNum].Image, screenX, screenY, gp.tileSize, gp.tileSize, null); // Draw grass tile
            worldCol ++;

            if (worldCol == gp.worldCol) { // If reached the end of the worldRow
                worldCol = 0; // Reset column index
                worldRow ++; // Move to the next row
            }
        }
    }

    public boolean loadMapByID(int mapID) {
        if (mapID >= 0 && mapID < mapFilePaths.length && mapFilePaths[mapID] != null) {
            loadMapByPath(mapFilePaths[mapID]); // Panggil metode yang lama dengan path yang benar
            this.currentMapID = mapID; // Update ID peta saat ini
            System.out.println("Successfully switched to map ID: " + mapID);
            return true;
        } else {
            System.err.println("Error: Invalid mapID (" + mapID + ") or map file path not configured.");
            // Fallback: coba load peta default (ID 0) jika ada error
            if (mapFilePaths.length > 0 && mapFilePaths[0] != null) {
                loadMapByPath(mapFilePaths[0]);
                this.currentMapID = 0;
                System.err.println("Reverted to default map ID: 0");
            } else {
                // Jika peta default pun tidak ada, buat peta kosong
                createEmptyMap(gp.maxScreenCol, gp.maxScreenRow); // Sesuaikan ukuran default
            }
            return false;
        }
    }


    private void createEmptyMap(int cols, int rows) {
        // ... (isi method createEmptyMap seperti yang sudah kamu buat/modifikasi sebelumnya) ...
        this.currentMapWorldCol = cols;
        this.currentMapWorldRow = rows;
        this.tiles = new int[this.currentMapWorldCol][this.currentMapWorldRow];
        for (int r = 0; r < this.currentMapWorldRow; r++) {
            for (int c = 0; c < this.currentMapWorldCol; c++) {
                this.tiles[c][r] = 0;
            }
        }
        System.out.println("Created/Reverted to empty map (" + cols + "x" + rows + ")");
    }


    public Tile getTile(int x, int y) {
        int col = x / gp.tileSize;
        int row = y / gp.tileSize;

        if (col >= 0 && col < gp.worldCol && row >= 0 && row < gp.worldRow) {
            return tileimage[tiles[col][row]];
        } else {
            return null; // Out of bounds
        }
    }

    public void setTile(int x, int y, int tileID) {
        int col = x / gp.tileSize;
        int row = y / gp.tileSize;

        if (col >= 0 && col < gp.worldCol && row >= 0 && row < gp.worldRow) {
            tiles[col][row] = tileID;
        }
    }

    public void plantSeedAtTile(int worldX, int worldY, Seeds seedToPlant) {
        int col = worldX / gp.tileSize;
        int row = worldY / gp.tileSize;

        // Asumsi gp.worldCol/Row adalah dimensi peta saat ini yang benar dari GamePanel
        // Jika Map.java sudah jadi MapManager, gunakan this.currentMapWorldCol/Row
        if (col >= 0 && col < gp.worldCol && row >= 0 && row < gp.worldRow) {
            int currentTileGridID = this.tiles[col][row];
            // Tile currentTilePrototype = this.tileimage[currentTileGridID]; // Tidak perlu ambil object prototype-nya jika hanya mau cek ID

            // Hanya boleh menanam di tile yang merupakan "Empty Soil" (misal, ID 10)
            if (currentTileGridID == 10) { // ID 10 adalah prototype Soil(null) kamu
                
                // Langsung dapatkan ID tile baru untuk bibit yang ditanam
                int newPlantedTileID = seedToPlant.getTileIndex(); // Dari Seeds.java

                if (newPlantedTileID != -1 && newPlantedTileID != currentTileGridID) { 
                    this.tiles[col][row] = newPlantedTileID; // GANTI ID TILE DI GRID PETA
                    System.out.println(seedToPlant.getName() + " planted at (" + col + "," + row + "). New Tile ID: " + newPlantedTileID);
                } else {
                    System.out.println("Cannot plant: Invalid new tile ID ("+ newPlantedTileID +") for " + seedToPlant.getName());
                }
            } else {
                // Jika bukan tanah kosong (ID 10), cek apakah tile ini punya bibit (untuk debug)
                Tile targetTile = this.tileimage[currentTileGridID];
                String plantedStatus = "is not Soil or unknown";
                if (targetTile instanceof Soil) {
                    Soil s = (Soil) targetTile;
                    if (s.getSeedPlanted() != null) {
                        plantedStatus = "is already planted with " + s.getSeedPlanted().getName();
                    } else {
                        // Ini bisa terjadi jika ID-nya bukan 10 tapi Soil(null), misal ID untuk tanah yang sudah dicangkul tapi belum ditanami.
                        // Untuk sekarang, kita anggap hanya ID 10 yang bisa ditanami dari awal.
                        plantedStatus = "is Soil but not the initial empty soil (ID 10) or its prototype is empty.";
                    }
                }
                System.out.println("Cannot plant: Tile at (" + col + "," + row + ") " + plantedStatus + ". Current Grid ID: " + currentTileGridID);
            }
        } else {
            System.out.println("Cannot plant: Coordinates (" + col + "," + row + ") are out of map bounds.");
        }
    }

}
