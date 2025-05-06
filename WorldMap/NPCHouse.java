package WorldMap;
import NPC.NPC;

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
        System.out.println("Welcome to " + getBuildingName() + "! I am " + owner.getName() + ".");
        // Implementasi interaksi dengan NPC
        // owner.talk();
    }
    
}
