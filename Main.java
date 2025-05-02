public class Main {
    public static void main(String[] args) {

        // Create a Tile object
        Tile tile = new Tile("Soil", 'S', true); // Example tile with name "Grass", symbol 'G', and walkable true
        Tile water = new Tile("Water", 'W', false); // Example tile with name "Water", symbol 'W', and walkable false
        Tile rock = new Tile("Rock", 'R', false); // Example tile with name "Rock", symbol 'R', and walkable false
        Tile building = new Tile("Building", 'B', false); // Example tile with name "Building", symbol 'B', and walkable false
        
        // Create a Point object
        int rows = 20; 
        int cols = 20; 

        Map map = new Map(rows, cols); 
        map.setEarlyMap(); 

        map.changePoint(3, 4, water);
        map.changePoint(2, 2, rock); // Change the tile at (2, 2) to tile2
        map.placeBuilding(2, 4, 3, 3, building); // Place a building at (2, 4) with width 3 and height 3



        // Example: Print the map
        map.printMap(); // Print the initial empty map

    }
}
