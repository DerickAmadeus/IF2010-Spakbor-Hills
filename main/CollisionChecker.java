package main;

import player.Player;

public class CollisionChecker {

	GamePanel gp;

	public CollisionChecker(GamePanel gp) {
		this.gp = gp;
	}

	public void checkTile(Player player) {

		int playerLeftX = player.getX() + player.solidArea.x;
		int playerRightX = player.getX() + player.solidArea.x + player.solidArea.width;
		int playerTopY = player.getY() + player.solidArea.y;
		int playerBottomY = player.getY() + player.solidArea.y + player.solidArea.height;

		int playerColLeft = playerLeftX / gp.tileSize;
		int playerColRight = playerRightX / gp.tileSize;
		int playerRowTop = playerTopY / gp.tileSize;
		int playerRowBottom = playerBottomY / gp.tileSize;

		int tileNum1, tileNum2;

		switch (player.direction) {
			case "up":
				playerRowTop = (playerTopY - player.speed) / gp.tileSize;
				tileNum1 = gp.map.tiles[playerColLeft][playerRowTop];
				tileNum2 = gp.map.tiles[playerColRight][playerRowTop];
				if (gp.map.tileimage[tileNum1].isWalkable() == false || gp.map.tileimage[tileNum2].isWalkable() == false) {
					player.collisionOn = true;
				}
				break;
			case "down":
				playerRowBottom = (playerBottomY + player.speed) / gp.tileSize;
				tileNum1 = gp.map.tiles[playerColLeft][playerRowBottom];
				tileNum2 = gp.map.tiles[playerColRight][playerRowBottom];
				if (gp.map.tileimage[tileNum1].isWalkable() == false || gp.map.tileimage[tileNum2].isWalkable() == false) {
					player.collisionOn = true;
				}
				break;

			case "left" : 
				playerColLeft = (playerLeftX - player.speed) / gp.tileSize;
				tileNum1 = gp.map.tiles[playerColLeft][playerRowTop];
				tileNum2 = gp.map.tiles[playerColLeft][playerRowBottom];
				if (gp.map.tileimage[tileNum1].isWalkable() == false || gp.map.tileimage[tileNum2].isWalkable() == false) {
					player.collisionOn = true;
				}
				break;

			case "right" :
				playerColRight = (playerRightX + player.speed) / gp.tileSize;
				tileNum1 = gp.map.tiles[playerColRight][playerRowTop];
				tileNum2 = gp.map.tiles[playerColRight][playerRowBottom];
				if (gp.map.tileimage[tileNum1].isWalkable() == false || gp.map.tileimage[tileNum2].isWalkable() == false) {
					player.collisionOn = true;
				}
				break;
		}

	}

}