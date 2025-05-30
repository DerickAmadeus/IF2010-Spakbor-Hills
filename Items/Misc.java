package Items;

public class Misc extends Item {
    
    public Misc(String name, String description, int hargaJual, int hargaBeli) {
        super(name, description, hargaJual, hargaBeli);
    }

    public void vanishItem(){
        System.out.println(getName() + " has vanished from existence.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Misc misc = (Misc) o;

        return this.getName().equals(misc.getName()); // Atau sesuaikan dengan ID unik yang kamu punya
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode(); // Atau kombinasi field yang unik
    }
}
