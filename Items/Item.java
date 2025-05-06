package Items;

public abstract class Item {
    private String name;
    private String description;
    private int hargaJual;
    private int hargaBeli;

    public Item(String name, String description, int hargaJual, int hargaBeli) {
        this.name = name;
        this.description = description;
        setHargaJual(hargaJual);
        setHargaBeli(hargaBeli);
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return description;
    }

    public int getHargaJual() {
        return hargaJual;
    }

    public int getHargaBeli() {
        return hargaBeli;
    }

    public void setHargaJual(int harga) {
        if (harga < 0) {
            hargaJual = 0;
        } else {
            hargaJual = harga;
        }
    }

    public void setHargaBeli(int harga) {
        if (harga < 0) {
            hargaBeli = 0;
        } else {
            hargaBeli = harga;
        }
    }
}
