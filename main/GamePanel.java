package main;

import Furniture.*;
import Items.*;
import Map.Map; // ← Tambahkan ini
import Map.ShippingBin;
import Map.Tile;
import NPC.NPC;
import NPC.Seller;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle; // Importing player class from player package
import java.awt.event.KeyEvent; // Importing map class from Map package
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import main.menu.InGameHelp;
import main.menu.PlayerInfo;
import main.menu.Statistics;
import player.Player;
import player.Recipe;
import player.RecipeLoader;


public class GamePanel extends JPanel implements Runnable {

    // Game State
    public final int titleState = -4;
    public final int farmNameInputState = -3;
    public final int playerNameInputState = -2;
    public final int helpState = -1;
    public final int playState = 0;
    public final int menuState = 1; 
    public final int dialogState = 2;
    public final int inventoryState = 3;
    public final int itemOptionState = 4;
    public final int fishingState = 5;
    public final int cookingState = 6;
    public final int fuelState = 7;
    public final int fishSelectionState = 8;
    public final int watchingState = 9;
    public final int fishingWinState = 10;
    public final int inGameHelpState  = 11;
    public final int playerInfoState  = 12;
    public final int statisticsState = 13;
    public final int creditsState = 14;
    public final int viewShippingState = 18;
    public final int shippingState = 19;
    public final int shippingOptionState = 20;
    public final int cutsceneState = 21;
    public int gameState = titleState;
    public String[] initialSeason = { "Spring", "Summer", "Fall", "Winter" };
    public int currentSeasonIndex = 0;
    public String currentSeason = initialSeason[currentSeasonIndex];
    public String[] initialWeather = { "Rainy", "Sunny" };
    public String currentWeather = initialWeather[1];
    public int[] rainDaysInSeason = new int[2]; 
    List<RainDrop> rainDrops = new ArrayList<>();
    private final int RAIN_COUNT = 100;

    private int lastday = 1;

    public Fish[] allFishes = loadInitialFish();
    public Fish fishingTargetFish = null;
    public int fishingTarget = -1; 
    public int fishingAttempts = 0; 
    public int maxFishingAttempts = 0; 
    public String fishingHint = "";

    public NPC[] npcs = loadNPCs(); 
    public Seller seller;
    public Recipe[] allRecipes = RecipeLoader.loadInitialRecipes();
    public Misc[] fuels = {new Misc("Firewood", "ini firewood", 20, 40), new Misc("Coal", "ini coal", 20, 40)};
    public int cookingCursorCol = 0;
    public int cookingCursorRow = 0;
    public int cookingCursorOffset = 0;
    public int fuelCursorCol = 0;
    public int fuelCursorRow = 0;
    public int fuelCursorOffset = 0;
    public int fishSelectionCol = 0;
    public int fishSelectionRow = 0;
    public int fishSelectionOffset = 0;
    public Stove activeStove; 
    public TV activeTV;
    Recipe pendingRecipe;
    int requiredFishAmount;

    public int gameHour = 6;
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
    public KeyHandler keyHandler = new KeyHandler(this);
    public final TitlePage titlePage = new TitlePage(this);
    public final FarmName farmName = new FarmName(this);
    public CollisionChecker cChecker = new CollisionChecker(this); 
    public final PlayerInput playerInput = new PlayerInput(this);
    public Help help = new Help(this);
    public Credits credits = new Credits(this);
    public InGameHelp inGameHelp = new InGameHelp(this);
    public PlayerInfo playerInfo = new PlayerInfo(this);
    public Statistics statistics = new Statistics(this);
    public Player player; 
    private BufferedImage backgroundImage;

    public boolean debugMode = false;

    public String fishingInput = "";

    int playerX = 100; 
    int playerY = 100; 
    int playerSpeed = 4;

    public boolean reachedEndgame = false;

    Thread gameThread; 

