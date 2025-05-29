package main;

import player.Player;
import Map.Tile;

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
}