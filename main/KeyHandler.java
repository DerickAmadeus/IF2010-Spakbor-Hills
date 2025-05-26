package main;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;


public class KeyHandler implements KeyListener{

    public boolean enterPressed, upPressed, downPressed, leftPressed, rightPressed, interactPressed, f1Pressed, invPressed; // Boolean flags for key states
    GamePanel gp; // Reference to the GamePanel

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }
    @Override
    public void keyTyped(KeyEvent e) {
        // Handle key typed event
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        // Input Title State
        if (gp.gameState == gp.titleState){
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP){
                gp.titlePage.commandNumber--;
                if (gp.titlePage.commandNumber < 0) gp.titlePage.commandNumber = 2;
            } else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
                gp.titlePage.commandNumber++;
                if (gp.titlePage.commandNumber > 2) gp.titlePage.commandNumber = 0;
            } else if (code == KeyEvent.VK_ENTER){
                enterPressed = true;         // diproses di GamePanel.update()
            }
            return;                          // abaikan input lain saat title
        }

        if (code == KeyEvent.VK_W) {
            upPressed = true; // Set upPressed to true when W key is pressed
            System.out.println("W key pressed"); // Move up
        } else if (code == KeyEvent.VK_A) {
            leftPressed = true; // Set leftPressed to true when A key is pressed
            System.out.println("A key pressed"); // Move left
        } else if (code == KeyEvent.VK_S) {
            downPressed = true; // Set downPressed to true when S key is pressed
            System.out.println("S key pressed"); // Move down
        } else if (code == KeyEvent.VK_D) {
            rightPressed = true; // Set rightPressed to true when D key is pressed
            System.out.println("D key pressed"); // Move right
        } else if (code == KeyEvent.VK_E) {
            interactPressed = true; // Set interactPressed to true when E key is pressed
        } else if (code == KeyEvent.VK_F1) {
            f1Pressed = true; // Set f1Pressed to true when F1 key is pressed
        } else if (code == KeyEvent.VK_I) {
            invPressed = true;
        } else if (code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false; // Set upPressed to false when W key is released
        } else if (code == KeyEvent.VK_A) {
            leftPressed = false; // Set leftPressed to false when A key is released
        } else if (code == KeyEvent.VK_S) {
            downPressed = false; // Set downPressed to false when S key is released
        } else if (code == KeyEvent.VK_D) {
            rightPressed = false; // Set rightPressed to false when D key is released
        } else if (code == KeyEvent.VK_I) {
            invPressed = false;
        } else if (code == KeyEvent.VK_ENTER){

        }
    }
}
