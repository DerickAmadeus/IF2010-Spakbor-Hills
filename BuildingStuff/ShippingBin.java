package BuildingStuff;
import Items.Item;

public class ShippingBin extends Building{
    private int maxSlot;
    private Item[] bin;
    private int binCount;
    public ShippingBin(String tilename, char tileSymbol, boolean isWalkable, String buildingName, int width, int height, char symbol, int longitude, int latitude) {
        super(tilename, tileSymbol, isWalkable, buildingName, width, height, symbol, longitude, latitude);
        this.maxSlot = 16;
        this.bin = new Item[maxSlot];
        this.binCount = 0;
    }
    public void placeItem(Item item){
        if(binCount < maxSlot){
            bin[binCount] = item;
            binCount++;
            System.out.println("Item " + item.getName() + " placed in the shipping bin.");
        } 
        else{
            System.out.println("Shipping bin is full. Cannot place item " + item.getName() + ".");
        }
    }
    public void selling(Item item){
        System.out.println("Item " + item + "has been sold. Yeay!");
    }
}