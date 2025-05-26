package main;

import player.Player;
import Map.Tile;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Player player) {

        // Get the pixel coordinates of the player's solid area edges
        int playerLeftX = player.getX() + player.solidArea.x;
        int playerRightX = player.getX() + player.solidArea.x + player.solidArea.width;
        int playerTopY = player.getY() + player.solidArea.y;
        int playerBottomY = player.getY() + player.solidArea.y + player.solidArea.height;

        // Calculate the tile indices the player's solid area currently occupies
        int currentTileColLeft = playerLeftX / gp.tileSize;
        int currentTileColRight = playerRightX / gp.tileSize;
        int currentTileRowTop = playerTopY / gp.tileSize;
        int currentTileRowBottom = playerBottomY / gp.tileSize;

        Tile tileNum1, tileNum2;

        // Get current map dimensions
        // Pastikan currentMapTiles sudah diinisialisasi dan tidak null
        if (gp.map.currentMapTiles == null || gp.map.currentMapTiles.length == 0) {
            // Handle case where map tiles are not loaded, perhaps set collisionOn to true
            // or log an error. For now, we'll prevent NullPointerException.
            player.collisionOn = true; // Prevent movement if map data is unavailable
            // System.err.println("CollisionChecker: currentMapTiles is null or empty. Defaulting to collision.");
            return;
        }
        int numMapCols = gp.map.currentMapWorldCol; // Menggunakan dimensi peta saat ini
        int numMapRows = gp.map.currentMapWorldRow; // Menggunakan dimensi peta saat ini


        int targetTileRow;
        int targetTileCol;

        switch (player.direction) {
            case "up":
                targetTileRow = (playerTopY - player.speed) / gp.tileSize;

                // Boundary and null checks for tile access
                if (targetTileRow < 0 || targetTileRow >= numMapRows || // Check target row against map bounds
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
            case "down":
                targetTileRow = (playerBottomY + player.speed) / gp.tileSize;

                if (targetTileRow >= numMapRows || targetTileRow < 0 || // Check target row against map bounds
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

                if (targetTileCol < 0 || targetTileCol >= numMapCols || // Check target col against map bounds
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

                if (targetTileCol >= numMapCols || targetTileCol < 0 || // Check target col against map bounds
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