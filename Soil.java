public class Soil extends Tile{
    private boolean wetStatus;
    private int wetCountdown;
    private Seeds seedPlanted;
    private int ferrtilizerCountdown;


    public Soil(String tileName, char tileSymbol, boolean isWalkable) {
        super(tileName, tileSymbol, isWalkable);
        this.wetStatus = false;
        this.wetCountdown = 3;
        this.seedPlanted = null;
        this.ferrtilizerCountdown = 5;
    }



    public boolean isWet() {
        return wetStatus;
    }


    public void tiling() {
        // Logic for tilling the soil
        System.out.println("Tilling the soil...");
        setTileSymbol('T'); // Example symbol for tilled soil
        setTileName("Tilled Soil"); // Example name for tilled soil
    }


    public void recoverLand() {
        // Logic for recovering the land
        if (getTileSymbol() == 'D') {
            System.out.println("Recovering the land...");
            seedPlanted = null; // Remove the planted seed
            wetStatus = false; // Reset wet status
            wetCountdown = 3; // Reset wet countdown
            ferrtilizerCountdown = 5; // Reset fertilizer countdown
            setTileSymbol('T'); // Example symbol for recovered land
            setTileName("Recovered Land"); // Example name for recovered land
        }
    }


    public void planting(Seeds seed) {
        // Logic for planting seeds
        System.out.println("Planting seeds...");
        seedPlanted = seed;
        setTileSymbol('P'); // Example symbol for planted soil
        setTileName("Planted Soil"); // Example name for planted soil
    }


    public void watering() {
        // Logic for watering the soil
        System.out.println("Watering the soil...");
        wetStatus = true;
        wetCountdown = 3; // Example countdown for wet status
        setTileSymbol('W'); // Example symbol for watered soil
        setTileName("Watered Soil"); // Example name for watered soil
    }


    public void fertilizing() {
        // Logic for fertilizing the soil
        System.out.println("Fertilizing the soil...");
        ferrtilizerCountdown = 5; // Example countdown for fertilizer status
        setTileSymbol('F'); // Example symbol for fertilized soil
        setTileName("Fertilized Soil"); // Example name for fertilized soil
    }


    public void harvest() {
        // Logic for harvesting the soil
        System.out.println("Harvesting the soil...");
        setTileSymbol('H'); // Example symbol for harvested soil
        setTileName("Harvested Soil"); // Example name for harvested soil

        // pasang add to inventory
        // Logic to add harvested corps to inventory
    }

    
    public void plantDead() {
        // Logic for dead plant
        if (seedPlanted != null && wetCountdown <= 0 && ferrtilizerCountdown <= 0) { 
            System.out.println("The plant has died.");
            seedPlanted = null; // Remove the planted seed
            setTileSymbol('D'); // Example symbol for dead plant
            setTileName("Dead Plant"); // Example name for dead plant
        } 
        wetCountdown--;
        ferrtilizerCountdown--;
    }
}

