package Furniture;

public class Bed extends Furniture{
    private String bedType;
    public Bed(String name, int width, int height, char symbol, String bedType){
        super(name, width, height, symbol);
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