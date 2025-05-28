package main;

import player.Player;
import Map.Tile;
import NPC.NPC;
import java.awt.Rectangle;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Player player) {

        // Pastikan peta dan tile-nya sudah dimuat
        if (gp.map == null || gp.map.currentMapTiles == null) {
            // System.err.println("CollisionChecker: Peta atau currentMapTiles belum dimuat!");
            player.collisionOn = true; // Anggap ada collision jika peta tidak tersedia
            return;
        }

        int playerLeftX = player.getX() + player.solidArea.x;
        int playerRightX = player.getX() + player.solidArea.x + player.solidArea.width;
        int playerTopY = player.getY() + player.solidArea.y;
        int playerBottomY = player.getY() + player.solidArea.y + player.solidArea.height;

        int currentTileColLeft = playerLeftX / gp.tileSize;
        int currentTileColRight = playerRightX / gp.tileSize;
        int currentTileRowTop = playerTopY / gp.tileSize;
        int currentTileRowBottom = playerBottomY / gp.tileSize;

        Tile tileNum1, tileNum2;

        // Gunakan dimensi peta yang aktif dari Map.java
        int numMapCols = gp.map.currentMapWorldCol;
        int numMapRows = gp.map.currentMapWorldRow;

        // Jika dimensi peta tidak valid (misalnya 0), anggap ada collision
        if (numMapCols <= 0 || numMapRows <= 0) {
            // System.err.println("CollisionChecker: Dimensi peta tidak valid!");
            player.collisionOn = true;
            return;
        }

        int targetTileRow;
        int targetTileCol;

        switch (player.direction) {
            case "up":
                targetTileRow = (playerTopY - player.speed) / gp.tileSize;

                // Pengecekan batas yang lebih aman
                if (targetTileRow < 0 || targetTileRow >= numMapRows ||
                    currentTileColLeft < 0 || currentTileColLeft >= numMapCols ||
                    currentTileColRight < 0 || currentTileColRight >= numMapCols) {
                    player.collisionOn = true;
                } else {
                    // Akses tile menggunakan currentMapTiles
                    tileNum1 = gp.map.currentMapTiles[currentTileColLeft][targetTileRow];
                    tileNum2 = gp.map.currentMapTiles[currentTileColRight][targetTileRow];
                    if ((tileNum1 != null && !tileNum1.isWalkable()) || (tileNum2 != null && !tileNum2.isWalkable())) {
                        player.collisionOn = true;
                    }
                }
                break;
            case "down":
                targetTileRow = (playerBottomY + player.speed) / gp.tileSize;

                if (targetTileRow >= numMapRows || targetTileRow < 0 ||
                    currentTileColLeft < 0 || currentTileColLeft >= numMapCols ||
                    currentTileColRight < 0 || currentTileColRight >= numMapCols) {
                    player.collisionOn = true;
                } else {
                    tileNum1 = gp.map.currentMapTiles[currentTileColLeft][targetTileRow];
                    tileNum2 = gp.map.currentMapTiles[currentTileColRight][targetTileRow];
                    if ((tileNum1 != null && !tileNum1.isWalkable()) || (tileNum2 != null && !tileNum2.isWalkable())) {
                        player.collisionOn = true;
                    }
                }
                break;
            case "left":
                targetTileCol = (playerLeftX - player.speed) / gp.tileSize;

                if (targetTileCol < 0 || targetTileCol >= numMapCols ||
                    currentTileRowTop < 0 || currentTileRowTop >= numMapRows ||
                    currentTileRowBottom < 0 || currentTileRowBottom >= numMapRows) {
                    player.collisionOn = true;
                } else {
                    tileNum1 = gp.map.currentMapTiles[targetTileCol][currentTileRowTop];
                    tileNum2 = gp.map.currentMapTiles[targetTileCol][currentTileRowBottom];
                    if ((tileNum1 != null && !tileNum1.isWalkable()) || (tileNum2 != null && !tileNum2.isWalkable())) {
                        player.collisionOn = true;
                    }
                }
                break;
            case "right":
                targetTileCol = (playerRightX + player.speed) / gp.tileSize;

                if (targetTileCol >= numMapCols || targetTileCol < 0 ||
                    currentTileRowTop < 0 || currentTileRowTop >= numMapRows ||
                    currentTileRowBottom < 0 || currentTileRowBottom >= numMapRows) {
                    player.collisionOn = true;
                } else {
                    tileNum1 = gp.map.currentMapTiles[targetTileCol][currentTileRowTop];
                    tileNum2 = gp.map.currentMapTiles[targetTileCol][currentTileRowBottom];
                    if ((tileNum1 != null && !tileNum1.isWalkable()) || (tileNum2 != null && !tileNum2.isWalkable())) {
                        player.collisionOn = true;
                    }
                }
                break;
        }
    }

    public void checkNPC(Player player) {
        // Jika tidak ada NPC di game, tidak perlu dicek
        if (gp.npcs == null || gp.npcs.length == 0) {
            return;
        }

        // Buat sebuah Rectangle untuk merepresentasikan area solid pemain
        // pada posisi berikutnya yang dituju.
        Rectangle playerNextSolidArea = new Rectangle();
        // Ambil posisi x, y awal dari area solid pemain saat ini
        playerNextSolidArea.x = player.getX() + player.solidArea.x;
        playerNextSolidArea.y = player.getY() + player.solidArea.y;
        // Lebar dan tinggi area solid pemain tetap
        playerNextSolidArea.width = player.solidArea.width;
        playerNextSolidArea.height = player.solidArea.height;

        // Sesuaikan posisi x atau y dari playerNextSolidArea berdasarkan arah dan kecepatan pemain
        // Ini adalah prediksi posisi area solid pemain JIKA pemain bergerak.
        switch (player.direction) {
            case "up":
                playerNextSolidArea.y -= player.speed;
                break;
            case "down":
                playerNextSolidArea.y += player.speed;
                break;
            case "left":
                playerNextSolidArea.x -= player.speed;
                break;
            case "right":
                playerNextSolidArea.x += player.speed;
                break;
        }

        // Iterasi melalui semua NPC yang ada di GamePanel
        for (NPC npc : gp.npcs) {
            if (npc != null) {
                // 1. Hanya periksa NPC yang berada di peta yang sama dengan pemain
                if (npc.getSpawnMapName() == player.getLocation()) {

                    // 2. Dapatkan area hitbox NPC dalam koordinat dunia.
                    // Asumsi: npc.worldX dan npc.worldY adalah posisi kiri-atas NPC,
                    // dan npc.hitbox.x serta npc.hitbox.y adalah offset dari posisi tersebut.
                    // Jika npc.hitbox.x dan npc.hitbox.y adalah 0 (seperti di implementasi NPC sebelumnya),
                    // maka npcWorldHitbox.x akan sama dengan npc.worldX, begitu juga dengan y.
                    Rectangle npcWorldHitbox = new Rectangle();
                    npcWorldHitbox.x = npc.worldX + npc.hitbox.x;
                    npcWorldHitbox.y = npc.worldY + npc.hitbox.y;
                    npcWorldHitbox.width = npc.hitbox.width;
                    npcWorldHitbox.height = npc.hitbox.height;

                    // 3. Periksa apakah area solid pemain di posisi berikutnya akan beririsan
                    // dengan hitbox NPC.
                    if (playerNextSolidArea.intersects(npcWorldHitbox)) {
                        player.collisionOn = true; // Set flag collision pemain menjadi true
                        // System.out.println("Player collision with NPC: " + npc.name); // Untuk debug
                        return; // Collision terdeteksi dengan satu NPC, tidak perlu cek NPC lain untuk gerakan ini
                    }
                }
            }
        }
        // Jika loop selesai tanpa menemukan collision dengan NPC manapun,
        // player.collisionOn tidak diubah di sini (tetap false jika belum di-set true oleh checkTile).
    }
}