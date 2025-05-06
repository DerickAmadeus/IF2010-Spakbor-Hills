package Furniture;

abstract class Furniture{
    private String name;
    private int width;
    private int height;
    private char symbol;
    public Furniture(String name, int width, int height, char symbol){
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
    public char getSymbol(){
        return symbol;
    }
    abstract void Action();
}