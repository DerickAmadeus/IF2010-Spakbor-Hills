package WorldMap;
import NPC.NPC;
import Playerstuffs.Player;
import java.util.Scanner;

public class NPCHouse extends WorldBuilding {
    private NPC owner;

    public NPCHouse(String buildingName, NPC owner) {
        super(buildingName);
        this.owner = owner;
    }

    public NPC getOwner() {
        return owner;
    }

    @Override
    public void interact() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to " + getBuildingName() + "! I am " + owner.getName() + ".");
        // Implementasi interaksi dengan NPC
        while (true) {
            System.out.println("1. Talk to " + owner.getName());
            System.out.println("2. Leave " + getBuildingName());
            System.out.println("3. Gifting");
            System.out.println("4. Marry " + owner.getName());
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            if (choice == 1) {
                // owner.talk();
            } else if (choice == 2) {
                System.out.println("Goodbye!");
                break;
            } else if (choice == 3) {
                // System.out.println("Gifting is not implemented yet.");
            } else if (choice == 4) {
                // System.out.println("Marrying is not implemented yet.");
            }
            else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
    
}
