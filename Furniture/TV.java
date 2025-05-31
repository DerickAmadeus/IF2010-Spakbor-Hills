package Furniture;

import java.awt.Graphics2D;

import main.GamePanel;

public class TV extends Furniture{
    public TV(String name, Boolean isWalkable) {
        super(name, false);
    }

    public TV(TV other) {
        super(other);
    }
    public void screen(Graphics2D g2, GamePanel gp) {
        int margin = 60;
        int screenX = margin;
        int screenY = margin;
        int screenWidth = gp.screenWidth - 2 * margin;
        int screenHeight = gp.screenHeight - 2 * margin;

        g2.setColor(java.awt.Color.BLACK);
        g2.fillRect(screenX, screenY, screenWidth, screenHeight);

        String weather = gp.currentWeather;

        if (weather.equals("Rainy")) {
            g2.setColor(new java.awt.Color(40, 40, 70));
            g2.fillRect(screenX + 10, screenY + 10, screenWidth - 20, screenHeight - 20);

            g2.setColor(java.awt.Color.LIGHT_GRAY);
            int cloudBaseX = screenX + 175;
            int cloudBaseY = screenY + 120;
            g2.fillOval(cloudBaseX, cloudBaseY, 200, 70);
            g2.fillOval(cloudBaseX + 60, cloudBaseY - 20, 200, 80);
            g2.fillOval(cloudBaseX + 120, cloudBaseY, 180, 70);

            g2.setColor(java.awt.Color.CYAN);
            int rainCols = 10;
            int rainRows = 3;
            int startX = screenX + 175 + 60;
            int startY = screenY + 200;
            int spacingX = 20;
            int spacingY = 50;

            for (int row = 0; row < rainRows; row++) {
                for (int col = 0; col < rainCols; col++) {
                    int rx = startX + col * spacingX;
                    int ry = startY + row * spacingY;
                    g2.drawLine(rx, ry, rx, ry + 25); 
                }
            }

            g2.setColor(java.awt.Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(28f));
            g2.drawString("Weather: Rainy", screenX + 40, screenY + screenHeight - 40);
        } else if (weather.equals("Sunny")) {
            g2.setColor(new java.awt.Color(135, 206, 235));
            g2.fillRect(screenX + 10, screenY + 10, screenWidth - 20, screenHeight - 20);

            int sunCenterX = screenX + screenWidth / 2;
            int sunCenterY = screenY + screenHeight / 2 - 30;
            int sunRadius = 60;

            g2.setColor(java.awt.Color.YELLOW);
            for (int i = 0; i < 12; i++) {
                double angle = Math.toRadians(i * 30);
                int x1 = (int)(sunCenterX + Math.cos(angle) * (sunRadius + 10));
                int y1 = (int)(sunCenterY + Math.sin(angle) * (sunRadius + 10));
                int x2 = (int)(sunCenterX + Math.cos(angle) * (sunRadius + 30));
                int y2 = (int)(sunCenterY + Math.sin(angle) * (sunRadius + 30));
                g2.drawLine(x1, y1, x2, y2);
            }

            g2.fillOval(sunCenterX - sunRadius, sunCenterY - sunRadius, sunRadius * 2, sunRadius * 2);

            g2.setColor(java.awt.Color.BLACK);
            g2.setFont(g2.getFont().deriveFont(28f));
            g2.drawString("Weather: Sunny", screenX + 40, screenY + screenHeight - 40);
        }
    }
    @Override
    public void Action() {
        System.out.println("TV is being watched");
    }
}