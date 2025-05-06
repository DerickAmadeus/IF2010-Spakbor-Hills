package Items;

public class Misc extends Item {
    
    public Misc(String name, String description, int hargaJual, int hargaBeli) {
        super(name, description, hargaJual, hargaBeli);
    }

    public void vanishItem(){
        System.out.println(getName() + " has vanished from existence.");
    }
}
