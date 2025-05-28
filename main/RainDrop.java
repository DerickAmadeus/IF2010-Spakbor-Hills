package main;
import java.awt.*;

public class RainDrop {
    public int x, y, speed;
    public GamePanel gp;

    public RainDrop(int x, int y, int speed, GamePanel gp) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.gp = gp;
    }

    public void update() {
        y += speed;
        if (y > gp.screenHeight) {
            y = 0;
            x = (int)(Math.random() * gp.screenWidth);
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(new Color(173, 216, 230, 120)); // Light blue with transparency
        g2.drawLine(x, y, x, y + 10);
    }
}

