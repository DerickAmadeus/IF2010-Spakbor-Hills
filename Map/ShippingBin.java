package Map;


public class ShippingBin extends Tile {
    
    public int maxSlot = 16; 
    public int binCount; 
    public int moneyStored; 
    public int lastday;
    

    public ShippingBin(String name, boolean isWalkable) {
        super(name, isWalkable);
        this.binCount = 0; 
        this.moneyStored = 0; 
        this.lastday = 0;
    }

    public ShippingBin(ShippingBin other) {
        super(other);
        this.binCount = other.binCount;
        this.moneyStored = other.moneyStored;
        this.lastday = other.lastday;
    }
}
