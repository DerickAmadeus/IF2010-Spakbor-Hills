package Furniture;

import Map.Tile; // Pastikan import Tile jika Bed adalah subclassnya
// Import lain yang mungkin diperlukan oleh Bed

public class Bed extends Tile { // Atau kelas parent lainnya jika bukan Tile

    // Atribut-atribut spesifik Bed Anda (misalnya, bedType, dll.)
    private String bedType;
    // ... atribut lain ...

    // Konstruktor utama Anda (mungkin sudah ada)
    public Bed(String name, boolean isWalkable, String bedType) {
        super(name, isWalkable); // Panggil konstruktor superclass
        this.bedType = bedType;
        // Inisialisasi atribut lain
        // Mungkin juga load gambar di sini jika belum dari superclass
    }

    // === TAMBAHKAN COPY CONSTRUCTOR INI ===
    public Bed(Bed other) {
        super(other); // Panggil copy constructor superclass (Tile)
                      // Ini akan meng-copy 'name', 'walkable', 'Image', 'gp' jika ada di Tile's copy constructor

        // Copy atribut-atribut spesifik dari kelas Bed
        this.bedType = other.bedType;
        // Copy atribut lain yang dimiliki Bed
        // Contoh:
        // this.someOtherBedSpecificProperty = other.someOtherBedSpecificProperty;

        // Jika gambar tidak di-handle oleh super(other) dan perlu di-set ulang
        // atau jika Bed memiliki gambar sendiri yang berbeda dari Tile standar
        if (this.Image == null && other.Image != null) {
            this.Image = other.Image; // Atau muat ulang berdasarkan tipe/path jika perlu
        } else if (this.Image == null) {
            // Logika fallback jika gambar masih null
            // loadImageBasedOnType(this.bedType); // Contoh method
        }
    }

    // ... method lain di kelas Bed ...

    public String getBedType() {
        return bedType;
    }
}