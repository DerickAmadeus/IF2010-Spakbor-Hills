package Items;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Item {
    private String name;
    private String description;
    private int hargaJual;
    private int hargaBeli;
    private BufferedImage icon;

    public Item(String name, String description, int hargaJual, int hargaBeli) {
        this.name = name;
        this.description = description;
        setHargaJual(hargaJual);
        setHargaBeli(hargaBeli);

        // Tentukan nama folder berdasarkan class turunan
        String className = getClass().getSimpleName().toLowerCase(); // contoh: "Food" → "food"
        String imagePath = String.format("image/%s/%s.png", className, name);

        try {
            BufferedImage icon = ImageIO.read(getClass().getResourceAsStream(imagePath));
            setIcon(icon);
        } catch (IOException | NullPointerException e) {
            System.err.println("Gagal memuat ikon: " + imagePath);
            e.printStackTrace();
        }
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

    public BufferedImage getIcon() {
        return icon;
    }

    public void setIcon(BufferedImage icon) {
        this.icon = icon;
    }

    public void setHargaJual(int harga) {
        this.hargaJual = Math.max(0, harga);
    }

    public void setHargaBeli(int harga) {
        this.hargaBeli = Math.max(0, harga);
    }
}
