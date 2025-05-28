// Di dalam kelas NPC.java
package NPC;

import main.GamePanel;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Font; // Tambahkan untuk dialog
import java.awt.FontMetrics; // Tambahkan untuk dialog
import java.awt.BasicStroke; // Tambahkan untuk dialog

public class NPC {
    public int worldX, worldY;
    public Rectangle hitbox;
    public Rectangle interactionTriggerArea; // Area di sekitar NPC untuk memicu interaksi

    GamePanel gp;
    public String name;
    public String spawnMapName; // Menggunakan int untuk ID map

    // Dialog
    public String[] dialogues;
    private int currentDialogueIndex = 0;

    // Variabel untuk animasi (sudah ada)
    private BufferedImage[] idleFrames;
    private int spriteCounter = 0;
    private int spriteNum = 0;
    private final int ANIMATION_SPEED = 15;
    private final int IDLE_FRAME_COUNT = 6; // Anda set 6, sebelumnya saya contohkan 8

    public NPC(GamePanel gp, String name, String spawnMapName, int tileX, int tileY) {
        this.gp = gp;
        this.name = name;
        this.spawnMapName = spawnMapName;
        this.worldX = tileX * gp.tileSize;
        this.worldY = tileY * gp.tileSize;
        this.hitbox = new Rectangle(0, 0, gp.tileSize, gp.tileSize); // Hitbox relatif terhadap worldX, worldY

        // Tentukan area interaksi NPC dalam koordinat dunia.
        // Contoh: Sama dengan hitboxnya, atau sedikit lebih besar.
        // Di sini kita buat sama dengan hitbox NPC.
        this.interactionTriggerArea = new Rectangle(worldX, worldY, gp.tileSize, gp.tileSize);
        // Jika ingin lebih besar, contoh:
        // this.interactionTriggerArea = new Rectangle(worldX - gp.tileSize / 2, worldY - gp.tileSize / 2, gp.tileSize * 2, gp.tileSize * 2);

        loadIdleAnimation();
        setDefaultDialogues(); // Inisialisasi dialog default untuk NPC
    }

    private void setDefaultDialogues() {
        // Contoh dialog, bisa Anda kembangkan per NPC
        if (this.name.equalsIgnoreCase("Villager")) {
            dialogues = new String[]{
                "Halo, petualang muda!",
                "Desa kami damai berkat para pahlawan sepertimu.",
                "Sudahkah kamu mencoba memancing di danau?"
            };
        } else if (this.name.equalsIgnoreCase("Merchant")) {
            dialogues = new String[]{
                "Barang baru datang! Mau lihat?",
                "Aku punya penawaran spesial untukmu hari ini.",
                "Jangan ragu untuk bertanya jika ada yang menarik."
            };
        } else if (this.name.equalsIgnoreCase("Fisherman")) {
            dialogues = new String[]{
                "Ikan hari ini sedang bagus-bagusnya!",
                "Kesabaran adalah kunci memancing, nak.",
                "Ada monster di danau itu... atau hanya perasaanku saja ya?"
            };
        } else {
            dialogues = new String[]{"Hmm..."};
        }
    }

    // Metode yang dipanggil ketika pemain berinteraksi dengan NPC ini

    public void interact() {
        if (dialogues != null && dialogues.length > 0) {
            // Tampilkan dialog NPC
            System.out.println(name + ": " + dialogues[currentDialogueIndex]);
            currentDialogueIndex++;
            if (currentDialogueIndex >= dialogues.length) {
                currentDialogueIndex = 0; // Reset ke awal dialog
            }
        } else {
            System.out.println(name + " tidak memiliki dialog.");
        }
    }


    private void loadIdleAnimation() {
        idleFrames = new BufferedImage[IDLE_FRAME_COUNT];
        // Anda menyebutkan IDLE_FRAME_COUNT = 6, jadi pastikan ada 6 frame (0-5)
        // Path diubah menjadi /npc/<NamaNPC>/idle_<nomorFrame>.png
        // jika Anda memiliki folder terpisah untuk setiap NPC
        System.out.println("Loading animation for NPC: " + name);
        for (int i = 0; i < IDLE_FRAME_COUNT; i++) {
            String imagePath = "/NPC/" + name + "/idle_" + i + ".png"; // Path dengan subfolder per NPC
            // Jika tidak ada subfolder, gunakan: String imagePath = "/NPC/" + name + "_idle_" + i + ".png";
            try {
                InputStream is = getClass().getResourceAsStream(imagePath);
                if (is == null) {
                    System.err.println("Cannot load NPC image: " + imagePath + " (InputStream is null)");
                    idleFrames[i] = createPlaceholderImage();
                } else {
                    idleFrames[i] = ImageIO.read(is);
                    System.out.println("Loaded: " + imagePath);
                    is.close();
                }
            } catch (IOException e) {
                System.err.println("Error loading NPC image: " + imagePath);
                e.printStackTrace();
                idleFrames[i] = createPlaceholderImage();
            }
        }
    }

