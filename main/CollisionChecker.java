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

        if (gp.map == null || gp.map.currentMapTiles == null) {
            player.collisionOn = true; 
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

        int numMapCols = gp.map.currentMapWorldCol;
        int numMapRows = gp.map.currentMapWorldRow;

        if (numMapCols <= 0 || numMapRows <= 0) {
            player.collisionOn = true;
            return;
        }

        int targetTileRow;
        int targetTileCol;

        switch (player.direction) {
            case "up":
                targetTileRow = (playerTopY - player.speed) / gp.tileSize;

                if (targetTileRow < 0 || targetTileRow >= numMapRows ||
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
        if (gp.npcs == null || gp.npcs.length == 0) {
            return;
        }
        Rectangle playerNextSolidArea = new Rectangle();
        playerNextSolidArea.x = player.getX() + player.solidArea.x;
        playerNextSolidArea.y = player.getY() + player.solidArea.y;
        playerNextSolidArea.width = player.solidArea.width;
        playerNextSolidArea.height = player.solidArea.height;
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

        for (NPC npc : gp.npcs) {
            if (npc != null) {
                if (npc.getSpawnMapName() == player.getLocation()) {
                    Rectangle npcWorldHitbox = new Rectangle();
                    npcWorldHitbox.x = npc.worldX + npc.hitbox.x;
                    npcWorldHitbox.y = npc.worldY + npc.hitbox.y;
                    npcWorldHitbox.width = npc.hitbox.width;
                    npcWorldHitbox.height = npc.hitbox.height;
                    if (playerNextSolidArea.intersects(npcWorldHitbox)) {
                        player.collisionOn = true; 
                        return; 
                    }
                }
            }
        }
    }
}