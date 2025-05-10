package player;

import main.GamePanel;
import main.KeyHandler;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;

public class Player { // Sebaiknya: public class Player
    public int x, y;
    private int speed;
    public int screenX;
    public int screenY; // Posisi layar (jika diperlukan untuk offset)
    GamePanel gp;
    KeyHandler keyH;
    String direction; // Akan menyimpan state seperti "up", "down", "idleUp", "idleLeft", dll.
    String lastMoveDirection; // Menyimpan arah gerakan terakhir ("up", "down", "left", "right")

    public BufferedImage[] idleDownFrames, idleUpFrames, idleLeftFrames, idleRightFrames,
                           leftFrames, rightFrames, upFrames, downFrames;

    private int spriteCounter = 0;
    private int spriteNum = 0;
    private final int ANIMATION_SPEED = 10; // Sesuaikan untuk kecepatan animasi yang diinginkan
    private boolean moving = false;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.screenX = gp.screenWidth / 2 - (gp.tileSize / 2); // Posisi tengah layar
        this.screenY = gp.screenHeight / 2 - (gp.tileSize / 2); // Posisi tengah layar
        this.keyH = keyH;
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        this.x = 100; // Posisi awal X
        this.y = 100; // Posisi awal Y
        this.speed = 4;
        this.lastMoveDirection = "down"; // Arah gerakan terakhir default
        this.direction = "idleDown";     // State animasi default (idle menghadap ke bawah)
    }

    public void getPlayerImage() {
        // Pastikan nama folder (parameter pertama) dan jumlah frame (parameter kedua) sesuai
        // dengan file gambar Anda.
        // Contoh: loadAnimationFrames("idledown", 6) akan mencari di folder /player/idledown/
        // file bernama idledown_0.png, idledown_1.png, ..., idledown_5.png

        idleDownFrames = loadAnimationFrames("idledown", 6);
        idleUpFrames = loadAnimationFrames("idleup", 6);
        idleLeftFrames = loadAnimationFrames("idleleft", 6);
        idleRightFrames = loadAnimationFrames("idleright", 6);

        upFrames = loadAnimationFrames("up", 6);
        downFrames = loadAnimationFrames("down", 6);
        leftFrames = loadAnimationFrames("left", 6);
        rightFrames = loadAnimationFrames("right", 6);

        // Blok if di bawah ini baik untuk keamanan, tapi jika loadAnimationFrames
        // selalu mengembalikan array (meskipun berisi placeholder), ini mungkin tidak
        // selalu diperlukan, namun tidak berbahaya.
        if (upFrames == null) upFrames = new BufferedImage[0];
        if (downFrames == null) downFrames = new BufferedImage[0];
        if (leftFrames == null) leftFrames = new BufferedImage[0];
        if (rightFrames == null) rightFrames = new BufferedImage[0];
        if (idleDownFrames == null) idleDownFrames = new BufferedImage[0];
        if (idleUpFrames == null) idleUpFrames = new BufferedImage[0];
        if (idleLeftFrames == null) idleLeftFrames = new BufferedImage[0];
        if (idleRightFrames == null) idleRightFrames = new BufferedImage[0];
    }

    private BufferedImage[] loadAnimationFrames(String animationIdentifier, int frameCount) {
        BufferedImage[] frames = new BufferedImage[frameCount];
        String actualFolderName;
        String fileNamePrefix = animationIdentifier; // Nama file akan selalu menggunakan animationIdentifier sebagai prefix
    
        if (animationIdentifier.startsWith("idle")) {
            // Semua animasi idle ada di dalam folder "idle"
            actualFolderName = "idle";
            // fileNamePrefix sudah benar (misalnya "idleleft", "idledown")
        } else {
            // Animasi bergerak ada di folder yang namanya sama dengan animationIdentifier
            actualFolderName = animationIdentifier; // Misalnya "left", "up"
            // fileNamePrefix sudah benar (misalnya "left", "up")
        }
    
        try {
            for (int i = 0; i < frameCount; i++) {
                // Path gambar yang dibangun: /player/<actualFolderName>/<fileNamePrefix>_i.png
                // Contoh untuk idle: /player/idle/idleleft_0.png
                // Contoh untuk gerak: /player/left/left_0.png
                String imagePath = "/player/" + actualFolderName + "/" + fileNamePrefix + "_" + i + ".png";
                InputStream is = getClass().getResourceAsStream(imagePath);
    
                if (is == null) {
                    System.err.println("Tidak dapat memuat gambar: " + imagePath);
                    frames[i] = createPlaceholderImage();
                } else {
                    frames[i] = ImageIO.read(is);
                    is.close();
                }
            }
        } catch (IOException e) {
            System.err.println("Error saat memuat animasi untuk identifier: " + animationIdentifier);
            e.printStackTrace();
            for (int i = 0; i < frameCount; i++) {
                if (frames[i] == null) frames[i] = createPlaceholderImage();
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
        g.drawString("X", gp.tileSize / 2 - 5, gp.tileSize / 2 + 5);
        g.dispose();
        return placeholder;
    }

    public void update() {
        String prevAnimationState = direction; // Simpan state animasi sebelumnya
        moving = false;

        if (keyH.upPressed) {
            direction = "up";
            lastMoveDirection = "up"; // Simpan arah gerakan aktual terakhir
            y -= speed;
            moving = true;
        } else if (keyH.downPressed) {
            direction = "down";
            lastMoveDirection = "down";
            y += speed;
            moving = true;
        } else if (keyH.leftPressed) {
            direction = "left";
            lastMoveDirection = "left";
            x -= speed;
            moving = true;
        } else if (keyH.rightPressed) {
            direction = "right";
            lastMoveDirection = "right";
            x += speed;
            moving = true;
        }

        // Jika tidak ada tombol gerakan yang ditekan, tentukan state idle berdasarkan lastMoveDirection
        if (!moving) {
            switch (lastMoveDirection) {
                case "up":
                    direction = "idleUp";
                    break;
                case "down":
                    direction = "idleDown";
                    break;
                case "left":
                    direction = "idleLeft";
                    break;
                case "right":
                    direction = "idleRight";
                    break;
                default: // Fallback jika lastMoveDirection tidak terdefinisi (seharusnya tidak terjadi)
                    direction = "idleDown";
                    break;
            }
        }

        // Jika state animasi berubah (misalnya dari "left" ke "idleLeft", atau "idleLeft" ke "up")
        // reset nomor sprite dan counter.
        if (!prevAnimationState.equals(direction)) {
            spriteNum = 0;
            spriteCounter = 0;
        }

        // Logika untuk mengganti frame animasi (berlaku untuk semua state animasi)
        spriteCounter++;
        if (spriteCounter > ANIMATION_SPEED) {
            spriteNum++;
            BufferedImage[] currentFrames = getCurrentAnimationFrames();
            if (currentFrames != null && currentFrames.length > 0) {
                if (spriteNum >= currentFrames.length) {
                    spriteNum = 0; // Kembali ke frame pertama
                }
            } else {
                spriteNum = 0; // Fallback jika array frame tidak valid/kosong
            }
            spriteCounter = 0;
        }
    }

    private BufferedImage[] getCurrentAnimationFrames() {
        switch (direction) {
            // Animasi Bergerak
            case "up":
                return (upFrames != null && upFrames.length > 0) ? upFrames : null;
            case "down":
                return (downFrames != null && downFrames.length > 0) ? downFrames : null;
            case "left":
                return (leftFrames != null && leftFrames.length > 0) ? leftFrames : null;
            case "right":
                return (rightFrames != null && rightFrames.length > 0) ? rightFrames : null;
            // Animasi Idle Sesuai Arah
            case "idleUp":
                return (idleUpFrames != null && idleUpFrames.length > 0) ? idleUpFrames : null;
            case "idleDown":
                return (idleDownFrames != null && idleDownFrames.length > 0) ? idleDownFrames : null;
            case "idleLeft":
                return (idleLeftFrames != null && idleLeftFrames.length > 0) ? idleLeftFrames : null;
            case "idleRight":
                return (idleRightFrames != null && idleRightFrames.length > 0) ? idleRightFrames : null;
            default:
                // Fallback jika 'direction' memiliki nilai yang tidak terduga.
                // Anda bisa default ke idleDownFrames atau salah satu animasi gerakan.
                if (idleDownFrames != null && idleDownFrames.length > 0) return idleDownFrames;
                return null; // Akan menampilkan placeholder jika idleDownFrames juga tidak ada/kosong
        }
    }

    public void drawPlayer(Graphics2D g2) {
        BufferedImage image = null;
        BufferedImage[] currentFrames = getCurrentAnimationFrames();

        if (currentFrames != null && currentFrames.length > 0) {
            if (spriteNum >= currentFrames.length) { // Keamanan tambahan
                spriteNum = 0;
            }
            image = currentFrames[spriteNum];
        }

        if (image == null) {
            // System.err.println("Fallback: Menggambar placeholder untuk direction: " + direction + ", spriteNum: " + spriteNum);
            g2.setColor(Color.RED); // Warna fallback jika gambar tidak ada
            g2.fillRect(x, y, gp.tileSize, gp.tileSize);
            return;
        }
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }

    public int getX() { return x; }
    public int getY() { return y; }
}