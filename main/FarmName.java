package main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class FarmName {
    GamePanel gp;

    String farmNameInput = "";
    Font inputFarm;
    private final String farmMessage = "Enter your farm's name: ";
    private final int maxLength = 20;
    private final String back = "BACK";

    public int commandNumber = 0;

    BufferedImage farmInputBackground;

    public FarmName(GamePanel gp){
        this.gp = gp;
        try{
            InputStream inputStream = getClass().getResourceAsStream("PressStart2PRegular.ttf");
            inputFarm = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        }
        catch (FontFormatException | IOException e){
            System.out.println("Failed to load font: "+ e.getMessage());
            inputFarm = new Font("Arial", Font.BOLD, 30);
        }

        loadFarmInputScreen();
    }

    public void loadFarmInputScreen(){
        try{
            InputStream inputStream = getClass().getResourceAsStream("Summer1.png");

            if (inputStream != null){
                farmInputBackground = ImageIO.read(inputStream);
            }
            else{
                System.err.println("Farm input screen can't be load!");
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void reset() {
        farmNameInput = "";
        commandNumber = 0;
    }

    public void draw(Graphics2D g2){
        // taro bg disini
        if (farmInputBackground != null){
            g2.drawImage(farmInputBackground, 0, 0, gp.screenWidth, gp.screenHeight, null);
        }
        else{
            g2.setColor(new Color(7, 150, 255));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        }

        //overlay biar agak gelap
        g2.setColor(new Color(0,0,0,160));      
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        // farm message
        g2.setFont(inputFarm.deriveFont(Font.BOLD, 24F));
        g2.setColor(Color.white);
        int x = getX(farmMessage, g2);
        int y = gp.screenHeight / 2 - gp.tileSize;
        g2.drawString(farmMessage, x, y);

        // input
        String displayText = farmNameInput;
        if ((System.currentTimeMillis() / 500) % 2 == 0){ // kursos blink tiap 0.5 detik
            displayText += "_";
        }

        g2.setFont(inputFarm.deriveFont(Font.PLAIN, 18F));
        x = getX(displayText, g2);
        y += gp.tileSize * 2;
        g2.drawString(displayText, x, y);

        //back
        g2.setFont(inputFarm.deriveFont(Font.BOLD, 30F));
        // g2.setColor(Color.white);
        g2.setColor(commandNumber == 1 ? Color.yellow : Color.white);
        int margin = 20;
        int backY  = gp.screenHeight - margin;
        int backX  = margin + (commandNumber==1 ? gp.tileSize : 0);

        // shadow
        g2.setColor(Color.black);
        g2.drawString(back, backX + 5, backY + 5);
        g2.setColor(Color.white);
        
        if (commandNumber == 1){
            g2.setColor(Color.yellow);
            g2.drawString(">", margin, backY);
        }

        g2.drawString(back, backX, backY);
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        char ch = e.getKeyChar();

        if (keyCode == KeyEvent.VK_ESCAPE) { // ESC auto back
            gp.gameState = gp.titleState; 
            gp.titlePage.commandNumber = 0;
            return;
        }
        else if (keyCode == KeyEvent.VK_ENTER) {
            if (commandNumber == 0 && !farmNameInput.trim().isEmpty()) {
                gp.player.setFarmName(farmNameInput.trim());
                gp.gameState = gp.playerNameInputState;  
                gp.keyHandler.enterPressed = false;
            }

            else if(commandNumber == 1){
                gp.gameState = gp.titleState;
                gp.keyHandler.enterPressed = false;
            }
        } else if (keyCode == KeyEvent.VK_TAB){
            commandNumber = (commandNumber == 0) ? 1 : 0;
            gp.keyHandler.upPressed = false;
            gp.keyHandler.downPressed = false;
        } else if (keyCode == KeyEvent.VK_BACK_SPACE) {
            if (!farmNameInput.isEmpty()) {
                farmNameInput = farmNameInput.substring(0, farmNameInput.length() - 1);
            }
        } else {
            if (commandNumber == 0 && farmNameInput.length() < maxLength) {
                if (ch >= 32 && ch <= 126) {
                    farmNameInput += ch;
                }
            }
        }
    }

    public int getX(String text, Graphics2D g2){
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }
}
