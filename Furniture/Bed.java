package Furniture;

public class Bed extends Furniture { 

    private String bedType;

    public Bed(String name, boolean isWalkable, String bedType) {
        super(name, isWalkable); 
        this.bedType = bedType;
    }

    public Bed(Bed other) {
        super(other); 
        this.bedType = other.bedType;
        if (this.Image == null && other.Image != null) {
            this.Image = other.Image; 
        } else if (this.Image == null) {
        }
    }

    public String getBedType() {
        return bedType;
    }

    public void Action() {
        System.out.println("Bed is being used");
    }
}