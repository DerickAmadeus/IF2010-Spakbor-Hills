package main;


import javax.imageio.ImageIO;
import javax.swing.JPanel;
import Items.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import java.awt.image.BufferedImage;
import player.Player;
import Map.Map;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.Font;
import Items.*;
import Map.Map;
import java.awt.Color; // ← Tambahkan ini
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException; // Importing player class from player package
import java.util.ArrayList; // Importing map class from Map package
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import player.Player; 


public class GamePanel extends JPanel implements Runnable {

    //Game State
    public final int titleState = -3;
    public final int farmNameInputState = -2;
    public final int helpState = -1;
    public final int playState = 0;
    public final int pauseState = 1;
    public final int dialogState = 2;
    public final int inventoryState = 3;
    public final int itemOptionState = 4;
    public final int sleepingFadeOutState = 5; // New state for fading to black
    public final int sleepingFadeInState = 6;  // New state for fading back from black
    public final int fishingState = 5;
    public int gameState = titleState;

    public String[] initialSeason = {"Spring", "Summer", "Fall", "Winter"};
    public int currentSeasonIndex = 0;
    public String currentSeason = initialSeason[currentSeasonIndex];
    public String[] initialWeather = {"Rainy", "Sunny"};
    public String currentWeather = initialWeather[1];
    public int[] rainDaysInSeason = new int[2]; // Menyimpan dua hari hujan dalam 1 musim
    List<RainDrop> rainDrops = new ArrayList<>();
    private final int RAIN_COUNT = 100;

    public Fish[] allFishes = loadInitialFish();
    public Fish fishingTargetFish = null;
    public int fishingTarget = -1;      // Angka yang harus ditebak
    public int fishingAttempts = 0;     // Berapa kali user sudah mencoba
    public int maxFishingAttempts = 0;  // Batas percobaan sesuai rarity
    public String fishingHint = "";


    // Game Time
    public int gameHour = 6; // Mulai dari jam 6 pagi
    public int gameMinute = 0;
    public int gameDay = 1;
    public int daysPlayed = 0;
    public int lastUpdateMinute = -1;

    private long lastRealTime = System.currentTimeMillis();
    private static final int REAL_TIME_INTERVAL = 1000;

    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    public final int worldCol = 32;
    public final int worldRow = 32;
    public final int worldWidth = tileSize * worldCol;
    public final int worldHeight = tileSize * worldRow;

    public List<TransitionData> transitions;
    public Map map = new Map(this);
    public KeyHandler keyHandler = new KeyHandler(this); // Key handler for keyboard input 
    public final TitlePage  titlePage  = new TitlePage(this);
    public final FarmName  farmName  = new FarmName(this);
    public CollisionChecker cChecker = new CollisionChecker(this); // Collision checker for player movement
    public Help help = new Help(this);
    public Player player; // Player object
    private BufferedImage backgroundImage; // Background image for the game\
    
    public boolean debugMode = false;

    public String fishingInput = "";

    int playerX = 100; // Player's X position
    int playerY = 100; // Player's Y position
    int playerSpeed = 4; // Player's speed
    
    
    Thread gameThread; // Thread for the game loop

