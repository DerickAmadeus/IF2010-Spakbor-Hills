package Map;

import main.GamePanel;
import javax.imageio.ImageIO;
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
        this.gp = gp;
        this.tileimage = new Tile[100]; // Asumsi maks 100 jenis tile
        getTileImage(); // Load semua jenis gambar tile (dilakukan sekali)

        // Langsung load peta awal berdasarkan currentMapID
        loadMapByID(this.currentMapID);
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

            tileimage[10] = new Soil();
            tileimage[10].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/dirt.png"));

            // Door
            tileimage[12] = new Building("Door", 'D', false, "House", 2, 4);
            tileimage[12].Image = ImageIO.read(getClass().getResourceAsStream("/Map/tiles/door.png"));



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
    
}