    private BufferedImage createPlaceholderImage() {
        // ... (metode createPlaceholderImage sudah ada) ...
        BufferedImage placeholder = new BufferedImage(gp.tileSize, gp.tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = placeholder.createGraphics();
        g.setColor(Color.MAGENTA);
        g.fillRect(0, 0, gp.tileSize, gp.tileSize);
        g.setColor(Color.BLACK);
        g.drawString("NPC?", gp.tileSize / 4, gp.tileSize / 2 + 5);
        g.dispose();
        return placeholder;
    }

    public void update() {
        // ... (logika animasi idle sudah ada) ...
        spriteCounter++;
        if (spriteCounter > ANIMATION_SPEED) {
            spriteNum++;
            if (spriteNum >= IDLE_FRAME_COUNT) {
                spriteNum = 0;
            }
            spriteCounter = 0;
        }
    }

    // Ganti nama drawNPC menjadi draw agar konsisten
    public void draw(Graphics2D g2) {
        // ... (logika draw NPC yang sudah ada, pastikan menggunakan gp.player.x dan gp.player.screenX, dst.) ...
        BufferedImage imageToDraw = null;
        if (idleFrames != null && idleFrames.length > 0 && spriteNum < idleFrames.length && idleFrames[spriteNum] != null) {
            imageToDraw = idleFrames[spriteNum];
        } else if (idleFrames != null && idleFrames.length > 0 && idleFrames[0] != null) {
            imageToDraw = idleFrames[0];
        } else {
            imageToDraw = createPlaceholderImage();
        }

        int screenX = worldX - gp.player.x + gp.player.screenX;
        int screenY = worldY - gp.player.y + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.x - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.x + gp.player.screenX + gp.screenWidth &&
            worldY + gp.tileSize > gp.player.y - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.y + gp.player.screenY + gp.screenHeight) {
            g2.drawImage(imageToDraw, screenX, screenY, gp.tileSize, gp.tileSize, null);
            if (gp.debugMode) {
                g2.setColor(new Color(255, 0, 255, 100));
                g2.drawRect(screenX + hitbox.x, screenY + hitbox.y, hitbox.width, hitbox.height); // Gambar hitbox NPC
                // Gambar juga interactionTriggerArea NPC untuk debug
                Rectangle actualTriggerArea = getInteractionTriggerAreaWorld(); // Dapatkan area trigger dalam koordinat dunia
                int triggerScreenX = actualTriggerArea.x - gp.player.x + gp.player.screenX;
                int triggerScreenY = actualTriggerArea.y - gp.player.y + gp.player.screenY;
                g2.setColor(new Color(0, 255, 255, 80)); // Cyan transparan
                g2.drawRect(triggerScreenX, triggerScreenY, actualTriggerArea.width, actualTriggerArea.height);

                g2.setColor(Color.WHITE);
                g2.drawString(name, screenX, screenY - 5);
            }
        }
    }

    // Metode interactionBox yang Anda buat sebelumnya bisa dihapus atau tidak digunakan jika sudah ada draw debug
    // public void interactionBox(Graphics2D g2) { ... }

    public String getSpawnMapName() { // Pastikan getter ini ada
        return spawnMapName;
    }

    // Getter untuk area interaksi NPC (dalam koordinat dunia)
    public Rectangle getInteractionTriggerAreaWorld() {
        // Jika NPC bisa bergerak, pastikan rectangle ini diupdate posisinya.
        // Untuk NPC statis, set di constructor sudah cukup.
        // Jika interactionTriggerArea adalah field yang diinisialisasi di constructor dengan worldX, worldY:
        // return this.interactionTriggerArea;
        // Atau jika selalu berdasarkan hitbox:
        return new Rectangle(worldX + hitbox.x, worldY + hitbox.y, hitbox.width, hitbox.height);
    }

    public void showStatus() {
        // Gambar kotak dialog di atas kepala NPC (seperti textbox)
        // Asumsikan dipanggil dari dalam draw(Graphics2D g2) setelah gambar NPC
        // Untuk demo, kita gambar dialog saat ini (atau dialog pertama)
        String dialogText = (dialogues != null && dialogues.length > 0) ? dialogues[currentDialogueIndex] : "-";
        int screenX = worldX - gp.player.x + gp.player.screenX;
        int screenY = worldY - gp.player.y + gp.player.screenY;

        Graphics2D g2 = gp.getGraphics2D(); // Pastikan ada cara ambil Graphics2D dari GamePanel, atau terima g2 sebagai parameter

        Font font = new Font("Arial", Font.BOLD, 16);
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(dialogText);
        int textHeight = fm.getHeight();

        int padding = 10;
        int boxWidth = textWidth + padding * 2;
        int boxHeight = textHeight + padding * 2;

        int boxX = screenX + gp.tileSize / 2 - boxWidth / 2;
        int boxY = screenY - boxHeight - 10;

        // Kotak dialog (putih, border hitam)
        g2.setColor(new Color(255, 255, 255, 230));
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);

        // Teks dialog
        g2.setColor(Color.BLACK);
        g2.drawString(dialogText, boxX + padding, boxY + padding + fm.getAscent());
    }
}