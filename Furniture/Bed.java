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

    public Bed(Bed other) {
        super(other.getTileName(), other.isWalkable()); // Memanggil konstruktor Furniture (atau Tile jika Furniture tidak punya copy constructor sendiri)
                                                        // Menggunakan getter dari superclass Tile jika name dan isWalkable di Furniture menyembunyikan milik Tile
                                                        // atau langsung super(other) jika Furniture punya copy constructor Furniture(Furniture other)
        this.bedType = other.bedType;
        // Pastikan juga untuk menyalin gambar jika tidak ditangani oleh super(other)
        if (other.Image != null) {
            this.Image = other.Image; // Menyalin referensi gambar. Jika perlu deep copy gambar, logikanya lebih rumit.
        }
    }
    @Override
    public void Action() {
        System.out.println("Bed is being slept on");
    }
}