    public GamePanel() {
        this.setPreferredSize(new java.awt.Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true); 
        this.addKeyListener(keyHandler);
        this.setFocusable(true); 

        gameState = titleState;

        setFocusTraversalKeysEnabled(false);

        this.player = new Player(this, keyHandler, "");

        initializeTransitions(); 

        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/main/cloud.png"));
            System.out.println("Gambar latar belakang game berhasil dimuat.");
        } catch (IOException e) {
            System.err.println("Gagal memuat gambar latar belakang game: " + e.getMessage());
            e.printStackTrace();
            backgroundImage = null; 
        }
        for (int i = 0; i < RAIN_COUNT; i++) {
            int x = (int) (Math.random() * screenWidth);
            int y = (int) (Math.random() * screenHeight);
            int speed = 2 + (int) (Math.random() * 3); 
            rainDrops.add(new RainDrop(x, y, speed, this));
        }
    }

    private void initializeTransitions() {
        transitions = new ArrayList<>();
        transitions.add(new TransitionData(0, 0, 1, 1, 30, 1, 12, 11, false, tileSize));
        transitions.add(new TransitionData(1, 16, 8, 1, 3, 0, 1, 11, false, tileSize));
        transitions.add(new TransitionData(0, 1, 0, 30, 1, 2, 10, 9, false, tileSize));
        transitions.add(new TransitionData(2, 8, 13, 5, 1, 0, 16, 1, false, tileSize));
        transitions.add(new TransitionData(0, map.getDoorLocationTileX(), map.getDoorLocationTileY(), 1, 1, 3, 7, 12, false, tileSize));
        transitions.add(new TransitionData(3, 7, 13, 1, 1, 0, map.getDoorLocationTileX(), map.getDoorLocationTileY() + 1, false, tileSize));
        transitions.add(new TransitionData(0, 31, 30, 1, 1, 4, 4, 5, false, tileSize));
        transitions.add(new TransitionData(4, 0, 0, 1, 10, 0, 29, 30, false, tileSize));
        transitions.add(new TransitionData(4, 15, 3, 1, 1, 10, 7, 12, false, tileSize));
        transitions.add(new TransitionData(10, 7, 13, 1, 1, 4, 17, 4, false, tileSize));
        transitions.add(new TransitionData(4, 24, 3, 1, 1, 5, 7, 12, false, tileSize));
        transitions.add(new TransitionData(5, 7, 13, 1, 1, 4, 26, 4, false, tileSize));
        transitions.add(new TransitionData(4, 33, 3, 1, 1, 6, 7, 12, false, tileSize));
        transitions.add(new TransitionData(6, 7, 13, 1, 1, 4, 35, 4, false, tileSize));
        transitions.add(new TransitionData(4, 42, 3, 1, 1, 7, 7, 12, false, tileSize));
        transitions.add(new TransitionData(7, 7, 13, 1, 1, 4, 44, 4, false, tileSize));
        transitions.add(new TransitionData(4, 51, 3, 1, 1, 8, 7, 12, false, tileSize));
        transitions.add(new TransitionData(8, 7, 13, 1, 1, 4, 53, 4, false, tileSize));
        transitions.add(new TransitionData(4, 60, 3, 1, 1, 9, 7, 12, false, tileSize));
        transitions.add(new TransitionData(9, 7, 13, 1, 1, 4, 62, 4, false, tileSize));
        transitions.add(new TransitionData(0, 31, 1, 1, 28, 11, 0, 10, false, tileSize));
        transitions.add(new TransitionData(11, 0, 8, 1, 6, 0, 30, 10, false, tileSize)); // Farm Map ke Forest Map
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
                    if (transition.targetMapID == 5) {
                        npcs[0].visitingFrequency += 1;
                    }
                    if (transition.targetMapID == 6) {
                        npcs[1].visitingFrequency += 1;
                    }
                    if (transition.targetMapID == 7) {
                        npcs[2].visitingFrequency += 1;
                    }
                    if (transition.targetMapID == 8) {
                        npcs[3].visitingFrequency += 1;
                    }
                    if (transition.targetMapID == 9) {
                        npcs[5].visitingFrequency += 1;
                    }
                    if (transition.targetMapID == 10) {
                        npcs[4].visitingFrequency += 1;
                    }

                    int previousMapID = map.currentMapID; 

                    map.loadMapByID(transition.targetMapID);
                    player.x = transition.targetPlayerX;
                    player.y = transition.targetPlayerY;
                    player.collisionOn = false;
                    player.direction = "down"; 

                    player.setLocation(transition.targetMapID);
                    if (previousMapID >=4 && previousMapID <= 10 && map.currentMapID == 4){
                        player.setEnergy(player.getEnergy() - 0);
                    } else if (map.currentMapID != 3 && map.currentMapID != 0 && map.currentMapID <= 4) {
                        addMinutes(15);
                        player.setEnergy(player.getEnergy() - 10);
                    } 
                  
                    transition.startCooldown();

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
                                System.out.println("Applied return cooldown to transition from map "
                                        + otherTransition.sourceMapID + " to " + otherTransition.targetMapID);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    public void setRainDaysForSeason() {
        int day1 = 1 + (int)(Math.random() * 10);
        int day2;
        do {
            day2 = 1 + (int) (Math.random() * 10);
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

        Fish[] fishlist = new Fish[] {
                new Fish("Bullhead", 50, 50, any, both,
                        new ArrayList<>(Arrays.asList("Mountain Lake")), "Common",
                        new ArrayList<>(Arrays.asList(0)), new ArrayList<>(Arrays.asList(24))),
                new Fish("Carp", 50, 50, any, both,
                        new ArrayList<>(Arrays.asList("Mountain Lake", "Farm Map")), "Common",
                        new ArrayList<>(Arrays.asList(0)), new ArrayList<>(Arrays.asList(24))),
                new Fish("Chub", 50, 50, any, both,
                        new ArrayList<>(Arrays.asList("Forest River", "Mountain Lake")), "Common",
                        new ArrayList<>(Arrays.asList(0)), new ArrayList<>(Arrays.asList(24))),
                new Fish("Largemouth Bass", 100, 100, any, both,
                        new ArrayList<>(Arrays.asList("Mountain Lake")), "Regular",
                        new ArrayList<>(Arrays.asList(6)), new ArrayList<>(Arrays.asList(18))),
                new Fish("Rainbow Trout", 120, 120, summer,
                        sunny, new ArrayList<>(Arrays.asList("Forest River", "Mountain Lake")), "Regular",
                        new ArrayList<>(Arrays.asList(6)), new ArrayList<>(Arrays.asList(18))),
                new Fish("Sturgeon", 200, 200, sturgeonSeason, both,
                        new ArrayList<>(Arrays.asList("Mountain Lake")), "Regular",
                        new ArrayList<>(Arrays.asList(6)), new ArrayList<>(Arrays.asList(18))),
                new Fish("Midnight Carp", 150, 150, midnightCarpSeason, both,
                        new ArrayList<>(Arrays.asList("Mountain Lake", "Farm Map")), "Regular",
                        new ArrayList<>(Arrays.asList(20)), new ArrayList<>(Arrays.asList(2))),
                new Fish("Flounder", 90, 90, flounderSeason, both,
                        new ArrayList<>(Arrays.asList("Ocean")), "Regular",
                        new ArrayList<>(Arrays.asList(6)), new ArrayList<>(Arrays.asList(22))),
                new Fish("Halibut", 110, 110, any, both,
                        new ArrayList<>(Arrays.asList("Ocean")), "Regular",
                        new ArrayList<>(Arrays.asList(6, 19)), new ArrayList<>(Arrays.asList(11, 2))),
                new Fish("Octopus", 180, 180, summer, both,
                        new ArrayList<>(Arrays.asList("Ocean")), "Regular",
                        new ArrayList<>(Arrays.asList(6)), new ArrayList<>(Arrays.asList(22))),
                new Fish("Pufferfish", 160, 160, summer, sunny,
                        new ArrayList<>(Arrays.asList("Ocean")), "Regular",
                        new ArrayList<>(Arrays.asList(0)), new ArrayList<>(Arrays.asList(16))),
                new Fish("Sardine", 40, 40, any, both, new ArrayList<>(Arrays.asList("Ocean")),
                        "Common",
                        new ArrayList<>(Arrays.asList(6)), new ArrayList<>(Arrays.asList(18))),
                new Fish("Super Cucumber", 250, 250, superCucumberSeason, both,
                        new ArrayList<>(Arrays.asList("Ocean")), "Regular",
                        new ArrayList<>(Arrays.asList(18)), new ArrayList<>(Arrays.asList(2))),
                new Fish("Catfish", 130, 130, catfishSeason, rainy,
                        new ArrayList<>(Arrays.asList("Forest River", "Farm Map")), "Regular",
                        new ArrayList<>(Arrays.asList(6)), new ArrayList<>(Arrays.asList(22))),
                new Fish("Salmon", 120, 120, fall, both,
                        new ArrayList<>(Arrays.asList("Forest River")), "Regular",
                        new ArrayList<>(Arrays.asList(6)), new ArrayList<>(Arrays.asList(18))),
                new Fish("Angler", 1000, 1000, fall, both,
                        new ArrayList<>(Arrays.asList("Farm Map")), "Legendary",
                        new ArrayList<>(Arrays.asList(8)), new ArrayList<>(Arrays.asList(20))),
                new Fish("Crimsonfish", 1000, 1000, summer, both,
                        new ArrayList<>(Arrays.asList("Ocean")), "Legendary",
                        new ArrayList<>(Arrays.asList(8)), new ArrayList<>(Arrays.asList(20))),
                new Fish("Glacierfish", 1000, 1000, winter, both,
                        new ArrayList<>(Arrays.asList("Forest River")), "Legendary",
                        new ArrayList<>(Arrays.asList(8)), new ArrayList<>(Arrays.asList(20))),
                new Fish("Legend", 1200, 1200, spring, rainy,
                        new ArrayList<>(Arrays.asList("Mountain Lake")), "Legendary",
                        new ArrayList<>(Arrays.asList(8)), new ArrayList<>(Arrays.asList(20)))
        };
        for (Fish f : fishlist) {
            int harga = f.calculateHargaJual();
            f.setHargaJual(harga); // pastikan kamu punya setter `setHargaJual()`
        }

        return fishlist;
    }

    public Fish[] filterFishesBySeasonAndWeather(String season, String weather, String location, int gameHour) {
        ArrayList<Fish> filtered = new ArrayList<>();
        int currentHour = gameHour;

        for (Fish fish : allFishes) {
            boolean seasonMatch = fish.getSeason().contains(season);
            boolean weatherMatch = fish.getWeather().contains(weather);
            boolean locationMatch = fish.getLocation().contains(location);
            boolean timeMatch = false;

            ArrayList<Integer> appearTimes = fish.getAppearTime();
            ArrayList<Integer> disappearTimes = fish.getDisappearTime();

            for (int i = 0; i < appearTimes.size(); i++) {
                int start = appearTimes.get(i);
                int end = disappearTimes.get(i);

                if (start <= end) {
                    if (currentHour >= start && currentHour < end) {
                        timeMatch = true;
                        break;
                    }
                } else {
                    if (currentHour >= start || currentHour < end) {
                        timeMatch = true;
                        break;
                    }
                }
            }

            if (seasonMatch && weatherMatch && locationMatch && timeMatch) {
                filtered.add(fish);
            }
        }

        return filtered.toArray(new Fish[0]);
    }


    public void handleFishingInput(int code) {
        if (gameState != fishingState) return;

        if (fishingTarget == -1) {
            // Setikan ikan dan password saat pertama kali masuk ke fishingState
            Fish[] currentFish = filterFishesBySeasonAndWeather(currentSeason, currentWeather, player.getLocation(), gameHour);
            for (Fish fish : currentFish) {
                System.out.println(fish.getName());
            }
            if (currentFish.length == 0)
                return;

            Fish prize = currentFish[(int) (Math.random() * currentFish.length)];
            fishingTargetFish = prize; // Simpan untuk diberikan saat menang

            int max = 10;
            if (prize.getRarity().equals("Regular"))
                max = 100;
            else if (prize.getRarity().equals("Legendary"))
                max = 500;

            fishingTarget = (int) (Math.random() * max) + 1;
            fishingAttempts = 0;
            maxFishingAttempts = prize.getRarity().equals("Legendary") ? 7 : 10;

            System.out.println(
                    "Tebak angka 1-" + max + " untuk menangkap " + prize.getName() + " (" + prize.getRarity() + ")");
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
                player.fishCaught += 1;
                switch (fishingTargetFish.getRarity()) {
                    case "Common":
                        player.fishCaughtCommon += 1;    
                        break;
                    case "Regular":
                        player.fishCaughtRegular += 1;    
                        break;
                    case "Legendary":
                        player.fishCaughtLegendary += 1;    
                        break;
                    default:
                        break;
                }
                if (player.fishCaught >= 10) {
                    allRecipes[2].setUnlockInfo(true);
                }
                if (fishingTargetFish.getName().equals("Pufferfish")) {
                    allRecipes[3].setUnlockInfo(true);
                } else if (fishingTargetFish.getName().equals("Legend")) {
                    allRecipes[10].setUnlockInfo(true);
                }
                System.out.println("Selamat!! Kamu dapat: " + fishingTargetFish.getName());
                dapet = true;
                gameState = fishingWinState;
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

    private void resetFishing() {
        fishingTarget = -1;
        fishingInput = "";
        fishingAttempts = 0;
        maxFishingAttempts = 0;
        fishingTargetFish = null;
        gameState = playState;
        fishingHint = "";
    }
    public void updateCookingCursor(boolean up, boolean down, boolean left, boolean right) {
        final int maxIndex = 10; // 11 item → index 0–10
        final int ITEMS_PER_ROW = 4;
        final int MAX_ROWS_ON_SCREEN = 5;

        if (up && cookingCursorRow > 0) {
            cookingCursorRow--;
            if (cookingCursorRow < cookingCursorOffset) {
                cookingCursorOffset = cookingCursorRow;
            }
        }

        if (down) {
            int nextIndex = (cookingCursorRow + 1) * ITEMS_PER_ROW + cookingCursorCol;
            if (nextIndex <= maxIndex) {
                cookingCursorRow++;
                if (cookingCursorRow >= cookingCursorOffset + MAX_ROWS_ON_SCREEN) {
                    cookingCursorOffset = cookingCursorRow - MAX_ROWS_ON_SCREEN + 1;
                }
            }
        }

        if (left && cookingCursorCol > 0) {
            cookingCursorCol--;
        }

        if (right) {
            if (cookingCursorCol < ITEMS_PER_ROW - 1) {
                int nextIndex = cookingCursorRow * ITEMS_PER_ROW + cookingCursorCol + 1;
                if (nextIndex <= maxIndex) {
                    cookingCursorCol++;
                }
            }
        }

        // Hindari kursor berada di slot kosong
        int currentIndex = cookingCursorRow * ITEMS_PER_ROW + cookingCursorCol;
        if (currentIndex > maxIndex) {
            cookingCursorCol = maxIndex % ITEMS_PER_ROW;
            cookingCursorRow = maxIndex / ITEMS_PER_ROW;
        }
    }
  
      public Fish getFishByName(String name) {
        if (allFishes != null && name != null) {
            for (Fish f : allFishes) {
                if (f != null && f.getName().equalsIgnoreCase(name)) { // equalsIgnoreCase lebih fleksibel
                    return f;
                }
            }
        }
        System.err.println("PERINGATAN: Ikan dengan nama '" + name + "' tidak ditemukan.");
        return null; // Atau bisa throw exception, atau kembalikan item placeholder
    }

    public NPC[] loadNPCs() {
        NPC[] npcArray = new NPC[6]; // Ganti nama variabel agar tidak sama dengan field kelas
        ArrayList<String> spring = new ArrayList<>(Arrays.asList("Spring"));
        ArrayList<String> summer = new ArrayList<>(Arrays.asList("Summer"));
        ArrayList<String> fall = new ArrayList<>(Arrays.asList("Fall"));
        ArrayList<String> wheatSeason = new ArrayList<>(Arrays.asList("Spring", "Fall"));


        // --NPC1 ----
        loadInitialFish();
        Seeds parsnipSeed = new Seeds("Parsnip Seeds", 10, 20, 1, spring, 13);
        Seeds cauliflowerSeed = new Seeds("Cauliflower Seeds", 40, 80, 5, spring, 14);
        Seeds potatoSeed = new Seeds("Potato Seeds", 25, 50, 3, spring, 15);
        Seeds wheatSeed = new Seeds("Wheat Seeds", 30, 60, 1, wheatSeason, 16);

        Seeds blueberrySeed = new Seeds("Blueberry Seeds", 40, 80, 7, summer, 17);
        Seeds tomatoSeed = new Seeds("Tomato Seeds", 25, 50, 3, summer, 18);
        Seeds hotPepperSeed = new Seeds("Hot Pepper Seeds", 20, 40, 1, summer, 19);
        Seeds melonSeed = new Seeds("Melon Seeds", 40, 80, 4, summer, 20);

        Seeds cranberrySeed = new Seeds("Cranberry Seeds", 50, 100, 2, fall, 21);
        Seeds pumpkinSeed = new Seeds("Pumpkin Seeds", 75, 150, 7, fall, 22);
        Seeds grapeSeed= new Seeds("Grape Seeds", 30, 60, 3, fall, 23);

        Crops parsnip = new Crops("Parsnip", 35, 50, 1);
        Crops cauliflower = new Crops("Cauliflower", 150, 200, 1);
        Crops wheat = new Crops("Wheat", 30, 50, 3);
        Crops blueberry = new Crops("Blueberry", 40, 150, 3);
        Crops tomato = new Crops("Tomato", 60, 90, 1);
        Crops pumpkin = new Crops("Pumpkin", 250, 300, 1);
        Crops grape = new Crops("Grape", 10, 100, 20);
        Crops potato = new Crops("Potato", 50, 70, 1);
        Crops hotPepper = new Crops("Hot Pepper", 80, 100, 1);
        Crops cranberry = new Crops("Cranberry", 100, 120, 1);
        Crops melon = new Crops("Melon", 200, 250, 1);

        Food fishChips = new Food("Fish n' Chips",  135, 150, 50);
        Food baguette = new Food("Baguette", 80, 100, 25);
        Food sashimi = new Food("Sashimi", 275, 300, 70);
        Food wine = new Food("Wine", 90, 100, 20);
        Food pumpkinPie = new Food("Pumpkin Pie", 100, 120, 35);
        Food veggieSoup = new Food("Veggie Soup", 120, 140, 40);
        Food fishStew = new Food("Fish Stew", 260, 280, 70);
        Food fishSandwich = new Food("Fish Sandwich", 180, 200, 50);
        Food pigHead = new Food("Cooked Pig's Head", 0, 1000, 100);
        Food theLegendsOfSpakbor = new Food("The Legends of Spakbor", 500, 600, 100);
        Food fugu = new Food("Fugu", 1000, 1200, 200);
        Food spakborSalad = new Food("Spakbor Salad", 150, 200, 50);


        Misc firewood = new Misc("Firewood", "Sell Price: 10 | Buy Price: 20. Use this to cook a meal." , 10, 20);
        Misc coal = new Misc("Coal", "Sell Price: 20 | Buy Price: 40. Use this to cook 2 meals (the ingredients will be doubled).", 20, 40);
        Misc egg = new Misc("Egg", "Sell Price: 30 | Buy Price: 60. Can be used to make Pumpkin Pie.", 30, 60);
        Misc eggplant = new Misc("Eggplant", "Sell Price: 65 | Buy Price: 135. Can be used to make The Legends of Spakbor.", 65, 130);

        Item[] mtLoved = {
            getFishByName("Legend"),
        };

        Item[] mtLiked = {
            getFishByName("Angler"),
            getFishByName("Crimsonfish"),
            getFishByName("Glacierfish"),
        };

        Item[] mtHated = {};


        // -- NPC2---
        Item[] cLoved = {
            firewood,
            coal
        };
        Item[] cLiked = {
            potato,
            wheat
        };

        Item[] cHated = {
            hotPepper,
        };

        // -- NPC3---
        Item[] pLoved ={
            cranberry, 
            blueberry
        };
        Item[] pLiked = {
            wine
        };
        Item[] pHated = {
            getFishByName("Bullhead"),
            getFishByName("Carp"),
            getFishByName("Chub"),
            getFishByName("Largemouth Bass"),
            getFishByName("Rainbow Trout"),
            getFishByName("Sturgeon"),
            getFishByName("Midnight Carp"),
            getFishByName("Flounder"),
            getFishByName("Halibut"),
            getFishByName("Octopus"),
            getFishByName("Pufferfish"),
            getFishByName("Sardine"),
            getFishByName("Super Cucumber"),
            getFishByName("Catfish"),
            getFishByName("Salmon"),
            getFishByName("Angler"),
            getFishByName("Crimsonfish"),
            getFishByName("Glacierfish"),
            getFishByName("Legend")
        };

        // --NPC 4---
        Item[] dLoved = {
            theLegendsOfSpakbor,
            pigHead,
            wine,
            fugu,
            spakborSalad

        };
        Item[] dLiked = {
            fishSandwich,
            fishStew,
            baguette,
            fishChips
        };
        Item[] dHated = {
            getFishByName("Legend"),
            grape,
            cauliflower,
            wheat,
            getFishByName("Pufferfish"),
            getFishByName("Salmon")
        };

        // --NPC 5---
        Item[] eLoved = {
            parsnipSeed,
            cauliflowerSeed,
            potatoSeed,
            wheatSeed,
            blueberrySeed,
            tomatoSeed,
            hotPepperSeed,
            melonSeed,
            cranberrySeed,
            pumpkinSeed,
            grapeSeed
        };
        Item[] eLiked = {
            getFishByName("Catfish"),
            getFishByName("Salmon"),
            getFishByName("Sardine"),
        };
        Item[] eHated = {
            coal,
            firewood
        };

        Item[] aLoved = {
            blueberry,
            melon,
            pumpkin,
            grape,
            cranberry
        };
        Item[] aLiked = {
            baguette,
            pumpkinPie,
            wine
        };
        Item[] aHated = {
            hotPepper,
            cauliflower,
            parsnip,
            wheat
        };


        npcArray[0] = new NPC(this, "Mayor Tadi", "MTHouse", 7, 3,  
                        mtLoved, mtLiked, mtHated);


        npcArray[1] = new Seller(this, "Caroline", "Caroline's House", 7, 5,  
                        cLoved, cLiked, cHated);


        npcArray[2] = new NPC(this, "Perry", "Perry's House", 7, 7,  
                        pLoved, pLiked, pHated);


        npcArray[3] = new NPC(this, "Dasco", "Dasco's House", 7, 9,  
                        dLoved, dLiked, dHated);

        npcArray[4] = new Seller(this, "Emily", "Emily's Store", 7, 7, eLoved, eLiked, eHated);

        npcArray[5] = new NPC(this, "Abigail", "Abigail's House", 7, 7, aLoved, aLiked, aHated);


        return npcArray;
    }
    public void updateFuelCursor(boolean up, boolean down, boolean left, boolean right) {
        final int maxIndex = 1; 
        final int ITEMS_PER_ROW = 1;
        final int MAX_ROWS_ON_SCREEN = 2;

        if (up && fuelCursorRow > 0) {
            fuelCursorRow--;
            if (fuelCursorRow < fuelCursorOffset) {
                fuelCursorOffset = fuelCursorRow;
            }
        }

        if (down) {
            int nextIndex = (fuelCursorRow + 1) * ITEMS_PER_ROW + fuelCursorCol;
            if (nextIndex <= maxIndex) {
                fuelCursorRow++;
                if (fuelCursorRow >= fuelCursorOffset + MAX_ROWS_ON_SCREEN) {
                    fuelCursorOffset = fuelCursorRow - MAX_ROWS_ON_SCREEN + 1;
                }
            }
        }

        if (left && fuelCursorCol > 0) {
            fuelCursorCol--;
        }

        if (right) {
            if (fuelCursorCol < ITEMS_PER_ROW - 1) {
                int nextIndex = fuelCursorRow * ITEMS_PER_ROW + fuelCursorCol + 1;
                if (nextIndex <= maxIndex) {
                    fuelCursorCol++;
                }
            }
        }

        // Hindari kursor berada di slot kosong
        int currentIndex = fuelCursorRow * ITEMS_PER_ROW + fuelCursorCol;
        if (currentIndex > maxIndex) {
            fuelCursorCol = maxIndex % ITEMS_PER_ROW;
            fuelCursorRow = maxIndex / ITEMS_PER_ROW;
        }
    }
    public void updateFishSelectionCursor(int maxIndex, boolean up, boolean down, boolean left, boolean right) {
        int ITEMS_PER_ROW = 5;
        int SLOT_SIZE = 64;
        int VIEWPORT_HEIGHT = 300; 
        int MAX_ROWS_ON_SCREEN = VIEWPORT_HEIGHT / SLOT_SIZE;
        if (up && fishSelectionRow > 0) {
            fishSelectionRow--;
            if (fishSelectionRow < fishSelectionOffset) {
                fishSelectionOffset = fishSelectionRow;
            }
        }
        if (down) {
            int nextIndex = (fishSelectionRow + 1) * ITEMS_PER_ROW + fishSelectionCol;
            if (nextIndex <= maxIndex) {
                fishSelectionRow++;
                if (fishSelectionRow >= fishSelectionOffset + MAX_ROWS_ON_SCREEN) {
                    fishSelectionOffset = fishSelectionRow - MAX_ROWS_ON_SCREEN + 1;
                }
            }
        }
        if (left && fishSelectionCol > 0) {
            fishSelectionCol--;
        }
        if (right) {
            if (fishSelectionCol < ITEMS_PER_ROW - 1) {
                int nextIndex = fishSelectionRow * ITEMS_PER_ROW + fishSelectionCol + 1;
                if (nextIndex <= maxIndex) {
                    fishSelectionCol++;
                }
            }
        }

        if ((fishSelectionRow * ITEMS_PER_ROW + fishSelectionCol) > maxIndex) {
            fishSelectionCol = maxIndex % ITEMS_PER_ROW;
            fishSelectionRow = maxIndex / ITEMS_PER_ROW;
        }
    }

    public void setupGame(){

        gameState = titleState;
    }

    public void startGameThread() {
        gameThread = new Thread(this); // Create a new thread for the game loop
        gameThread.start(); // Start the game loop thread
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
                    for (NPC npc : npcs) {
                        if (npc instanceof Seller) {
                            Seller seller =(Seller) npc;
                            seller.getInventory().getItems().clear();
                            seller.getInventory().getItemContainer().clear();
                            if (seller.getName().equals("Emily")) {
                                seller.loadInitialCrops();
                                seller.loadInitialFood();
                                seller.loadInitialSeeds();
                                seller.loadProposalRing();
                            } else if (seller.getName().equals("Caroline")) {
                                seller.loadMisc();
                            }
                        }
                    }
                }
            }
            map.updateTiles(); 
        }
    }

    @Override
    public void run() {
        // Game loop logic here
        while (gameThread != null) {

            // Update game state
            update();
            // Repaint screen
            repaint();

            // Delay to simulate FPS
            try {
                Thread.sleep(16); // roughly 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if(gameDay > lastday) {
            System.out.println("******************");
            System.out.println("Day Has Been Reset.");
            System.out.println("Current Player Money: " + player.getMoney());
            System.out.println("Current Player Stored Money: " + player.getStoredMoney());
            lastday = gameDay;
            player.currSB.clearBin();
            player.setMoneyX(player.getMoney() + player.getStoredMoney());
            player.totalIncome += player.getStoredMoney();
            player.setStoredMoney(0);
            System.out.println("____________________");
            System.out.println("Player: Stored money. New stored money: " + player.getStoredMoney());
            System.out.println("Player: Money added from stored money. Current money: " + player.getMoney());
            System.out.println("Emptied Shipping Bin");
            System.out.println("******************");
        }

        if (gameState == titleState) {
            if (keyHandler.enterPressed) {
                if (titlePage.commandNumber == 3) {
                    System.exit(0);
                } else if (titlePage.commandNumber == 0) {
                    gameState = farmNameInputState;
                    keyHandler.enterPressed = false;
                } else if (titlePage.commandNumber == 1) {
                    gameState = helpState;
                    keyHandler.enterPressed = false;
                } else if (titlePage.commandNumber == 2) {
                    gameState = creditsState;
                    keyHandler.enterPressed = false;
                }
            }
        } else if (gameState == farmNameInputState) {
            if (keyHandler.enterPressed) {
                if (farmName.commandNumber == 0 && !farmName.farmNameInput.trim().isEmpty()) {
                    player.setFarmName(farmName.farmNameInput.trim());
                    gameState = playerNameInputState;
                } else if (farmName.commandNumber == 1) {
                    gameState = titleState;
                }
                keyHandler.enterPressed = false;
            }
        }
        else if (gameState == playerNameInputState) {
            if (keyHandler.enterPressed) {
                if (!playerInput.playerNameInput.trim().isEmpty()
                        && (playerInput.selectedGender.equals("Male") || playerInput.selectedGender.equals("Female"))) {
                    player.setPlayerName(playerInput.playerNameInput.trim());
                    player.setGender(playerInput.selectedGender);
                    setRainDaysForSeason();
                    gameState = playState;
                }
                keyHandler.enterPressed = false;
            }
        }
        player.update();
        for (NPC npc : npcs) {
            if (npc != null) {
                if (npc.getSpawnMapName() == player.getLocation()) {
                    npc.update(); 
                }
            }
        }
        if (keyHandler.f1Pressed) {
            debugMode = !debugMode;
            keyHandler.f1Pressed = false; 
            System.out.println("Debug mode: " + (debugMode ? "ON" : "OFF"));
        }
        if (keyHandler.invPressed && (seller == null || (seller != null && !seller.isBuying))) {
            if (gameState == playState) {
                gameState = inventoryState;
            } else if (gameState == inventoryState || gameState == itemOptionState) {
                gameState = playState;
            }
            keyHandler.invPressed = false;
        }
        if (keyHandler.escapePressed) {
            if (gameState == playState){
                gameState = menuState;
            } else if (gameState == menuState) {
                gameState = playState;
            }
            keyHandler.escapePressed = false; 
        }
        if (keyHandler.rPressed){
            Rectangle soliddArea = new Rectangle(8, 16, 32, 32);
            int playerCurrentTileCol = (player.x + soliddArea.x + soliddArea.width / 2) / tileSize; 
            int playerCurrentTileRow = (player.y + soliddArea.y + soliddArea.height / 2) / tileSize;
            int targetTileCol = playerCurrentTileCol;
            int targetTileRow = playerCurrentTileRow;

            String lastMoveDirectionz = player.getLastMoveDirection();

            switch (lastMoveDirectionz) {
                case "up": targetTileRow--; break;
                case "down": targetTileRow++; break;
                case "left": targetTileCol--; break;
                case "right": targetTileCol++; break;
            }

            Rectangle interactionAreaz = new Rectangle(0, 0, tileSize, tileSize);
            interactionAreaz.x = targetTileCol * tileSize;
            interactionAreaz.y = targetTileRow * tileSize;

            Tile tileToInteractz = map.getTile(interactionAreaz.x, interactionAreaz.y);

            if (tileToInteractz instanceof ShippingBin){
                if (gameState == playState) {
                    gameState = shippingState;
                } else if (gameState == shippingState || gameState == shippingOptionState) {
                    addMinutes(15);
                    gameState = playState;
                    player.checkerstate = 0;
                }
                keyHandler.rPressed = false;
            }
            else{
                System.out.println("Tidak ada interaksi shipping bin yang tersedia di sini.");
            }
        }
        if (keyHandler.tPressed){
            Rectangle soliddArea = new Rectangle(8, 16, 32, 32);
            int playerCurrentTileCol = (player.x + soliddArea.x + soliddArea.width / 2) / tileSize; 
            int playerCurrentTileRow = (player.y + soliddArea.y + soliddArea.height / 2) / tileSize;
            int targetTileCol = playerCurrentTileCol;
            int targetTileRow = playerCurrentTileRow;

            String lastMoveDirectionz = player.getLastMoveDirection();

            switch (lastMoveDirectionz) {
                case "up": targetTileRow--; break;
                case "down": targetTileRow++; break;
                case "left": targetTileCol--; break;
                case "right": targetTileCol++; break;
            }

            Rectangle interactionAreaz = new Rectangle(0, 0, tileSize, tileSize);
            interactionAreaz.x = targetTileCol * tileSize;
            interactionAreaz.y = targetTileRow * tileSize;

            Tile tileToInteractz = map.getTile(interactionAreaz.x, interactionAreaz.y);

            if (tileToInteractz instanceof ShippingBin){
                if (gameState == playState) {
                    gameState = viewShippingState;
                } else if (gameState == viewShippingState) {
                    gameState = playState;
                }
                keyHandler.tPressed = false;
            }
            else{
                System.out.println("Tidak ada interaksi shipping bin yang tersedia di sini.");
            }
        }
        if (gameState == playState) {
            player.tiling();
            player.recoverLand();
            player.planting();
            checkMapTransitions();
            player.watering();
            player.harvesting();
            player.fishing();
            player.sleeping();
            player.cooking();
            player.watching();
            player.interactingWithNPC();
        } else if (gameState == dialogState) {
            if (keyHandler.enterPressed) {
                if (player.currentNPC != null && player.currentNPC.isTalking) {
                    player.currentNPC.currentDialogueIndex++;

                    if (player.currentNPC.currentDialogueIndex >= player.currentNPC.dialogues.length) {
                        player.currentNPC.currentDialogueIndex = 0;
                        player.currentNPC.isTalking = false;
                        // player.currentNPC.showActionMenu = true;
                    }

                    keyHandler.enterPressed = false;
                } else if (player.currentNPC != null && player.currentNPC.isProposed) {
                    player.currentNPC.isProposed = false;
                    keyHandler.enterPressed = false;
                } else if (player.currentNPC != null && player.currentNPC.isGifted){
                    player.getInventory().removeItem(player.getInventory().getSelectedItem(), 1);
                    player.currentNPC.isGifted = false;
                    keyHandler.enterPressed = false;
                } else if (player.currentNPC != null && player.currentNPC.isMarried){
                    player.currentNPC.isMarried = false;
                    keyHandler.enterPressed = false;

                } else {
                    String action = player.currentNPC.confirmAction();
                    if (player.currentNPC != null && player.currentNPC instanceof Seller) {
                        seller = (Seller) player.currentNPC;
                        action = seller.confirmAction();
                    }
                    if (action.equalsIgnoreCase("Talk")) {
                        player.currentNPC.isTalking = true;
                        player.currentNPC.currentDialogueIndex = 0;
                        player.energyReducedInThisChat = false;
                        // player.currentNPC.showActionMenu = false;
                    } else if (action.equalsIgnoreCase("Leave")) {
                        gameState = playState;
                    } else if (action.equalsIgnoreCase("Propose")){
                        player.currentNPC.isProposed = true;
                        player.energyReducedInThisChat = false;
                    } else if (action.equalsIgnoreCase("Give")) {
                        player.currentNPC.isGifted = true;
                        gameState = shippingState;
                        player.energyReducedInThisChat = false;
                    } else if (action.equalsIgnoreCase("Marry")){
                        player.currentNPC.isMarried = true;
                        player.energyReducedInThisChat = false;

                    }else if (seller != null && player.currentNPC instanceof Seller && action.equalsIgnoreCase("Buy") && !seller.getInventory().getItemContainer().isEmpty()) {
                        seller.isBuying = true;
                        gameState = inventoryState;
                    }
                    keyHandler.enterPressed = false;
                }
        }
    }
        if (gameState == inventoryState && (seller == null || (seller != null && !seller.isBuying))) {
            player.getInventory().updateInventoryCursor(
                    keyHandler.upPressed,
                    keyHandler.downPressed,
                    keyHandler.leftPressed,
                    keyHandler.rightPressed);

            keyHandler.upPressed = false;
            keyHandler.downPressed = false;
            keyHandler.leftPressed = false;
            keyHandler.rightPressed = false;

            if (keyHandler.enterPressed) {
                player.getInventory().selectCurrentItem();
                keyHandler.enterPressed = false;
            }
            player.sleeping();
        } else if (gameState == inventoryState && seller != null && seller.isBuying) {
            seller.getInventory().updateInventoryCursor(
                    keyHandler.upPressed,
                    keyHandler.downPressed,
                    keyHandler.leftPressed,
                    keyHandler.rightPressed);

            keyHandler.upPressed = false;
            keyHandler.downPressed = false;
            keyHandler.leftPressed = false;
            keyHandler.rightPressed = false;

            if (keyHandler.enterPressed) {
                seller.getInventory().selectCurrentItem();
                keyHandler.enterPressed = false;
            }
            player.sleeping();

        } else if (gameState == itemOptionState && (seller == null || (seller != null && !seller.isBuying))) {
            if (keyHandler.upPressed) {
                player.getInventory().optionCommandNum = (player.getInventory().optionCommandNum - 1 + 3) % 3;
                keyHandler.upPressed = false;
            }
            if (keyHandler.downPressed) {
                player.getInventory().optionCommandNum = (player.getInventory().optionCommandNum + 1) % 3;
                keyHandler.downPressed = false;
            }

            if (gameState == itemOptionState) {
                if (keyHandler.enterPressed) {
                    Item selected = player.getInventory().getSelectedItem();
                    if (selected instanceof Equipment) {
                        Equipment eq = (Equipment) selected;
                        if (player.getInventory().optionCommandNum == 0) {
                            if (player.getEquippedItem() == eq) {
                                player.equipItem(null);
                            } else {
                                player.equipItem(eq);
                            }
                            gameState = inventoryState;
                        } else if (player.getInventory().optionCommandNum == 1) {
                            gameState = inventoryState; 
                        }
                    } else if (selected instanceof Seeds) {
                        Seeds eq = (Seeds) selected;
                        if (player.getInventory().optionCommandNum == 0) {
                            if (player.getEquippedItem() == eq) {
                                player.equipItem(null);
                            } else {
                                player.equipItem(eq);
                            }
                            gameState = inventoryState;
                        } else if (player.getInventory().optionCommandNum == 1) {
                            gameState = inventoryState; 
                        }
                    } else if (selected instanceof Edible) {
                        if (player.getInventory().optionCommandNum == 0) {
                            player.eating();
                            gameState = inventoryState;
                        } else if (player.getInventory().optionCommandNum == 1) {
                            gameState = inventoryState; 
                        }
                    } else if (selected instanceof Misc) {
                        if (player.getInventory().optionCommandNum == 0) {
                            gameState = inventoryState;
                        }
                    }
                    keyHandler.enterPressed = false;
                }
            }
            player.sleeping();
        } else if (gameState == itemOptionState && seller != null && seller.isBuying) {
            if (keyHandler.upPressed) {
                seller.getInventory().optionCommandNum = (seller.getInventory().optionCommandNum - 1 + 3) % 3;
                keyHandler.upPressed = false;
            }
            if (keyHandler.downPressed) {
                seller.getInventory().optionCommandNum = (seller.getInventory().optionCommandNum + 1) % 3;
                keyHandler.downPressed = false;
            }

            if (gameState == itemOptionState) {
                if (keyHandler.enterPressed) {
                    Item selected = seller.getInventory().getSelectedItem();
                    if (selected instanceof Buyable) {
                        Buyable eq = (Buyable) selected;
                        if (seller.getInventory().optionCommandNum == 0) {
                            eq.buy(this, selected, 1);
                            gameState = playState;
                            seller.isBuying = false;
                            seller = null;
                        } else if (seller.getInventory().optionCommandNum == 1) {
                            eq.buy(this, selected, seller.getInventory().getItemCount(selected));
                            gameState = playState;
                            seller.isBuying = false;
                            seller = null;
                        } else if (seller.getInventory().optionCommandNum == 2) {
                            gameState = playState;
                            seller.isBuying = false;
                            seller = null;
                        }
                    } 
                    keyHandler.enterPressed = false;
                }
            }
            player.sleeping();
        }
        if (gameState == viewShippingState) {
            player.currSB.getInventory().updateInventoryCursor(
                keyHandler.upPressed,
                keyHandler.downPressed,
                keyHandler.leftPressed,
                keyHandler.rightPressed
            );

            keyHandler.upPressed = false;
            keyHandler.downPressed = false;
            keyHandler.leftPressed = false;
            keyHandler.rightPressed = false;

            if (keyHandler.enterPressed) {
                keyHandler.enterPressed = false;
            }
            player.sleeping();
        }
        if (gameState == shippingState) {
            player.getInventory().updateInventoryCursor(
                keyHandler.upPressed,
                keyHandler.downPressed,
                keyHandler.leftPressed,
                keyHandler.rightPressed
            );

            keyHandler.upPressed = false;
            keyHandler.downPressed = false;
            keyHandler.leftPressed = false;
            keyHandler.rightPressed = false;

            if (keyHandler.enterPressed) {
                player.getInventory().selectCurrentItemShipping();
                keyHandler.enterPressed = false;
            }
            player.sleeping();
        }
        else if (gameState == shippingOptionState) {
            if (keyHandler.upPressed) {
                player.getInventory().optionCommandNum = (player.getInventory().optionCommandNum - 1 + 3) % 3;
                keyHandler.upPressed = false;
            }
            if (keyHandler.downPressed) {
                player.getInventory().optionCommandNum = (player.getInventory().optionCommandNum + 1) % 3;
                keyHandler.downPressed = false;
            }

            if (gameState == shippingOptionState && (player.currentNPC == null || (player.currentNPC != null && !player.currentNPC.isGifted))) {
                if (keyHandler.enterPressed) {
                    Item selected = player.getInventory().getSelectedItem();
                    if(selected instanceof Sellable){
                        if (player.getInventory().optionCommandNum == 0) {
                            player.selling();
                            gameState = shippingState;
                        } else if (player.getInventory().optionCommandNum == 1) {
                            gameState = shippingState; 
                        }
                    }
                    else {
                        gameState = playState;
                    }
                    keyHandler.enterPressed = false;
                }
            } else if (gameState == shippingOptionState  &&  player.currentNPC != null && player.currentNPC.isGifted) {
                if (keyHandler.enterPressed) {
                    Item selected = player.getInventory().getSelectedItem();
                    if(!(selected instanceof Equipment)){
                        if (player.getInventory().optionCommandNum == 0) {
                            gameState = dialogState;
                        } else if (player.getInventory().optionCommandNum == 1) {
                            player.currentNPC.isGifted = false;
                            gameState = dialogState; 
                        }
                    } else {
                        player.currentNPC.isGifted = false;
                        gameState = dialogState;
                    }
                    keyHandler.enterPressed = false;
                }
            }
            player.sleeping();
        } else if (gameState == dialogState) {
            player.currentNPC.selectAction(keyHandler.leftPressed, keyHandler.rightPressed);
            keyHandler.leftPressed = false;
            keyHandler.rightPressed = false;
        }
        if (gameState == fishingState) {
            if (keyHandler.enterPressed) {
                gameState = playState;
                keyHandler.enterPressed = false;
            }
        }

        if (gameState == cookingState) {
            updateCookingCursor(
                keyHandler.upPressed,
                keyHandler.downPressed,
                keyHandler.leftPressed,
                keyHandler.rightPressed
            );

            keyHandler.upPressed = false;
            keyHandler.downPressed = false;
            keyHandler.leftPressed = false;
            keyHandler.rightPressed = false;

            if (keyHandler.enterPressed) {
                keyHandler.enterPressed = false;

                Recipe selectedRecipe = allRecipes[cookingCursorRow * 4 + cookingCursorCol];
                if (!selectedRecipe.getUnlockInfo()) {
                    gameState = playState;
                    return;
                }

                if (activeStove != null && activeStove.getFuel() != null && activeStove.getcookingFuel() > 0) {
                    String fuelName = activeStove.getFuel().getName();
                    int timesToCook = fuelName.equals("Coal") ? 2 : 1;

                    HashMap<Item, Integer> ingredients = selectedRecipe.getIngredients();
                    boolean hasAllIngredients = true;
                    HashMap<Item, Integer> actualIngredientsToUse = new HashMap<>();

                    for (Item item : ingredients.keySet()) {
                        int required = ingredients.get(item) * timesToCook;

                        if (item.getName().equals("Any Fish")) {
                            if (player.getInventory().hasItemOfClass(Fish.class, required)) {
                                pendingRecipe = selectedRecipe;
                                requiredFishAmount = required;
                                gameState = fishSelectionState;
                                return;
                            } else {
                                hasAllIngredients = false;
                                gameState = playState;
                                break;
                            }
                        } else {
                            if (player.getInventory().hasItem(item) && player.getInventory().getItemCount(item) >= required) {
                                actualIngredientsToUse.put(item, required);
                            } else {
                                hasAllIngredients = false;
                                gameState = playState;
                                break;
                            }
                        }
                    }

                    if (hasAllIngredients) {
                        for (Item item : actualIngredientsToUse.keySet()) {
                            player.getInventory().removeItem(item, actualIngredientsToUse.get(item));
                        }
                        activeStove.cook(this, selectedRecipe.getFood());
                    }
                    if (activeStove.getcookingFuel() <= 0) {
                        gameState = playState;
                    }
                } else {
                    gameState = playState;
                }
            }
            player.sleeping();
        }
        if (gameState == fuelState) {
            updateFuelCursor(
                keyHandler.upPressed,
                keyHandler.downPressed,
                keyHandler.leftPressed,
                keyHandler.rightPressed
            );

            keyHandler.upPressed = false;
            keyHandler.downPressed = false;
            keyHandler.leftPressed = false;
            keyHandler.rightPressed = false;
            if(keyHandler.enterPressed) {
                gameState = playState;
                keyHandler.enterPressed = false;
            } else if (keyHandler.fpressed) {
                Misc selectedFuel = fuels[fuelCursorRow]; 
                if (player.getInventory().getItem(selectedFuel) != null) {
                    if(activeStove.getFuel() == null) {
                        activeStove.isiFuel(selectedFuel);
                        player.getInventory().removeItem(selectedFuel, 1); 
                    } else if (activeStove.getFuel().getName().equals("Firewood") && selectedFuel.getName().equals("Coal")) {
                        player.getInventory().addItem(activeStove.getFuel(), 1);
                        activeStove.isiFuel(selectedFuel);
                        player.getInventory().removeItem(selectedFuel, 1); 
                    } else if (activeStove.getFuel().getName().equals("Coal") && selectedFuel.getName().equals("Firewood") && activeStove.getcookingFuel() > 1) {
                        player.getInventory().addItem(activeStove.getFuel(), 1);
                        activeStove.isiFuel(selectedFuel);
                        player.getInventory().removeItem(selectedFuel, 1); 
                    }
                }
                keyHandler.fpressed = false;
            }
            player.sleeping();
        }
        if (gameState == fishSelectionState) {
            HashMap<Item, Integer> fishes = player.getInventory().getAllItemOfClass(Fish.class);
            List<Item> fishList = new ArrayList<>(fishes.keySet());
            updateFishSelectionCursor( fishes.size() - 1,
                keyHandler.upPressed,
                keyHandler.downPressed,
                keyHandler.leftPressed,
                keyHandler.rightPressed
            );

            keyHandler.upPressed = false;
            keyHandler.downPressed = false;
            keyHandler.leftPressed = false;
            keyHandler.rightPressed = false;
            if (keyHandler.enterPressed) {
                Item selectedFish = fishList.get(fishSelectionRow * 4 + fishSelectionCol);
                if (player.getInventory().getItemCount(selectedFish) >= requiredFishAmount) {
                    player.getInventory().removeItem(selectedFish, requiredFishAmount);
                    HashMap<Item, Integer> otherIngredients = pendingRecipe.getIngredients();
                    for (Item item : otherIngredients.keySet()) {
                        if (!item.getName().equals("Any Fish")) {
                            player.getInventory().removeItem(item, otherIngredients.get(item));
                        }
                    }
                    activeStove.cook(this, pendingRecipe.getFood());
                    gameState = playState;
                }
                keyHandler.enterPressed = false;
            }
            player.sleeping();
        }
        if (gameState == watchingState) {
            if(keyHandler.enterPressed) {
                gameState = playState;
                keyHandler.enterPressed = false;
            }
            player.sleeping();
        }
        if (gameState == fishingWinState) {
            if(keyHandler.enterPressed) {
                resetFishing();
                gameState = playState;
                keyHandler.enterPressed = false;
            }
            player.sleeping();
        }

        long now = System.currentTimeMillis();
        if (now - lastRealTime >= REAL_TIME_INTERVAL && gameState != fishingState && gameState != shippingState && gameState != shippingOptionState && gameState >= playState) {
            gameMinute += 5;
            if (gameMinute >= 60) {
                gameMinute = 0;
                gameHour++;
                if (gameHour >= 24) {
                    gameHour = 0;
                    gameDay++;
                    daysPlayed++;
                    for (NPC npc : npcs) {
                        if (npc instanceof Seller) {
                            Seller seller =(Seller) npc;
                            seller.getInventory().getItems().clear();
                            seller.getInventory().getItemContainer().clear();
                            if (seller.getName().equals("Emily")) {
                                seller.loadInitialCrops();
                                seller.loadInitialFood();
                                seller.loadInitialSeeds();
                                seller.loadProposalRing();
                            } else if (seller.getName().equals("Caroline")) {
                                seller.loadMisc();
                            }
                        }
                    }
                }
            }
            map.updateTiles();
            lastRealTime = now;
        }

        if (gameDay > 10) {
            currentSeasonIndex = (currentSeasonIndex + 1) % 4;
            currentSeason = initialSeason[currentSeasonIndex];
            gameDay %= 10;
            setRainDaysForSeason();
        }

        if (gameDay == rainDaysInSeason[0] || gameDay == rainDaysInSeason[1]) {
            currentWeather = initialWeather[0]; 
        } else {
            currentWeather = initialWeather[1];
        }
        
        if (currentWeather.equals("Rainy") && !player.getLocation().contains("House") && !player.getLocation().equals("Emily's Store")) {
            for (RainDrop drop : rainDrops) {
                drop.update();
            }
        }

        if ((player.getMoney() >= 17209 || player.getPartner() != null) && !reachedEndgame) {
            gameState = statisticsState;
            reachedEndgame = true;
        }
    }
    public Graphics2D getGraphics2D(){
        java.awt.Graphics g = this.getGraphics();
        if (g == null) {
            System.err.println("Gagal mendapatkan Graphics untuk GamePanel.");
            return null;
        }
        return (java.awt.Graphics2D) g.create(); 
    }

    public void drawFishingWindow(Graphics2D g2) {
        int frameX = tileSize;
        int frameY = tileSize * 2;
        int frameWidth = tileSize * 6;
        int frameHeight = tileSize * 5;

        Color backgroundColor = new Color(0, 0, 0, 210);
        g2.setColor(backgroundColor);
        g2.fillRoundRect(frameX, frameY, frameWidth, frameHeight, 35, 35);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(frameX + 5, frameY + 5, frameWidth - 10, frameHeight - 10, 25, 25);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24F));
        int debugY = frameY + 130;
        if (fishingTargetFish != null) {
            switch (fishingTargetFish.getRarity()) {
                case "Common":
                    g2.setColor(new Color(169, 169, 169));
                    g2.drawString("Attempt: " + fishingAttempts + " / " + maxFishingAttempts, frameX + 20, debugY);
                    debugY += 25;
                    g2.drawString("A Common Catch!", frameX + 20, frameY + 50);
                    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
                    g2.drawString("Input 1-10 to Catch!", frameX + 20, debugY);
                    break;
                case "Regular":
                    g2.setColor(new Color(30, 144, 255));
                    g2.drawString("Attempt: " + fishingAttempts + " / " + maxFishingAttempts, frameX + 20, debugY);
                    debugY += 25;
                    g2.drawString("Lucky Catch!", frameX + 20, frameY + 50);
                    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
                    g2.drawString("Input 1-100 to Catch!", frameX + 20, debugY);
                    break;
                case "Legendary":
                    g2.setColor(new Color(218, 165, 32));
                    g2.drawString("Attempt: " + fishingAttempts + " / " + maxFishingAttempts, frameX + 20, debugY);
                    debugY += 25;
                    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 16F));
                    g2.drawString("Unknown Presence Awakened...", frameX + 20, frameY + 50);
                    g2.drawString("Input 1–500 to Uncover the Mystery", frameX + 20, debugY);
                    break;
                default:
                    break;
            }
            debugY += 25;
        } else {
            debugY += 25;
            g2.drawString("Mulai Memancing!!!", frameX + 20, debugY);
            debugY += 25;
        }

        if (fishingHint != null && !fishingHint.isEmpty()) {
            g2.setColor(Color.YELLOW);
            g2.drawString("Hint: " + fishingHint, frameX + 20, debugY);
        }
        if (debugMode) {
            if (fishingTarget != -1) {
                g2.drawString("Target Code: " + fishingTarget, frameX + 20, debugY + 25);
            }
        }
        g2.drawString(fishingInput, frameX + 20, frameY + 90);
    }

    public void drawCookingWindow(Graphics2D g2) {
        int frameX = tileSize;
        int frameY = tileSize * 2;
        int frameWidth = tileSize * 5;
        int frameHeight = tileSize * 4;

        Color backgroundColor = new Color(0, 0, 0, 210);
        g2.setColor(backgroundColor);
        g2.fillRoundRect(frameX, frameY, frameWidth, frameHeight, 35, 35);
        g2.fillRoundRect(frameX + 350, frameY, frameWidth, frameHeight + 100, 35, 35);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(frameX + 5, frameY + 5, frameWidth - 10, frameHeight - 10, 25, 25);
        g2.drawRoundRect(frameX + 5 + 350, frameY + 5, frameWidth - 10, frameHeight - 10 + 100, 25, 25);


        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24F));
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;

        int cursorX = slotXStart + (tileSize * cookingCursorCol);
        int cursorY = slotYStart + (tileSize * (cookingCursorRow - cookingCursorOffset));
        int cursorWidth = tileSize;
        int cursorHeight = tileSize;

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10); 
        int index = 0;
        for (Recipe recipe : allRecipes) {
            int row = index / 4;

            if (row < cookingCursorOffset) {
                index++;
                continue; 
            }

            if (row >= cookingCursorOffset + 300 / 64) {
                break;
            }

            int col = index % 4;
            int itemX = slotXStart + col * tileSize;
            int itemY = slotYStart + (row - cookingCursorOffset) * tileSize; 

            if (recipe.getFood().getIcon() != null) {
                int padding = 6;
                int drawSize = tileSize - 2 * padding;
                int drawX = itemX + padding;
                int drawY = itemY + padding;

                if (recipe.getUnlockInfo()) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // normal
                } else {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f)); // lebih transparan
                }

                g2.drawImage(recipe.getFood().getIcon(), drawX, drawY, drawSize, drawSize, null);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // reset ke normal
            }
            index++;
        }
        int selectedIndex = cookingCursorRow * 4 + cookingCursorCol;
        if (selectedIndex >= 0 && selectedIndex < allRecipes.length) {
            Recipe selectedRecipe = allRecipes[selectedIndex];

            int detailX = frameX + 370;
            int detailY = frameY + 20;
            int detailGapY = tileSize;

            int ingIndex = 0;
            for (Item item : selectedRecipe.getIngredients().keySet()) {
                int amount = selectedRecipe.getIngredients().get(item);
                if (activeStove != null && activeStove.getFuel() != null && activeStove.getFuel().getName().equals("Coal")) {
                    amount *= 2;
                }
                int iconSize = tileSize - 8;
                int iconX = detailX;
                int iconY = detailY + ingIndex * detailGapY;

                boolean enough = false;

                if (item.getName().equals("Any Fish")) {
                    enough = player.getInventory().hasItemOfClass(Fish.class, amount);
                } else {
                    enough = player.getInventory().getItem(item) != null &&
                            player.getInventory().getItemCount(item) >= amount;
                }

                if (item.getIcon() != null) {
                    g2.drawImage(item.getIcon(), iconX, iconY, iconSize, iconSize, null);
                }

                g2.setColor(enough ? Color.white : Color.red);
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
                String displayName = item.getName();
                g2.drawString(displayName + " x" + amount, iconX + iconSize + 10, iconY + 24);
                ingIndex++;
            }
        }
    }
   public void drawFuelWindow(Graphics2D g2) {
        int frameX = tileSize;
        int frameY = tileSize * 2;
        int frameWidth = tileSize * 4;
        int frameHeight = tileSize * 3;

        Color backgroundColor = new Color(0, 0, 0, 210);
        g2.setColor(backgroundColor);
        g2.fillRoundRect(frameX + 250, frameY, frameWidth, frameHeight, 35, 35);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(frameX + 5 + 250, frameY + 5, frameWidth - 10, frameHeight - 10, 25, 25);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24F));
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));

        final int slotXStart = frameX + 20 + 250;
        final int slotYStart = frameY + 20;

        // Batasan cursor untuk 2 item
        if (fuelCursorRow < 0) fuelCursorRow = 0;
        if (fuelCursorRow > 1) fuelCursorRow = 1;

        // DRAW CURSOR (vertikal)
        int cursorX = slotXStart;
        int cursorY = slotYStart + fuelCursorRow * tileSize;
        int cursorWidth = tileSize;
        int cursorHeight = tileSize;

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10); 

        // DRAW FUELS (hanya 2 item, vertikal)
        for (int i = 0; i < fuels.length && i < 2; i++) {
            Misc fuel = fuels[i];
            int itemX = slotXStart;
            int itemY = slotYStart + i * tileSize;

            if (fuel.getIcon() != null) {
                int padding = 6;
                int drawSize = tileSize - 2 * padding;
                int drawX = itemX + padding;
                int drawY = itemY + padding;

                g2.drawImage(fuel.getIcon(), drawX, drawY, drawSize, drawSize, null);
                if (player.getInventory().getItem(fuel) == null) {
                    g2.setColor(Color.red);
                } else {
                    g2.setColor(Color.white);
                }
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
                g2.drawString(fuel.getName() + " x" + 1, drawX + drawSize + 10, drawY + 24);
            }
        }
        int slotWidth = tileSize;
        int slotHeight = tileSize;
        int slotX = frameX + 250 + (frameWidth / 2) - (slotWidth / 2); 
        int slotY = frameY + frameHeight - tileSize + 50;
        g2.setColor(backgroundColor);
        g2.fillRoundRect(slotX, slotY, slotWidth, slotHeight, 15, 15);
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(slotX + 5, slotY + 5, slotWidth - 10, slotHeight - 10, 10, 10);

        if (activeStove != null && activeStove.getFuel() != null) {
            Misc fuel = activeStove.getFuel();
            if (fuel.getIcon() != null) {
                int padding = 6;
                int drawSize = tileSize - 2 * padding;
                int drawX = slotX + padding;
                int drawY = slotY + padding;
                g2.drawImage(fuel.getIcon(), drawX, drawY, drawSize, drawSize, null);
            }
        }
    }

    public void drawFishSelection(Graphics2D g2) {
        // FRAME UKURAN & POSISI
        int frameX = tileSize * 9;
        int frameY = tileSize;
        int frameWidth = tileSize * 6;
        int frameHeight = tileSize * 5;
        Color c = new Color(0,0,0, 210);
        g2.setColor(c);
        g2.fillRoundRect(frameX, frameY, frameWidth, frameHeight, 35, 35);
        c = new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(frameX+5, frameY+5, frameWidth-10, frameHeight-10, 25, 25);

        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;

        int cursorX = slotXStart + (tileSize * fishSelectionCol);
        int cursorY = slotYStart + (tileSize * (fishSelectionRow - fishSelectionOffset));
        int cursorWidth = tileSize;
        int cursorHeight = tileSize;

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);
        HashMap<Item, Integer> fishes = player.getInventory().getAllItemOfClass(Fish.class);
        List<Item> fishList = new ArrayList<>(fishes.keySet());

        int ITEMS_PER_ROW = 5;
        int SLOT_SIZE = 64;
        int VIEWPORT_HEIGHT = 300; 
        int MAX_ROWS_ON_SCREEN = VIEWPORT_HEIGHT / SLOT_SIZE;
        int index = 0;
        for (Item item : fishList) {
            int row = index / ITEMS_PER_ROW;

            if (row < fishSelectionOffset) {
                index++;
                continue; // Lewati baris di atas viewport
            }

            if (row >= fishSelectionOffset + MAX_ROWS_ON_SCREEN) {
                break; // Hentikan kalau sudah melebihi viewport
            }

            int col = index % ITEMS_PER_ROW;
            int itemX = slotXStart + col * tileSize;
            int itemY = slotYStart + (row - fishSelectionOffset) * tileSize; // kurangi offset agar scroll naik

            if (item.getIcon() != null) {
                int padding = 6;
                int drawSize = tileSize - 2 * padding;
                int drawX = itemX + padding;
                int drawY = itemY + padding;

                g2.drawImage(item.getIcon(), drawX, drawY, drawSize, drawSize, null);
            }

            Integer count = fishes.get(item);
            if (count != null && count > 1) {
                g2.setColor(Color.white);
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                String countStr = String.valueOf(count);
                int stringWidth = g2.getFontMetrics().stringWidth(countStr);
                g2.drawString(countStr, itemX + tileSize - stringWidth - 4, itemY + tileSize - 4);
            }

            index++;
        }
    }

    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (gameState == titleState) {
            titlePage.draw(g2);
            g2.dispose();
            return;
        } else if (gameState == farmNameInputState) {
            farmName.draw(g2);
            g2.dispose();
            return;
        } else if (gameState == helpState) {
            help.draw(g2);
            g2.dispose();
            return;
        } else if (gameState == creditsState) {
            credits.draw(g2);
            g2.dispose();
            return;
        } else if (gameState == playerNameInputState) {
            playerInput.draw(g2);
            g2.dispose();
            return;
        } if (map.currentMapID == 3) { // Ganti angka 3 jika ID peta rumah Anda berbeda
                g2.setColor(java.awt.Color.black); // Atur latar belakang menjadi hitam untuk rumah
                g2.fillRect(0, 0, screenWidth, screenHeight);
            } else {
                // Jika bukan peta rumah, gambar latar belakang luar ruangan seperti biasa
            if (backgroundImage != null) {
                g2.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight, null);
            } else {
                g2.setColor(java.awt.Color.cyan); // Latar belakang fallback jika gambar awan gagal dimuat
                g2.fillRect(0, 0, screenWidth, screenHeight);
            }
        }
        
        map.draw(g2); // Draw the map
        if (gameHour >= 18 || gameHour <= 5) {
            Color nightOverlay = new Color(0, 0, 0, 100);
            g2.setColor(nightOverlay);
            g2.fillRect(0, 0, screenWidth, screenHeight);
        }
        if (currentWeather.equals("Rainy") && !player.getLocation().contains("House") && !player.getLocation().equals("Emily's Store")) {
            g2.setColor(new Color(0, 0, 0, 80)); 
            g2.fillRect(0, 0, screenWidth, screenHeight);

            for (RainDrop drop : rainDrops) {
                drop.draw(g2); 
            }
        }
        g2.setColor(java.awt.Color.white);
        g2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
        String timeString = String.format("Day %d - %02d:%02d", gameDay, gameHour, gameMinute);
        g2.drawString(timeString, 500, 30);
        g2.drawString(player.getLocation(), 500, 70);
        switch (currentSeason) {
            case "Spring":
            g2.setColor(Color.PINK);
            break;
            case "Summer":
                g2.setColor(Color.YELLOW);
                break;
            case "Fall":
                g2.setColor(Color.ORANGE);
                break;
            case "Winter":
                g2.setColor(Color.CYAN);
            default:
            break;
        }
        g2.drawString(currentSeason, 500, 50);

        player.drawPlayer(g2);
        player.drawEnergyBar(g2);
        player.drawGoldWindow(g2);
        for (NPC npc : npcs) {
            if (npc != null) { 
                if (npc.getSpawnMapName() == player.getLocation()) {
                    npc.draw(g2); // Panggil metode draw() pada setiap objek NPC individual
                }
            }
        }

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
        
        if (gameState == inventoryState && (seller == null || (seller != null && !seller.isBuying))) {
            player.openInventory(g2);
        } else if (gameState == inventoryState && seller != null && seller.isBuying) {
            seller.getInventory().drawInventory(g2);
        }
        if (gameState == itemOptionState && (seller == null || (seller != null && !seller.isBuying))) {
            player.getInventory().drawItemOptionWindow(g2);
        } else if (gameState == itemOptionState && seller != null && seller.isBuying) {
            seller.getInventory().drawItemOptionWindow(g2);
        }
        if (gameState == shippingState) {
            player.openShipping(g2);
        } 
        if (gameState == shippingOptionState) {
            player.getInventory().drawShippingOptionWindow(g2);
        }
        if (gameState == viewShippingState) {
            player.currSB.getInventory().drawShipping(g2);
        }
        if (gameState == fishingState) {
            drawFishingWindow(g2);
        }
        if (gameState == cookingState) {
            drawCookingWindow(g2);
        }
        if (gameState == fuelState) {
            drawFuelWindow(g2);
        }
        if (gameState == fishSelectionState) {
            drawFishSelection(g2);
        }
        if (gameState == menuState) {
            player.showMenu(g2);
        }
        if (gameState == inGameHelpState) {
            inGameHelp.draw(g2); 
        }
        if (gameState == playerInfoState) {
            playerInfo.draw(g2); 
        }
        if (gameState == statisticsState) {
            statistics.draw(g2);
        }
        if (gameState == watchingState) {
            activeTV.screen(g2, this);
        }
        if (gameState == fishingWinState) {
            fishingTargetFish.fishingWin(this, g2);
        }
        if (gameState == dialogState) {
            player.dialogNPC(g2);
        }
    }

    public void startSleepingSequence() {
        while(gameHour != 6 || gameMinute != 0) {
            addMinutes(5);
        }
        gameState = playState;

        for (int i = 0; i <= 200; i += 10) {
            try {
                java.awt.Graphics g = this.getGraphics();
                if (g != null) {
                    java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                    g2.setColor(new Color(0, 0, 0, i));
                    g2.fillRect(0, 0, screenWidth, screenHeight);
                    g2.dispose();
                    Thread.sleep(30); 
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        repaint(); 
    }

    public void triggerWeddingDayEvent(Player player, NPC spouse) {
        System.out.println("Wedding event triggered with " + spouse.getName() + "!");

        // (Opsional) gameState = cutsceneState;

        // 1. Logika untuk memindahkan pemain dan pasangan ke rumah
        int playerHouseMapID = 3; // ID Peta Rumah Pemain (sesuaikan jika berbeda)
        int playerHouseSpawnTileX = 7; // Contoh koordinat tile X di dalam rumah
        int playerHouseSpawnTileY = 10; // Contoh koordinat tile Y di dalam rumah

        // Konversi ke koordinat pixel
        int targetPlayerPixelX = playerHouseSpawnTileX * tileSize;
        int targetPlayerPixelY = playerHouseSpawnTileY * tileSize;

        this.map.loadMapByID(playerHouseMapID); // Muat peta rumah

        player.x = targetPlayerPixelX;          // Atur posisi X pemain
        player.y = targetPlayerPixelY;          // Atur posisi Y pemain
        player.setLocation(playerHouseMapID);   // Update nama lokasi pemain
        player.direction = "down";              // Atur arah default pemain
        player.collisionOn = false;

        // (Opsional) Atur posisi NPC pasangan jika perlu
        // spouse.x = ...; spouse.y = ...; spouse.setLocation(playerHouseMapID);

        // 2. Skip waktu ke malam hari
        this.gameHour = 22;
        this.gameMinute = 0;

        // 3. Pulihkan energi pemain
        player.setEnergy(Player.getMaxEnergy());

        // 4. Selesaikan event dan kembali ke playState
        this.gameState = playState;
        player.currentNPC = null; // Bersihkan NPC yang sedang diajak bicara
        player.energyReducedInThisChat = false; // Reset flag energi untuk interaksi berikutnya

        System.out.println("Wedding event concluded. Player is at home. Time: " + gameHour + ":" + gameMinute);
        // Anda mungkin perlu memanggil repaint() jika perubahan tidak langsung terlihat
        // atau jika Anda tidak berada dalam loop game utama saat ini (meskipun seharusnya berada).
    }
}