package Furniture;

abstract class Furniture{
    private String name;
    private int width;
    private int height;
    private int symbol;
    public Furniture(String name, int width, int height, int symbol){
        this.furniName = furniName;
        this.width = width;
        this.height = height;
        this.symbol = symbol;
    }
    public String getName(){
        return name;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public int getSymbol(){
        return symbol;
    }
    abstract void Action();
}