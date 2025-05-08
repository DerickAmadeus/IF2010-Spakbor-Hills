package player;

import main.GamePanel;
import main.KeyHandler;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;

// Sebaiknya nama kelas diawali huruf kapital: Player
public class player {
    // ... (atribut lainnya tetap sama) ...
    private int x, y;
    private int speed;
    GamePanel gp;
    KeyHandler keyH;
    String direction;

    public BufferedImage[] idleFrames, leftFrames, rightFrames, upFrames, downFrames;

    private int spriteCounter = 0;
    private int spriteNum = 0;
    private final int ANIMATION_SPEED = 10;
    private boolean moving = false;

    public player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        this.x = 100;
        this.y = 100;
        this.speed = 4;
        this.direction = "down"; // Atau "idle" jika Anda punya animasi idle
    }

    public void getPlayerImage() {
        // Asumsi Anda memiliki 2 frame per animasi dan file bernama <arah>_0.png, <arah>_1.png
        // Sesuaikan frameCount jika jumlahnya berbeda
        // Path sekarang relatif terhadap folder 'player' di classpath
        idleFrames = loadAnimationFrames("idle", 2);   // Misal: player/idle/idle_0.png
        upFrames = loadAnimationFrames("left", 7);     // Misal: player/up/up_0.png (jika ada)
        downFrames = loadAnimationFrames("right", 7);   // Misal: player/down/down_0.png (jika ada)
        leftFrames = loadAnimationFrames("left",7);   // Misal: player/left/left_0.png
        rightFrames = loadAnimationFrames("right", 7); // Misal: player/right/right_0.png

        // Jika Anda belum punya animasi up/down/idle, Anda bisa mengomentarinya
        // atau buat array kosong agar tidak error, misal:
        if (upFrames == null) upFrames = new BufferedImage[0];
        if (downFrames == null) downFrames = new BufferedImage[0];
        if (idleFrames == null) idleFrames = new BufferedImage[0];
    }

    private BufferedImage[] loadAnimationFrames(String animationDirection, int frameCount) {
        BufferedImage[] frames = new BufferedImage[frameCount];
        try {
            for (int i = 0; i < frameCount; i++) {
                // Path di dalam classpath: /player/<animationDirection>/<animationDirection>_i.png
                // Contoh: /player/left/left_0.png
                String imagePath = "/player/" + animationDirection + "/" + animationDirection + "_" + i + ".png";
                InputStream is = getClass().getResourceAsStream(imagePath);

                if (is == null) {
                    System.err.println("Tidak dapat memuat gambar: " + imagePath);
                    // Anda bisa melempar exception atau memuat gambar placeholder
                    frames[i] = createPlaceholderImage();
                } else {
                    frames[i] = ImageIO.read(is);
                    is.close(); // Tutup InputStream setelah selesai membaca
                }
            }
        } catch (IOException e) {
            System.err.println("Error saat memuat animasi untuk: " + animationDirection);
            e.printStackTrace();
            for(int i=0; i < frameCount; i++) {
                if(frames[i] == null) frames[i] = createPlaceholderImage();
            }
        }
        return frames;
    }

    private BufferedImage createPlaceholderImage() {
        BufferedImage placeholder = new BufferedImage(gp.tileSize, gp.tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = placeholder.createGraphics();
        g.setColor(Color.MAGENTA);
        g.fillRect(0, 0, gp.tileSize, gp.tileSize);
        g.setColor(Color.BLACK);
        g.drawString("X", gp.tileSize/2 - 5, gp.tileSize/2 + 5);
        g.dispose();
        return placeholder;
    }

    public void update() {
        moving = false;
        String prevDirection = direction; // Simpan arah sebelumnya

        if (keyH.upPressed) {
            direction = "up";
            y -= speed;
            moving = true;
        } else if (keyH.downPressed) {
            direction = "down";
            y += speed;
            moving = true;
        } else if (keyH.leftPressed) {
            direction = "left";
            x -= speed;
            moving = true;
        } else if (keyH.rightPressed) {
            direction = "right";
            x += speed;
            moving = true;
        }

        // Jika arah berubah, reset animasi ke frame pertama
        if (!prevDirection.equals(direction) && moving) {
            spriteNum = 0;
            spriteCounter = 0;
        }

        if (moving) {
            spriteCounter++;
            if (spriteCounter > ANIMATION_SPEED) {
                spriteNum++;
                BufferedImage[] currentFrames = getCurrentAnimationFrames();
                if (currentFrames != null && currentFrames.length > 0) {
                    if (spriteNum >= currentFrames.length) {
                        spriteNum = 0;
                    }
                } else {
                    spriteNum = 0; // Fallback jika array frame tidak valid
                }
                spriteCounter = 0;
            }
        } else {
            // Jika tidak bergerak dan ada animasi idle, gunakan itu.
            // Jika tidak, tetap di frame pertama arah terakhir atau frame pertama idle.
            if (idleFrames != null && idleFrames.length > 0) {
                direction = "idle"; // Set arah ke idle jika tidak bergerak
                // Biarkan spriteNum direset jika arah berubah ke idle,
                // atau kelola animasi idle secara terpisah jika perlu.
                 spriteCounter++;
                 if (spriteCounter > ANIMATION_SPEED) {
                    spriteNum++;
                    if (spriteNum >= idleFrames.length) {
                        spriteNum = 0;
                    }
                    spriteCounter = 0;
                 }

            } else {
                 spriteNum = 0; // Kembali ke frame pertama dari arah terakhir jika tidak ada idle
            }
        }
    }


    private BufferedImage[] getCurrentAnimationFrames() {
        // Pastikan untuk menangani kasus di mana array mungkin null atau kosong
        switch (direction) {
            case "up":
                return (upFrames != null && upFrames.length > 0) ? upFrames : null;
            case "down":
                return (downFrames != null && downFrames.length > 0) ? downFrames : null;
            case "left":
                return (leftFrames != null && leftFrames.length > 0) ? leftFrames : null;
            case "right":
                return (rightFrames != null && rightFrames.length > 0) ? rightFrames : null;
            case "idle":
                return (idleFrames != null && idleFrames.length > 0) ? idleFrames : null;
            default:
                // Fallback ke idle jika ada, atau down, atau null jika tidak ada yang valid
                if (idleFrames != null && idleFrames.length > 0) return idleFrames;
                if (downFrames != null && downFrames.length > 0) return downFrames;
                return null;
        }
    }

    public void drawPlayer(Graphics2D g2) {
        BufferedImage image = null;
        BufferedImage[] currentFrames = getCurrentAnimationFrames();

        if (currentFrames != null && currentFrames.length > 0) {
            // Pastikan spriteNum berada dalam batas yang valid untuk frame saat ini
            if (spriteNum >= currentFrames.length) {
                spriteNum = 0; // Reset jika di luar batas (seharusnya sudah ditangani di update)
            }
            image = currentFrames[spriteNum];
        }

        if (image == null) {
            // Fallback jika tidak ada gambar yang bisa ditampilkan
            // System.err.println("Tidak ada gambar untuk digambar. Arah: " + direction + ", spriteNum: " + spriteNum);
            g2.setColor(Color.RED); // Gambar kotak merah sebagai indikasi error
            g2.fillRect(x, y, gp.tileSize, gp.tileSize);
            return;
        }

        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }

    // ... (getX, getY, dll. tetap sama) ...
    public int getX() { return x; }
    public int getY() { return y; }
}