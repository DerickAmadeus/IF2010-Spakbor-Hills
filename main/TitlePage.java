package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class TitlePage {
    GamePanel gp;

    Font pressStart;

    public int commandNumber = 0;

    // TITLE BACKGROUND
    BufferedImage titleScreenBackground;

    // //Game State
    // public final int titleState = 0;

    public TitlePage(GamePanel gp){
        this.gp = gp;

        try{
            InputStream inputStream = getClass().getResourceAsStream("PressStart2PRegular.ttf");
            pressStart = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        }
        catch (FontFormatException | IOException e){
            System.out.println("Failed to load font: "+ e.getMessage());
            pressStart = new Font("Arial", Font.BOLD, 30);
        }
        loadTitleScreen();
    }
    public void loadTitleScreen(){
        try{
            InputStream inputStream = getClass().getResourceAsStream("Summer1.png");

            if (inputStream != null){
                titleScreenBackground = ImageIO.read(inputStream);
            }
            else{
                System.err.println("Title screen can't be load!");
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void draw(Graphics2D g2){
        drawTitleScreen(g2);
    }

    public void drawTitleScreen(Graphics2D g2){
        //Bg image
        if (titleScreenBackground != null){
            g2.drawImage(titleScreenBackground, 0, 0, gp.screenWidth, gp.screenHeight, null);
        }
        else{
            g2.setColor(new Color(7, 150, 255));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        }

        //Text
        g2.setFont(pressStart.deriveFont(Font.BOLD, 50F));
        String text = "SPAKBOR HILLS";
        int x = getX(text, g2);
        int y = gp.tileSize * 3;

        //Shadow
        g2.setColor(Color.black);
        g2.drawString(text, x + 5, y + 5);

        //Title
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        //Menu 
        g2.setFont(pressStart.deriveFont(Font.BOLD, 30F));

        //NEW GAME
        text = "NEW GAME";
        x = getX(text, g2);
        y += gp.tileSize * 4;
        g2.setColor(Color.black);
        g2.drawString(text, x + 5, y + 5);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        if (commandNumber == 0){
            g2.drawString(">", x - gp.tileSize, y);
            // if(gp.keyHandler.enterPressed){
            //     gp.gameState = gp.farmNameInputState;
            //     gp.keyHandler.enterPressed = false;
            // }
        }

        //LOAD GAME
        text = "LOAD GAME";
        x = getX(text, g2);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x + 5, y + 5);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        if (commandNumber == 1){
            g2.drawString(">", x - gp.tileSize, y);
        }

        //HELP
        text = "HELP";
        x = getX(text, g2);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x + 5, y + 5);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        if (commandNumber == 2){
            g2.drawString(">", x - gp.tileSize, y);
        }

        //QUIT  
        text = "QUIT";
        x = getX(text, g2);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x + 5, y + 5);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        if (commandNumber == 3){
            g2.drawString(">", x - gp.tileSize, y);
        }
    }

    public int getX(String text, Graphics2D g2){
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }
}
