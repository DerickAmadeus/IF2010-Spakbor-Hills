package main;
// import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyHandler implements KeyListener{
    public boolean enterPressed, upPressed, downPressed, leftPressed, rightPressed, interactPressed, f1Pressed, invPressed, fpressed ,escapePressed, rPressed, tPressed; // Boolean flags for key states
    public boolean input0, input9;
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
        if (gp.gameState == gp.titleState){
            if (code == KeyEvent.VK_W){
                gp.titlePage.commandNumber--;
                if (gp.titlePage.commandNumber < 0) gp.titlePage.commandNumber = 3;
            } else if (code == KeyEvent.VK_S){
                gp.titlePage.commandNumber++;
                if (gp.titlePage.commandNumber > 3) gp.titlePage.commandNumber = 0;
            } else if (code == KeyEvent.VK_ENTER){
                enterPressed = true;   
                if (gp.titlePage.commandNumber == 0){
                        gp.farmName.reset();           
                        gp.playerInput.reset();              
                        gp.gameState = gp.farmNameInputState; 
                        gp.keyHandler.enterPressed = false;
                } else if (gp.titlePage.commandNumber == 1) { // HELP
                gp.gameState = gp.helpState;
                } else if (gp.titlePage.commandNumber == 2) { // CREDIS
                gp.gameState = gp.creditsState;
                } else if (gp.titlePage.commandNumber == 3) { // QUIT
                    System.exit(0);
                }
                enterPressed = false;
                return;                          // abaikan input lain saat title
            }
        }

        if (gp.gameState == gp.farmNameInputState){
            if (code == KeyEvent.VK_TAB || code == KeyEvent.VK_ESCAPE) {
            gp.farmName.keyPressed(e);
            return;
            }
        gp.farmName.keyPressed(e);
        return;
        }

        if (gp.gameState == gp.helpState) {
            gp.help.keyPressed(e);
            return;
        }

        if (gp.gameState == gp.creditsState) {
            gp.credits.keyPressed(e);
            return;
        }

        if (gp.gameState == gp.playerNameInputState) {
            gp.playerInput.keyPressed(e);
            return;
        }


        if (gp.gameState != gp.titleState && gp.gameState != gp.helpState && gp.gameState != gp.farmNameInputState && gp.gameState != gp.fishingState){
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
            } else if (code == KeyEvent.VK_ENTER) {
                enterPressed = true;
            } else if (code == KeyEvent.VK_F) {
                fpressed = true;
            } else if (code == KeyEvent.VK_ESCAPE) {
                escapePressed = true;
            } else if (code == KeyEvent.VK_R) {
                rPressed = true; 
            } else if (code == KeyEvent.VK_T) {
                tPressed = true;
            }
        }

        if (gp.gameState == gp.fishingState) {
            gp.handleFishingInput(code);
            return;
        }

        if (gp.gameState == gp.menuState) {
            gp.player.handleMenuKey(e);
            return;
        }
        
        if (gp.gameState == gp.inGameHelpState) {          
            gp.inGameHelp.keyPressed(e);                   
            return;                                       
        }

        if (gp.gameState == gp.playerInfoState) {          
            gp.playerInfo.keyPressed(e);                   
            return;                                       
        }

        if (gp.gameState == gp.statisticsState) {          
            gp.statistics.keyPressed(e);                   
            return;                                       
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
        } else if (code == KeyEvent.VK_ENTER && gp.gameState != gp.fishingState) {
            enterPressed = false;
        } else if (code == KeyEvent.VK_F) {
            fpressed = false;
        } else if (code == KeyEvent.VK_ESCAPE) {
            escapePressed = false;
        } else if (code == KeyEvent.VK_R) {
            rPressed = false;
        } else if (code == KeyEvent.VK_T) {
            tPressed = false;
        }
    }
}

