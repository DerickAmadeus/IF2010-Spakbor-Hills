package main;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import javax.imageio.ImageIO;

public class Credits {
    GamePanel gp;
    
    Font creditsFont;
    
    private int commandNumber = 0;
    private final String title = "CREDITS";
    private final String back = "BACK";

    BufferedImage credsBackground;

    private final String[] creditsLines = {
        "Created by:",
        "18223064 Ahmad Evander Ruichi Xavier",
        "18223090 Derick Amadeus Budiono",
        "18223080 Michael Ballard",
        "18223092 Gabriela Jennifer Sandy",
    };

    private final String copyright = "Institut Teknologi Bandung © 2025";

    public Credits(GamePanel gp) {
        this.gp = gp;
        loadFont();
        loadCredsScreen();
    }

    private void loadFont() {
        try {
            creditsFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("PressStart2PRegular.ttf"));
        } catch (Exception e) {
            creditsFont = new Font("Arial", Font.PLAIN, 20);
        }
    }

    public void loadCredsScreen(){
        try{
            InputStream inputStream = getClass().getResourceAsStream("Summer1.png");

            if (inputStream != null){
                credsBackground = ImageIO.read(inputStream);
            }
            else{
                System.err.println("Farm input screen can't be load!");
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void draw(Graphics2D g2) {
        // bg 
        if (credsBackground != null){
            g2.drawImage(credsBackground, 0, 0, gp.screenWidth, gp.screenHeight, null);
        }
        else{
            g2.setColor(new Color(7, 150, 255));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        }

        //overlay biar agak gelap
        g2.setColor(new Color(0,0,0,160));      
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        // title
        g2.setFont(creditsFont.deriveFont(Font.BOLD, 36f));
        g2.setColor(new Color(255, 215, 0));
        g2.drawString(title, getX(title, g2), gp.tileSize * 2);

        // credits list
        g2.setFont(creditsFont.deriveFont(Font.PLAIN, 14f));
        g2.setColor(Color.WHITE);
        
        int startY = gp.tileSize * 4;
        int lineHeight = 30;
        for (int i = 0; i < creditsLines.length; i++) {
            String line = creditsLines[i];
            int x = getX(line, g2);
            int y = startY + i * lineHeight;
            g2.drawString(line, x, y);
        }
        
        // copyright hehe
        g2.setFont(creditsFont.deriveFont(Font.PLAIN, 10f));
        g2.setColor(new Color(200, 200, 200)); // Light gray
        
        int x = getX(copyright, g2);
        int y = gp.screenHeight - gp.tileSize * 2; 
        g2.drawString(copyright, x, y);

        // back button
        g2.setFont(creditsFont.deriveFont(Font.BOLD, 28F));
        int margin = 20;
        int backY  = gp.screenHeight - margin;
        int backX  = margin + (commandNumber == 0 ? gp.tileSize : 0);

        g2.setColor(Color.BLACK);
        g2.drawString(back, backX + 5, backY + 5);
        g2.setColor(commandNumber == 0 ? Color.yellow : Color.WHITE);
        
        if (commandNumber == 0){
            g2.setColor(Color.yellow);
            g2.drawString(">", margin, backY);
        }

        g2.drawString(back, backX, backY);
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_ESCAPE){          // ESC auto back
                gp.gameState = gp.titleState;
                return;
        } else if (keyCode == KeyEvent.VK_TAB){
            commandNumber = (commandNumber == 0) ? 1 : 0;
            gp.keyHandler.upPressed = false;
            gp.keyHandler.downPressed = false;
        } else if (keyCode == KeyEvent.VK_ENTER) {
            if (commandNumber == 0) {
                gp.gameState = gp.titleState;
                gp.keyHandler.enterPressed = false;
            }
        }
    }

    public int getX(String text, Graphics2D g2){
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }
}
