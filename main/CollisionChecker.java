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


        int currentTileColLeft = playerLeftX / gp.tileSize;
        int currentTileColRight = playerRightX / gp.tileSize;
        int currentTileRowTop = playerTopY / gp.tileSize;
        int currentTileRowBottom = playerBottomY / gp.tileSize;

        Tile tileNum1, tileNum2;


        int numMapCols = gp.map.tiles.length;
        int numMapRows = gp.map.tiles[0].length;

        // These variables will store the calculated TARGET tile index
        // for the edge of the player in the direction of movement.
        int targetTileRow;
        int targetTileCol;

        switch (player.direction) {
            case "up":
                targetTileRow = (playerTopY - player.speed) / gp.tileSize;


                if (targetTileRow < 0 ||
                    currentTileColLeft < 0 || currentTileColLeft >= numMapCols ||
                    currentTileColRight < 0 || currentTileColRight >= numMapCols) {
                    player.collisionOn = true;
                } else {
                    // If within bounds, check walkability of the two tiles the player's top edge would hit
                    tileNum1 = gp.map.tiles[currentTileColLeft][targetTileRow];
                    tileNum2 = gp.map.tiles[currentTileColRight][targetTileRow];
                    if ((tileNum1 != null && !tileNum1.isWalkable()) || (tileNum2 != null && !tileNum2.isWalkable())) {
                        player.collisionOn = true;
                    }
                }
                break;
            case "down":
                // Calculate the prospective tile row for the player's bottom edge after moving
                targetTileRow = (playerBottomY + player.speed) / gp.tileSize;

                if (targetTileRow >= numMapRows || // Fix: Check if target row is out of bounds (bottom)
                    currentTileColLeft < 0 || currentTileColLeft >= numMapCols ||
                    currentTileColRight < 0 || currentTileColRight >= numMapCols) {
                    player.collisionOn = true;
                } else {
                    // If within bounds, check walkability of the two tiles the player's bottom edge would hit
                    tileNum1 = gp.map.tiles[currentTileColLeft][targetTileRow]; // This was line 38
                    tileNum2 = gp.map.tiles[currentTileColRight][targetTileRow];
                    if ((tileNum1 != null && !tileNum1.isWalkable()) || (tileNum2 != null && !tileNum2.isWalkable())) {
                        player.collisionOn = true;
                    }
                }
                break;
            case "left":
                // Calculate the prospective tile column for the player's left edge after moving
                targetTileCol = (playerLeftX - player.speed) / gp.tileSize;


                if (targetTileCol < 0 ||
                    currentTileRowTop < 0 || currentTileRowTop >= numMapRows ||
                    currentTileRowBottom < 0 || currentTileRowBottom >= numMapRows) {
                    player.collisionOn = true;
                } else {
                    // If within bounds, check walkability of the two tiles the player's left edge would hit
                    tileNum1 = gp.map.tiles[targetTileCol][currentTileRowTop];
                    tileNum2 = gp.map.tiles[targetTileCol][currentTileRowBottom];
                    if ((tileNum1 != null && !tileNum1.isWalkable()) || (tileNum2 != null && !tileNum2.isWalkable())) {
                        player.collisionOn = true;
                    }
                }
                break;
            case "right":
                // Calculate the prospective tile column for the player's right edge after moving
                targetTileCol = (playerRightX + player.speed) / gp.tileSize;

                // Boundary checks:
                // 1. Is the target column to the right of the map?
                // 2. Are the current rows (top/bottom edges of player) valid?
                if (targetTileCol >= numMapCols || // Fix: Check if target col is out of bounds (right)
                    currentTileRowTop < 0 || currentTileRowTop >= numMapRows ||
                    currentTileRowBottom < 0 || currentTileRowBottom >= numMapRows) {
                    player.collisionOn = true;
                } else {
                    // If within bounds, check walkability of the two tiles the player's right edge would hit
                    tileNum1 = gp.map.tiles[targetTileCol][currentTileRowTop];
                    tileNum2 = gp.map.tiles[targetTileCol][currentTileRowBottom];
                    if ((tileNum1 != null && !tileNum1.isWalkable()) || (tileNum2 != null && !tileNum2.isWalkable())) {
                        player.collisionOn = true;
                    }
                }
                break;
        }
    }
}