package Map;

import main.GamePanel;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Map {
    GamePanel gp;
    public Tile[] tileimage; // Assuming you have 10 different tiles
    public int tiles[][];

    public Map(GamePanel gp) {
        // this.width = width;
        // this.height = height;
        this.tileimage = new Tile[100]; // Initialize tile images array
        this.tiles = new int[gp.worldCol][gp.worldRow]; // Initialize tiles array
        this.gp = gp; // Initialize GamePanel if needed 
        tileimage = new Tile[100]; // Initialize tile images array
        getTileImage(); // Load tile images
        loadMap();
    }

    
    public void getTileImage() {
        try {
            // GRASS
            tileimage[0] = new Tile("Grass", '.', true);
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

public void loadMap() {
    try {
        InputStream is = getClass().getResourceAsStream("/Map/map.txt");
        if (is == null) {
            System.err.println("FATAL ERROR: File /Map/map.txt tidak ditemukan!");
            // Mungkin isi semua tiles dengan tile error atau default
            for (int r = 0; r < gp.worldRow; r++) {
                for (int c = 0; c < gp.worldCol; c++) {
                    tiles[c][r] = 0; // Default ke rumput jika map tidak ada
                }
            }
            return;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        for (int row = 0; row < gp.worldRow || row < gp.worldCol; row++) { // Loop untuk setiap baris peta
            String line = br.readLine();
            if (line == null) {
                System.err.println("Error: map.txt berakhir lebih awal pada baris ke-" + (row + 1) + ". Sisa peta akan diisi tile default (0).");
                // Isi sisa baris dengan tile default jika file terlalu pendek
                for (int rFiller = row; rFiller < gp.worldRow; rFiller++) {
                    for (int cFiller = 0; cFiller < gp.worldCol; cFiller++) {
                        tiles[cFiller][rFiller] = 0; // Tile default (misalnya rumput)
                    }
                }
                break; // Keluar dari loop baris
            }

            String[] numbers = line.split(" ");

            for (int col = 0; col < gp.worldCol; col++) { // Loop untuk setiap kolom dalam baris saat ini
                if (col < numbers.length) { // Pastikan ada cukup angka di baris ini
                    try {
                        tiles[col][row] = Integer.parseInt(numbers[col].trim()); // .trim() untuk menghapus spasi ekstra
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing angka di map.txt pada baris " + (row + 1) + ", kolom " + (col + 1) + ". Nilai: '" + numbers[col] + "'. Menggunakan tile default (0).");
                        tiles[col][row] = 0; // Tile default jika ada error parsing
                    }
                } else {
                    // Jika baris di map.txt lebih pendek dari gp.worldCol
                    System.err.println("Peringatan: Baris " + (row + 1) + " di map.txt lebih pendek dari lebar peta. Kolom " + (col + 1) + " dst. diisi tile default (0).");
                    tiles[col][row] = 0; // Tile default
                }
            }
        }
        br.close();
    } catch (IOException e) { // Lebih spesifik untuk error I/O
        System.err.println("IOException saat memuat peta: " + e.getMessage());
        e.printStackTrace();
    } catch (Exception e) { // Untuk error tak terduga lainnya
        System.err.println("Error tak terduga saat memuat peta: " + e.getMessage());
        e.printStackTrace();
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