    public GamePanel() {
        this.setPreferredSize(new java.awt.Dimension(screenWidth, screenHeight));
        // this.setBackground(java.awt.Color.cyan);
        this.setDoubleBuffered(true); // Enable double buffering for smoother rendering
        this.addKeyListener(keyHandler); // Add key listener for keyboard input
        this.setFocusable(true); // Make the panel focusable to receive key events
        
        gameState = titleState; // start dari title dulu

        setFocusTraversalKeysEnabled(false);

        this.player = new Player(this, keyHandler, ""); // Initialize player object

        initializeTransitions(); // Panggil setelah tileSize dan player siap

        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/main/cloud.png"));
            System.out.println("Gambar latar belakang game berhasil dimuat.");
        } catch (IOException e) {
            System.err.println("Gagal memuat gambar latar belakang game: " + e.getMessage());
            e.printStackTrace();
            backgroundImage = null;
        }
        for (int i = 0; i < RAIN_COUNT; i++) {
            int x = (int)(Math.random() * screenWidth);
            int y = (int)(Math.random() * screenHeight);
            int speed = 2 + (int)(Math.random() * 3); // Kecepatan bervariasi
            rainDrops.add(new RainDrop(x, y, speed, this));
        }
    }

    private void initializeTransitions() {
        transitions = new ArrayList<>();
        transitions.add(new TransitionData(0, 0, 10, 1, 3, 1, 12, 11, false, tileSize));
        transitions.add(new TransitionData(1, 15, 10, 1, 3, 0, 1, 11, false, tileSize));
        transitions.add(new TransitionData(0, 15, 0, 3, 1, 2, 10, 9, false, tileSize));
        transitions.add(new TransitionData(2, 10, 10, 3, 1, 0, 16, 1, false, tileSize));
        transitions.add(new TransitionData(0, 5, 10, 1, 1, 3, 7, 12, false, tileSize));
        transitions.add(new TransitionData(3, 7, 13, 1, 1, 0, 5, 11, false, tileSize));

        //Farm Map ke NPC map and backwards
        transitions.add(new TransitionData(0, 30, 30, 1, 3, 4, 4, 5, false, tileSize));
        transitions.add(new TransitionData(4, 3, 5, 1, 3, 0, 29, 30, false, tileSize));

        //NPC Map ke MT House Map and backwards
        transitions.add(new TransitionData(4, 17, 3, 1, 1, 5, 7, 12, false, tileSize));
        transitions.add(new TransitionData(5, 7, 13, 1, 1, 4, 17, 4, false, tileSize));

        //NPC Map ke C House Map and backwards
        transitions.add(new TransitionData(4, 26, 3, 1, 1, 6, 7, 12, false, tileSize));
        transitions.add(new TransitionData(6, 7, 13, 1, 1, 4, 26, 4, false, tileSize));


        // Tambahkan transisi lain sesuai kebutuhan Anda
    }

    public void checkMapTransitions() {
        for (TransitionData transition : transitions) {
            transition.updateCooldown();
            Rectangle absolutePlayerSolidArea = new Rectangle(
                    player.x + player.solidArea.x,
                    player.y + player.solidArea.y,
                    player.solidArea.width,
                    player.solidArea.height);

            if (transition.isTriggered(absolutePlayerSolidArea, map.currentMapID)) {
                if (!transition.requiresInteraction) {
                    System.out.println("Transition triggered: From Map ID " + map.currentMapID +
                            " To Map ID " + transition.targetMapID +
                            " at player pos (" + transition.targetPlayerX / tileSize + ", " +
                            transition.targetPlayerY / tileSize + ")");
                    int previousMapID = map.currentMapID;
                    map.loadMapByID(transition.targetMapID);
                    player.x = transition.targetPlayerX;
                    player.y = transition.targetPlayerY;

                    // Reset status penting pemain jika perlu
                    player.collisionOn = false; 
                    player.direction = "down"; // Atur arah default
                    // player.isActuallyMoving = false; // Jika Anda memiliki variabel ini di Player
                    player.setLocation(transition.targetMapID);
                    if (map.currentMapID != 3 && map.currentMapID != 0) {
                        addMinutes(15);
                        player.setEnergy(player.getEnergy() - 10);

                    }
                    transition.startCooldown(); // Mulai cooldown untuk transisi yang baru saja digunakan

                    // Mencegah langsung kembali: terapkan cooldown pada transisi yang mengarah kembali
                    // jika pemain spawn di atasnya.

                    for (TransitionData otherTransition : transitions) {
                        if (otherTransition.sourceMapID == map.currentMapID &&
                                otherTransition.targetMapID == previousMapID) {
                            Rectangle playerSpawnSolidArea = new Rectangle(
                                    player.x + player.solidArea.x,
                                    player.y + player.solidArea.y,
                                    player.solidArea.width,
                                    player.solidArea.height);
                            if (otherTransition.sourceArea.intersects(playerSpawnSolidArea)) {
                                otherTransition.startCooldown();
                                System.out.println("Applied return cooldown to transition from map " + otherTransition.sourceMapID + " to " + otherTransition.targetMapID);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
    private void setRainDaysForSeason() {
        int day1 = 1 + (int)(Math.random() * 10);
        int day2;
        do {
            day2 = 1 + (int)(Math.random() * 10);
        } while (day2 == day1);

        rainDaysInSeason[0] = day1;
        rainDaysInSeason[1] = day2;
    }

    public Fish[] loadInitialFish() {
        ArrayList<String> any = new ArrayList<>(Arrays.asList("Spring", "Summer", "Fall", "Winter"));
        ArrayList<String> spring = new ArrayList<>(Arrays.asList("Spring"));
        ArrayList<String> summer = new ArrayList<>(Arrays.asList("Summer"));
        ArrayList<String> fall = new ArrayList<>(Arrays.asList("Fall"));
        ArrayList<String> winter = new ArrayList<>(Arrays.asList("Winter"));
        ArrayList<String> sturgeonSeason = new ArrayList<>(Arrays.asList("Summer", "Winter"));
        ArrayList<String> midnightCarpSeason = new ArrayList<>(Arrays.asList("Fall", "Winter"));
        ArrayList<String> flounderSeason = new ArrayList<>(Arrays.asList("Spring", "Summer"));
        ArrayList<String> superCucumberSeason = new ArrayList<>(Arrays.asList("Summer", "Fall", "Winter"));
        ArrayList<String> catfishSeason = new ArrayList<>(Arrays.asList("Spring", "Summer", "Fall"));

        ArrayList<String> rainy = new ArrayList<>(Arrays.asList("Rainy")); 
        ArrayList<String> sunny = new ArrayList<>(Arrays.asList("Sunny"));
        ArrayList<String> both = new ArrayList<>(Arrays.asList("Rainy", "Sunny"));

        Fish[] fishlist = new Fish[]{
            new Fish("Bullhead", "Ikan Bullhead, mudah ditemukan.", 50, 50, any, both, new ArrayList<>(Arrays.asList("Mountain Lake")), "Common", 
                    new ArrayList<>(Arrays.asList(0)), new ArrayList<>(Arrays.asList(24))),
            new Fish("Carp", "Ini Carp.", 50, 50, any, both, new ArrayList<>(Arrays.asList("Mountain Lake", "Pond")), "Common", 
                    new ArrayList<>(Arrays.asList(0)), new ArrayList<>(Arrays.asList(24))),
            new Fish("Chub", "Ikan Chub, cukup umum.", 50, 50, any, both, new ArrayList<>(Arrays.asList("Forest River", "Mountain Lake")), "Common", 
                    new ArrayList<>(Arrays.asList(0)), new ArrayList<>(Arrays.asList(24))),
            new Fish("Largemouth Bass", "Ikan besar dari danau pegunungan.", 100, 100, any, both, new ArrayList<>(Arrays.asList("Mountain Lake")), "Regular", 
                    new ArrayList<>(Arrays.asList(6)), new ArrayList<>(Arrays.asList(18))),
            new Fish("Rainbow Trout", "Ikan berwarna pelangi yang muncul saat cuaca cerah.", 120, 120, summer, sunny, new ArrayList<>(Arrays.asList("Forest River", "Mountain Lake")), "Regular", 
                    new ArrayList<>(Arrays.asList(6)), new ArrayList<>(Arrays.asList(18))),
            new Fish("Sturgeon", "Ikan langka dari danau pegunungan.", 200, 200, sturgeonSeason, both, new ArrayList<>(Arrays.asList("Mountain Lake")), "Regular", 
                    new ArrayList<>(Arrays.asList(6)), new ArrayList<>(Arrays.asList(18))),
            new Fish("Midnight Carp", "Ikan malam dari danau atau kolam.", 150, 150, midnightCarpSeason, both, new ArrayList<>(Arrays.asList("Mountain Lake", "Pond")), "Regular", 
                    new ArrayList<>(Arrays.asList(20)), new ArrayList<>(Arrays.asList(2))),
            new Fish("Flounder", "Ikan pipih dari laut.", 90, 90, flounderSeason, both, new ArrayList<>(Arrays.asList("Ocean")), "Regular", 
                    new ArrayList<>(Arrays.asList(6)), new ArrayList<>(Arrays.asList(22))),
            new Fish("Halibut", "Ikan laut besar aktif pagi dan malam.", 110, 110, any, both,  new ArrayList<>(Arrays.asList("Ocean")), "Regular", 
                    new ArrayList<>(Arrays.asList(6, 19)), new ArrayList<>(Arrays.asList(11, 2))),
            new Fish("Octopus", "Gurita laut yang aktif siang hari.", 180, 180, summer, both,  new ArrayList<>(Arrays.asList("Ocean")), "Regular", 
                    new ArrayList<>(Arrays.asList(6)), new ArrayList<>(Arrays.asList(22))),
            new Fish("Pufferfish", "Ikan buntal beracun saat cuaca cerah.", 160, 160, summer, sunny,  new ArrayList<>(Arrays.asList("Ocean")), "Regular", 
                    new ArrayList<>(Arrays.asList(0)), new ArrayList<>(Arrays.asList(16))),
            new Fish("Sardine", "Ikan kecil dari laut.", 40, 40, any, both,  new ArrayList<>(Arrays.asList("Ocean")), "Common", 
                    new ArrayList<>(Arrays.asList(6)), new ArrayList<>(Arrays.asList(18))),
            new Fish("Super Cucumber", "Ikan misterius aktif malam hari.", 250, 250, superCucumberSeason, both,  new ArrayList<>(Arrays.asList("Ocean")), "Regular", 
                    new ArrayList<>(Arrays.asList(18)), new ArrayList<>(Arrays.asList(2))),
            new Fish("Catfish", "Ikan lele liar saat hujan.", 130, 130, catfishSeason, rainy,  new ArrayList<>(Arrays.asList("Forest River", "Pond")), "Regular", 
                    new ArrayList<>(Arrays.asList(6)), new ArrayList<>(Arrays.asList(22))),
            new Fish("Salmon", "Ikan migrasi dari sungai.", 120, 120, fall, both, new ArrayList<>(Arrays.asList("Forest River")), "Regular", 
                    new ArrayList<>(Arrays.asList(6)), new ArrayList<>(Arrays.asList(18))),
            new Fish("Angler", "Ikan legendaris yang hanya muncul di musim gugur.", 1000, 1000, fall, both,new ArrayList<>(Arrays.asList("Pond")), "Legendary", 
                    new ArrayList<>(Arrays.asList(8)), new ArrayList<>(Arrays.asList(20))),
            new Fish("Crimsonfish", "Ikan legendaris dari laut tropis.", 1000, 1000, summer, both, new ArrayList<>(Arrays.asList("Ocean")), "Legendary", 
                    new ArrayList<>(Arrays.asList(8)), new ArrayList<>(Arrays.asList(20))),
            new Fish("Glacierfish", "Ikan legendaris dari sungai beku.", 1000, 1000, winter, both, new ArrayList<>(Arrays.asList("Forest River")), "Legendary", 
                    new ArrayList<>(Arrays.asList(8)), new ArrayList<>(Arrays.asList(20))),
            new Fish("Legend", "Ikan legendaris tertinggi di danau gunung saat hujan.", 1200, 1200, spring, rainy, new ArrayList<>(Arrays.asList("Mountain Lake")), "Legendary", 
                    new ArrayList<>(Arrays.asList(8)), new ArrayList<>(Arrays.asList(20)))
        };
        for (Fish f : fishlist) {
            int harga = f.calculateHargaJual();
            f.setHargaJual(harga);  // pastikan kamu punya setter `setHargaJual()`
        }

        return fishlist;
    }

    public Fish[] filterFishesBySeasonAndWeather(String season, String weather, int gameHour) {
        ArrayList<Fish> filtered = new ArrayList<>();
        int currentHour = gameHour;

        for (Fish fish : allFishes) {
            boolean seasonMatch = fish.getSeason().contains(season);
            boolean weatherMatch = fish.getWeather().contains(weather);
            boolean timeMatch = false;

            ArrayList<Integer> appearTimes = fish.getAppearTime();
            ArrayList<Integer> disappearTimes = fish.getDisappearTime();

            for (int i = 0; i < appearTimes.size(); i++) {
                int start = appearTimes.get(i);
                int end = disappearTimes.get(i);

                if (start <= end) {
                    // Normal time range, e.g. 6 to 18
                    if (currentHour >= start && currentHour < end) {
                        timeMatch = true;
                        break;
                    }
                } else {
                    // Overnight time range, e.g. 20 to 2
                    if (currentHour >= start || currentHour < end) {
                        timeMatch = true;
                        break;
                    }
                }
            }

            if (seasonMatch && weatherMatch && timeMatch) {
                filtered.add(fish);
            }
        }

        return filtered.toArray(new Fish[0]);
    }


    public void handleFishingPasswordInput(int code) {
        if (gameState != fishingState) return;

        if (fishingTarget == -1) {
            // Setikan ikan dan password saat pertama kali masuk ke fishingState
            Fish[] currentFish = filterFishesBySeasonAndWeather(currentSeason, currentWeather, gameHour);
            for (Fish fish : currentFish) {
                System.out.println(fish.getName());
            }
            if (currentFish.length == 0) return;

            Fish prize = currentFish[(int)(Math.random() * currentFish.length)];
            fishingTargetFish = prize; // Simpan untuk diberikan saat menang

            int max = 10;
            if (prize.getRarity().equals("Regular")) max = 100;
            else if (prize.getRarity().equals("Legendary")) max = 500;

            fishingTarget = (int) (Math.random() * max) + 1;
            fishingAttempts = 0;
            maxFishingAttempts = prize.getRarity().equals("Legendary") ? 7 : 10;

            System.out.println("Tebak angka 1-" + max + " untuk menangkap " + prize.getName() + " (" + prize.getRarity() + ")");
            fishingInput = "";
            return;
        }
        boolean dapet = false;
        // Input angka
        if (code == KeyEvent.VK_BACK_SPACE && fishingInput.length() > 0) {
            fishingInput = fishingInput.substring(0, fishingInput.length() - 1);
        } else if (code >= KeyEvent.VK_0 && code <= KeyEvent.VK_9 && fishingInput.length() < 3) {
            fishingInput += (char) code;
        } else if (code == KeyEvent.VK_ENTER && fishingInput != null && !fishingInput.isEmpty()) {
            if (fishingInput.equals(String.valueOf(fishingTarget))) {
                player.getInventory().addItem(fishingTargetFish, 1);
                System.out.println("Selamat!! Kamu dapat: " + fishingTargetFish.getName());
                dapet = true;
                resetFishing();
            } else {
                fishingAttempts++;
                int inputVal = Integer.parseInt(fishingInput);
                if (inputVal < fishingTarget) {
                    fishingHint = "Terlalu kecil!";
                } else if (inputVal > fishingTarget) {
                    fishingHint = "Terlalu besar!";
                }
                fishingInput = ""; // reset input, bukan menutup window
            }
            if (fishingAttempts >= maxFishingAttempts && !dapet) {
                System.out.println("Kesempatan habis. Gagal memancing!");
                resetFishing();
            }
        } 
    }


    public void setupGame() {
      gameState = titleState;
    }


    private void resetFishing() {
        fishingTarget = -1;
        fishingInput = "";
        fishingAttempts = 0;
        maxFishingAttempts = 0;
        fishingTargetFish = null;
        gameState = playState;
        fishingHint = "";
    }


    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    public void startSleepingSequence() {
        if (gameState == playState) { // Ensure we can only sleep from playState
            gameState = sleepingFadeOutState;
            fadeAlpha = 0; // Start fully transparent, will fade to black
        }
    }

    public void addMinutes(int minutesToAdd) {
        for (int i = 0; i < minutesToAdd; i += 5) {
            gameMinute += 5;
            if (gameMinute >= 60) {
                gameMinute -= 60;
                gameHour++;
                if (gameHour >= 24) {
                    gameHour = 0;
                    gameDay++;
                    daysPlayed++;
                    if (gameDay > 10) { // Season Change Logic
                        currentSeasonIndex = (currentSeasonIndex + 1) % initialSeason.length;
                        currentSeason = initialSeason[currentSeasonIndex];
                        gameDay = 1; // Reset day for new season
                    }
                }
            }
            map.updateTiles();
        }
    }

    @Override
    public void run() {
        while (gameThread != null) {
            update();
            repaint();
            try {
                Thread.sleep(16); // roughly 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {

        if (gameState == titleState){
            if(keyHandler.enterPressed){
                if (titlePage.commandNumber == 3){
                    System.exit(0);
                }
                else if(titlePage.commandNumber == 0){
                    gameState = farmNameInputState;
                }
                else if(titlePage.commandNumber == 2){
                    gameState = helpState;
                }
            }
        }
        else if (gameState == farmNameInputState){
            setRainDaysForSeason();
            //     // if(keyHandler.enterPressed){
        //     //   gameState = playState;
        }
        // }   
        
        player.update();

        // Potentially update other game entities or systems here
        // e.g., map.update(), npcs.update(), etc.


        if (keyHandler.f1Pressed) { // Assuming you add f1Pressed to KeyHandler
            debugMode = !debugMode;
            keyHandler.f1Pressed = false; // Consume the press to avoid rapid toggling
            System.out.println("Debug mode: " + (debugMode ? "ON" : "OFF"));
        }
        if (keyHandler.invPressed) {
            if (gameState == playState) {
                gameState = inventoryState;
            } else if (gameState == inventoryState || gameState == itemOptionState) {
                gameState = playState;
                keyHandler.enterPressed = false; // Consume press
            }
        } else if (gameState == playState) {
            player.update();
            player.tiling();
            player.recoverLand();
            player.planting();
            checkMapTransitions();
            player.watering();
            player.harvesting();
            player.sleeping();
            player.WatchTV();
<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> fb525b28b82c427a37f31a1f9f1e0809b32dc757
            // Time progression
            long now = System.currentTimeMillis();
            if (now - lastRealTime >= REAL_TIME_INTERVAL) {
                gameMinute += 5;
                if (gameMinute >= 60) {
                    gameMinute = 0;
                    gameHour++;
                    if (gameHour >= 24) {
                        gameHour = 0; // Midnight
                        gameDay++;
                        daysPlayed++;
                        // Season Change Logic on natural day turnover
                        if (gameDay > 10) {
                            currentSeasonIndex = (currentSeasonIndex + 1) % initialSeason.length;
                            currentSeason = initialSeason[currentSeasonIndex];
                            gameDay = 1; // Reset day for new season
                        }
                    }
                }
                map.updateTiles();
                lastRealTime = now;
            }
            player.fishing();
<<<<<<< HEAD
>>>>>>> fb525b28b82c427a37f31a1f9f1e0809b32dc757
=======
>>>>>>> fb525b28b82c427a37f31a1f9f1e0809b32dc757
        }
        if (gameState == inventoryState) {
            player.getInventory().updateInventoryCursor(
                keyHandler.upPressed,
                keyHandler.downPressed,
                keyHandler.leftPressed,
                keyHandler.rightPressed
            );

        } else if (gameState == inventoryState) {
            player.getInventory().updateInventoryCursor(
                    keyHandler.upPressed, keyHandler.downPressed, keyHandler.leftPressed, keyHandler.rightPressed);
            keyHandler.upPressed = false;
            keyHandler.downPressed = false;
            keyHandler.leftPressed = false;
            keyHandler.rightPressed = false;

            if (keyHandler.enterPressed) {
                player.getInventory().selectCurrentItem();
                // gameState might change to itemOptionState by selectCurrentItem()
                keyHandler.enterPressed = false;
            }
        } else if (gameState == itemOptionState) {
            if (keyHandler.upPressed) {
                player.getInventory().optionCommandNum = (player.getInventory().optionCommandNum - 1 + 3) % 3;
                keyHandler.upPressed = false;
            }
            if (keyHandler.downPressed) {
                player.getInventory().optionCommandNum = (player.getInventory().optionCommandNum + 1) % 3;
                keyHandler.downPressed = false;
            }
            if (keyHandler.enterPressed) {
                Item selected = player.getInventory().getSelectedItem();
                if (selected instanceof Equipment) {
                    Equipment eq = (Equipment) selected;
                    if (player.getInventory().optionCommandNum == 0) { // Equip/Unequip
                        if (player.getEquippedItem() == eq) player.equipItem(null);
                        else player.equipItem(eq);
                        gameState = inventoryState;
                    } else if (player.getInventory().optionCommandNum == 1) { // Cancel
                        gameState = inventoryState;
                    }
                } else if (selected instanceof Seeds) {
                     Seeds sd = (Seeds) selected;
                    if (player.getInventory().optionCommandNum == 0) { // Equip/Unequip
                        if (player.getEquippedItem() == sd) player.equipItem(null);
                        else player.equipItem(sd);
                        gameState = inventoryState;
                    } else if (player.getInventory().optionCommandNum == 1) { // Cancel
                        gameState = inventoryState;
                    }
                } else if (selected instanceof Fish || selected instanceof Crops || selected instanceof Food) {
                     if (player.getInventory().optionCommandNum == 0) { // Eat
                        player.eating(); // Assumes eating handles item removal & energy
                        gameState = inventoryState;
                    } else if (player.getInventory().optionCommandNum == 1) { // Cancel
                        gameState = inventoryState;
                    }
                }
                keyHandler.enterPressed = false;
            }
        } else if (gameState == sleepingFadeOutState) {
            fadeAlpha += FADE_SPEED;
            if (fadeAlpha >= 255) {
                fadeAlpha = 255; // Fully black
                // --- Perform sleep actions ---
                gameHour = 6; // Wake up at 6 AM
        }
        if (gameState == fishingState) {
            if(keyHandler.enterPressed) {
                gameState = playState;
                keyHandler.enterPressed = false;
            }
        }
        long now = System.currentTimeMillis();
        if (now - lastRealTime >= REAL_TIME_INTERVAL && gameState != fishingState) {
            gameMinute += 5;
            if (gameMinute >= 60) {
                gameMinute = 0;
                gameDay++;
                daysPlayed++;
                player.setEnergy(Player.MAX_ENERGY); // Restore energy

                // Season Change Logic
                if (gameDay > 10) { // Assuming 10 days per season
                    currentSeasonIndex = (currentSeasonIndex + 1) % initialSeason.length;
                    currentSeason = initialSeason[currentSeasonIndex];
                    gameDay = 1; // Reset day for new season
                }
                map.updateTiles(); // Update tiles for the new day/time
                // --- End sleep actions ---
                gameState = sleepingFadeInState; // Start fading back in
            }
        } else if (gameState == sleepingFadeInState) {
            fadeAlpha -= FADE_SPEED;
            if (fadeAlpha <= 0) {
                fadeAlpha = 0; // Fully transparent
                gameState = playState; // Back to game
            }
        }

        // Global key listeners (can be accessed from multiple states)
        if (keyHandler.f1Pressed) {
            debugMode = !debugMode;
            keyHandler.f1Pressed = false;
            System.out.println("Debug mode: " + (debugMode ? "ON" : "OFF"));
        }
        if (keyHandler.invPressed) {
            if (gameState == playState) {
                gameState = inventoryState;
            } else if (gameState == inventoryState || gameState == itemOptionState) {
                // Close inventory/options and return to play state
                gameState = playState;
            }
            keyHandler.invPressed = false; // Consume the press

        if (gameDay > 10) {
            currentSeasonIndex = (currentSeasonIndex + 1) % 4;
            currentSeason = initialSeason[currentSeasonIndex];
            gameDay %= 10;
            setRainDaysForSeason();
        }
        
        if (gameDay == rainDaysInSeason[0] || gameDay == rainDaysInSeason[1]) {
            currentWeather = initialWeather[0]; // Hujan
        } else {
            currentWeather = initialWeather[1];
        }
        if (currentWeather.equals("Rainy")) {
            for (RainDrop drop : rainDrops) {
                drop.update();
            }
        }
    }

    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (gameState == titleState) {
            titlePage.draw(g2);
            g2.dispose(); // dispose was here in original, but typically not at the end of paintComponent
            return;
        } else if (gameState == farmNameInputState) {
            g2.setColor(java.awt.Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            g2.setColor(java.awt.Color.white);
            g2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 30));
            g2.drawString("Enter Farm Name:", 100, screenHeight / 2 - 40);
            // Add input field drawing here if you have one for farm name
            g2.drawString("Press Enter to Continue", 100, screenHeight / 2 + 40);
            // No g2.dispose() here in original for this state
            return;
        }

        // Draw game world (visible during play, pause, dialog, inventory, options, and fades)
        if (map.currentMapID == 3) { // House map
            g2.setColor(java.awt.Color.black);
            g2.dispose();
            return;
        } else if (gameState == farmNameInputState){
            farmName.draw(g2);
            g2.dispose();
            return;
        }
        else if (gameState == helpState) {
            help.draw(g2);
            g2.dispose();
        return;
        }


        if (map.currentMapID == 3) { // Ganti angka 3 jika ID peta rumah Anda berbeda
            g2.setColor(java.awt.Color.black); // Atur latar belakang menjadi hitam untuk rumah
            g2.fillRect(0, 0, screenWidth, screenHeight);
        } else { // Other maps
            if (backgroundImage != null) {
                g2.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight, null);
            } else {
                g2.setColor(java.awt.Color.cyan);
                g2.fillRect(0, 0, screenWidth, screenHeight);
            }
        }
        map.draw(g2);
        player.drawPlayer(g2);

        map.draw(g2); // Draw the map
        if (currentWeather.equals("Rainy")) {
            g2.setColor(new Color(0, 0, 0, 80)); // Hitam transparan untuk efek gelap
            g2.fillRect(0, 0, screenWidth, screenHeight);

            for (RainDrop drop : rainDrops) {
                drop.draw(g2); // Gambar partikel hujan di atas
            }
        }
        g2.setColor(java.awt.Color.white);
        g2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
        String timeString = String.format("Day %d - %02d:%02d", gameDay, gameHour, gameMinute);
        g2.drawString(timeString, 500, 30);
        g2.drawString(currentSeason, 500, 50);
        g2.drawString(player.getLocation(), 500, 70);
        g2.drawString(currentSeason + " - " + currentWeather, 500, 50);



        player.drawPlayer(g2);
        player.drawEnergyBar(g2);

        // Debug transition areas
        if (debugMode) {
            for (TransitionData transition : transitions) {
                if (transition.sourceMapID == map.currentMapID) {
                    g2.setColor(new Color(0, 0, 255, 80));
                    int screenAreaX = transition.sourceArea.x - player.x + player.screenX;
                    int screenAreaY = transition.sourceArea.y - player.y + player.screenY;
                    g2.fillRect(screenAreaX, screenAreaY, transition.sourceArea.width, transition.sourceArea.height);
                    if (transition.cooldownFrames > 0) {
                        g2.setColor(Color.YELLOW);
                        g2.setFont(new Font("Arial", Font.BOLD, 12));
                        g2.drawString("COOLDOWN: " + transition.cooldownFrames, screenAreaX, screenAreaY - 5);
                    }
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Arial", Font.BOLD, 10));
                    g2.drawString("ToMap:" + transition.targetMapID, screenAreaX, screenAreaY + 12);
                }
            }
        }

        // Inventory and Item Options UI (drawn on top of game world and basic UI)
        if (gameState == inventoryState) {
            player.openInventory(g2);
        }
        if (gameState == itemOptionState) {
            player.getInventory().drawItemOptionWindow(g2); // Assumes inventory is drawn first or handled by this method
        }

        // Fade Effect Overlay (drawn on top of almost everything)
        if (gameState == sleepingFadeOutState || gameState == sleepingFadeInState) {
            g2.setColor(new Color(0, 0, 0, fadeAlpha)); // Black color with current alpha
            g2.fillRect(0, 0, screenWidth, screenHeight);
        }

        if (gameState == fishingState) {
            player.drawFishingWindow(g2);
        }

    }

    public void startSleepingSequence() {
        // Mengatur jam permainan ke 6 pagi
        gameHour = 5;
        gameMinute = 55;

        // Mengatur hari permainan ke hari berikutnya
        gameDay++;
        daysPlayed++;
        // Memastikan pemain tidak dalam keadaan dialog atau interaksi lainnya
        gameState = playState;

        // Update peta dan transisi jika diperlukan
        map.updateTiles(); // Update tiles setelah tidur

        for (int i = 0; i <= 200; i += 10) {
            try {
            java.awt.Graphics g = this.getGraphics();
            if (g != null) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, i));
                g2.fillRect(0, 0, screenWidth, screenHeight);
                g2.dispose();
                Thread.sleep(30); // jeda animasi
            }
            } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            }
        }
        repaint(); // Memanggil repaint agar tampilan map diperbarui setelah tidur
    }
}