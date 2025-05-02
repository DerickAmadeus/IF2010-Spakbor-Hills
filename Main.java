public class Main {
    public static void main(String[] args) {

        // Create a Tile object
        Tile tile = new Tile("Soil", 'S', true); // Example tile with name "Grass", symbol 'G', and walkable true
        Tile water = new Tile("Water", 'W', false); // Example tile with name "Water", symbol 'W', and walkable false
        Tile rock = new Tile("Rock", 'R', false); // Example tile with name "Rock", symbol 'R', and walkable false
        Tile building = new Tile("Building", 'B', false); // Example tile with name "Building", symbol 'B', and walkable false
        
        // Create a Point object
        int rows = 20; 
        int cols = 40; 

        Map map = new Map(rows, cols); // Create a map with 20 rows and 20 columns
        map.loadMap("map.txt", rows, cols);


        // Example: Print the map
        map.printMap(); // Print the initial empty map
        map.displayMapGUI();

    }
}
