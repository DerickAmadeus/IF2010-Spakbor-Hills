import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Map {
    private int width;
    private int height;
    private List<Point> points;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        this.points = new ArrayList<>();
    }

    public void addPoint(int x, int y, Tile tile) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            Point point = new Point(x, y, tile);
            points.add(point);
        } else {
            System.out.println("Point out of bounds: (" + x + ", " + y + ")");
        }
    }

    public void changePoint(int x, int y, Tile tile) {
        for (Point point : points) {
            if (point.getX() == x && point.getY() == y) {
                point.setTileInfo(tile.getTileSymbol());
                return;
            }
        }
        System.out.println("Point not found: (" + x + ", " + y + ")");
    }

    public void placeBuilding(int startX, int startY, int width, int height, Tile tile) {
        boolean outOfBounds = false;

        // Check if the building fits within the map boundaries
        if (startX < 0 || startY < 0 || startX + width > this.width || startY + height > this.height) {
            System.out.println("Building out of bounds.");
            return;
        }

        // Place the building tiles
        for (int y = startY; y < startY + height; y++) {
            for (int x = startX; x < startX + width; x++) {
                boolean pointFound = false;
                for (Point point : points) {
                    if (point.getX() == x && point.getY() == y) {
                        point.setTileInfo(tile.getTileSymbol());
                        pointFound = true;
                        break;
                    }
                }
                if (!pointFound) {
                    outOfBounds = true;
                }
            }
        }

        if (outOfBounds) {
            System.out.println("Some points were out of bounds.");
        }
    }

    public List<Point> getPoints() {
        return points;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setEarlyMap() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                addPoint(x, y, new Tile("Soil", 'S', true));
            }
        }
    }


    public void loadMap(String filePath, int width, int height) {
        this.width = width;
        this.height = height;
        this.points = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int y = 0;

            while ((line = reader.readLine()) != null && y < height) {
                String[] tiles = line.trim().split(" ");
                for (int x = 0; x < Math.min(tiles.length, width); x++) {
                    char symbol = tiles[x].charAt(0);
                    Tile tile;
                    switch (symbol) {
                        case 'R':
                            tile = new Tile("Rock", 'R', false);
                            break;
                        case 'B':
                            tile = new Tile("Building", 'B', false);
                            break;
                        case 'S':
                        default:
                            tile = new Tile("Soil", 'S', true);
                            break;
                    }
                    addPoint(x, y, tile);
                }
                y++;
            }
        } catch (IOException e) {
            System.out.println("Error reading map file: " + e.getMessage());
        }
    }





    public void printMap() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean found = false;
                for (Point point : points) {
                    if (point.getX() == x && point.getY() == y) {
                        System.out.print(point.getTile() + " ");
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.print(". "); // Empty space
                }
            }
            System.out.println();
        }
    }



    // Masi ngebug hehehe

    public void displayMapGUI() {
        javax.swing.JFrame frame = new javax.swing.JFrame("Map Display");
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setLayout(new java.awt.GridLayout(height, width));

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                javax.swing.JLabel label = new javax.swing.JLabel();
                label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                label.setVerticalAlignment(javax.swing.SwingConstants.CENTER);

                boolean found = false;
                for (Point point : points) {
                    if (point.getX() == x && point.getY() == y) {
                        if (point.getTile() == 'S') {
                            try {
                                javax.swing.ImageIcon icon = new javax.swing.ImageIcon("grass.png");
                                java.awt.Image scaledImage = icon.getImage().getScaledInstance(800 / width, 800 / height, java.awt.Image.SCALE_SMOOTH);
                                label.setIcon(new javax.swing.ImageIcon(scaledImage));
                            } catch (Exception e) {
                                System.out.println("Error loading grass.png: " + e.getMessage());
                                label.setText("G");
                            }
                        } else {
                            label.setText(String.valueOf(point.getTile()));
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    label.setText(".");
                }

                panel.add(label);
            }
        }

        frame.add(panel);
        frame.setVisible(true);
    }
    
}
