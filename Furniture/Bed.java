package Furniture;

public class Bed extends Furniture{
    private String bedType;
    public Bed(String name, boolean isWalkable, String bedType) {
        super(name, isWalkable);
        this.bedType = bedType;
    }
    public String getBedType(){
        return bedType;
        
    }
    @Override
    public void Action() {
        System.out.println("Bed is being slept on");
    }
}