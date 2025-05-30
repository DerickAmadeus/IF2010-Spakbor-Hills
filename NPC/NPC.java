// Di dalam kelas NPC.java
package NPC;

import main.GamePanel;
import Items.*;



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

    // Dialog
    public String[] dialogues;
    public int currentDialogueIndex = 0;

    // Variabel untuk animasi (sudah ada)
    private BufferedImage[] idleFrames;
    private int spriteCounter = 0;
    private int spriteNum = 0;
    private final int ANIMATION_SPEED = 15;
    private final int IDLE_FRAME_COUNT = 6; // Anda set 6, sebelumnya saya contohkan 8
    private boolean showActionMenu = false;
    private String[] actions = {"Talk", "Give Item", "View Status", "Propose", "Marry", "Leave"};
    private int selectedActionIndex = 0;
    private String[] proposingAnswers = {"AAWWWWWWWWWWW SO SWEEETTTT. AKU MAUUUUUUUUUU", "Dih Effort Dulu Bang","Dah kau lamar bang aku", "Dah nikah kita"}; // Contoh jawaban untuk pertanyaan pernikahan



    // Variabel NPC
    public String name;
    public String spawnMapName; // Menggunakan int untuk ID map
    private int heartPoints;
    private Item[] lovedItems;
    private Item[] likedItems;
    private Item[] hatedItems;
    private String relationship;
    public boolean isTalking = false;
    public boolean isProposed = false;


    public NPC(GamePanel gp, String name, String spawnMapName, int tileX, int tileY, Item[] loveditems, Item[] likedItems, Item[] hatedItems) {
        this.gp = gp;
        this.name = name;
        heartPoints = 0;
        this.lovedItems = loveditems;
        this.likedItems = likedItems;
        this.hatedItems = hatedItems;

        






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
        if (this.name.equalsIgnoreCase("MT")) {
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

// NPC.java
    public void interact() {
        if (!showActionMenu) {
            showActionMenu = true;
            selectedActionIndex = 0;
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

    public String getSpawnMapName() { // Pastikan getter ini ada
        return spawnMapName;
    }

    // Getter untuk area interaksi NPC (dalam koordinat dunia)
    public Rectangle getInteractionTriggerAreaWorld() {
        return new Rectangle(worldX + hitbox.x, worldY + hitbox.y, hitbox.width, hitbox.height);
    }

    public void drawSubwindow(Graphics2D g2, int frameX, int frameY, int frameWidth, int frameHeight) {
        Color c = new Color(53,33,0, 255);
        g2.setColor(c);
        g2.fillRoundRect(frameX, frameY, frameWidth, frameHeight, 35, 35);
        c = new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(frameX+5, frameY+5, frameWidth-10, frameHeight-10, 25, 25);
    }

    public void showStatus(Graphics2D g2) {
        // draw Sub Windownya
        int frameX = gp.tileSize*9;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize*6;
        int frameHeight = gp.tileSize*5;
        drawSubwindow(g2, frameX, frameY, frameWidth, frameHeight);


        // Metode ini bisa digunakan untuk menampilkan status NPC, misalnya saat dialog
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics metrics = g2.getFontMetrics();
        String statusText = "NPC: " + name;
        int textWidth = metrics.stringWidth(statusText);
        int textHeight = metrics.getHeight();
        statusText += " (" + (relationship != null ? relationship : "Single") + ")";
        
        // Gambar latar belakang untuk teks
        g2.setColor(new Color(0, 0, 0, 150)); // Hitam transparan
        g2.fillRect(10, 10, textWidth + 10, textHeight + 5);
        
        // Gambar teks
        g2.setColor(Color.WHITE);
        g2.drawString(statusText, frameX + 20, frameY + 40);
        // Gambar garis bawah
        g2.setStroke(new BasicStroke(2));

        // Gambar heart points
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        String heartPointsText = "Heart Points: " + heartPoints;

        if (heartPoints == 150) {
            g2.setColor(Color.pink);
        } else {
            g2.setColor(Color.WHITE);
        }
        g2.drawString(heartPointsText, frameX + 20, frameY + 80);
        g2.setColor(Color.WHITE);


        // Gambar loved items
        int y = frameY + 100; // Mulai dari posisi Y setelah heart points
        FontMetrics fm = g2.getFontMetrics();
        int maxWidth = frameWidth - 40; // padding kiri-kanan
        int x = frameX + 20;

        if (lovedItems != null && lovedItems.length > 0) {
            String lovedItemsText = "Loved Items: ";
            for (Item item : lovedItems) {
            lovedItemsText += item.getName() + ", ";
            }
            // Hapus koma terakhir
            if (lovedItemsText.endsWith(", ")) {
            lovedItemsText = lovedItemsText.substring(0, lovedItemsText.length() - 2);
            }

            // Bungkus teks jika terlalu panjang untuk frame
            String[] words = lovedItemsText.split(" ");
            StringBuilder line = new StringBuilder();
            for (String word : words) {
            String testLine = line + (line.length() == 0 ? "" : " ") + word;
            if (fm.stringWidth(testLine) > maxWidth) {
                g2.drawString(line.toString(), x, y);
                y += fm.getHeight() + 4; // spacing antar baris
                line = new StringBuilder(word);
            } else {
                if (line.length() > 0) line.append(" ");
                line.append(word);
            }
            }
            if (line.length() > 0) {
            g2.drawString(line.toString(), x, y);
            y += fm.getHeight() + 8; // spacing antar section
            }
        }

        // Gambar liked items, mulai dari y setelah loved items
        if (likedItems != null && likedItems.length > 0) {
            String likedItemsText = "Liked Items: ";
            for (Item item : likedItems) {
            likedItemsText += item.getName() + ", ";
            }
            // Hapus koma terakhir
            if (likedItemsText.endsWith(", ")) {
            likedItemsText = likedItemsText.substring(0, likedItemsText.length() - 2);
            }

            // Bungkus teks jika terlalu panjang untuk frame
            String[] words = likedItemsText.split(" ");
            StringBuilder line = new StringBuilder();
            for (String word : words) {
            String testLine = line + (line.length() == 0 ? "" : " ") + word;
            if (fm.stringWidth(testLine) > maxWidth) {
                g2.drawString(line.toString(), x, y);
                y += fm.getHeight() + 4; // spacing antar baris
                line = new StringBuilder(word); 
            } else {
                if (line.length() > 0) line.append(" ");
                line.append(word);
            }
            }
            if (line.length() > 0) {
            g2.drawString(line.toString(), x, y);
            y += fm.getHeight() + 8; // spacing antar section
            }
        }

        // Gambar hated items, mulai dari y setelah liked items
        if (hatedItems != null && hatedItems.length > 0) {
            String hatedItemsText = "Hated Items: ";
            for (Item item : hatedItems) {
            hatedItemsText += item.getName() + ", ";
            }
            // Hapus koma terakhir
            if (hatedItemsText.endsWith(", ")) {
            hatedItemsText = hatedItemsText.substring(0, hatedItemsText.length() - 2);
            }

            // Bungkus teks jika terlalu panjang untuk frame
            String[] words = hatedItemsText.split(" ");
            StringBuilder line = new StringBuilder();
            for (String word : words) {
            String testLine = line + (line.length() == 0 ? "" : " ") + word;
            if (fm.stringWidth(testLine) > maxWidth) {
                g2.drawString(line.toString(), x, y);
                y += fm.getHeight() + 4; // spacing antar baris
                line = new StringBuilder(word); 
            } else {
                if (line.length() > 0) line.append(" ");
                line.append(word);
            }
            }
            if (line.length() > 0) {
            g2.drawString(line.toString(), x, y);
            y += fm.getHeight() + 8; // spacing antar section
            }
        } else {
            String hatedItemsText = "Hated Items: Seluruh item yang bukan merupakan lovedItems dan likedItems.";
            String[] words = hatedItemsText.split(" ");
            StringBuilder line = new StringBuilder();
            for (String word : words) {
            String testLine = line + (line.length() == 0 ? "" : " ") + word;
            if (fm.stringWidth(testLine) > maxWidth) {
                g2.drawString(line.toString(), x, y);
                y += fm.getHeight() + 4; // spacing antar baris
                line = new StringBuilder(word); 
            } else {
                if (line.length() > 0) line.append(" ");
                line.append(word);
            }
            }
            if (line.length() > 0) {
            g2.drawString(line.toString(), x, y);
            y += fm.getHeight() + 8; // spacing antar section
            }
            y += fm.getHeight() + 8; // spacing antar section
        }
        

    }

    public void drawActionMenu(Graphics2D g2) {
        int frameX = gp.tileSize * 1;
        int frameY = gp.tileSize * 8;
        int frameWidth = gp.tileSize * 14;
        int frameHeight = gp.tileSize * 3;

        drawSubwindow(g2, frameX, frameY, frameWidth, frameHeight);

        g2.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g2.getFontMetrics();

        int cols = 3;
        int rows = 2;
        int cellWidth = frameWidth / cols;
        int cellHeight = frameHeight / rows;

        for (int i = 0; i < actions.length; i++) {
            int col = i % cols;
            int row = i / cols;
            int x = frameX + col * cellWidth + (cellWidth - fm.stringWidth(actions[i])) / 2;
            int y = frameY + row * cellHeight + cellHeight / 2 + fm.getAscent() / 2;

            if (i == selectedActionIndex) {
                g2.setColor(Color.YELLOW);
            } else {
                g2.setColor(Color.WHITE);
            }
            g2.drawString(actions[i], x, y);
        }
    }

    public void selectAction(boolean leftPressed, boolean rightPressed) {
        if (leftPressed) {
            selectedActionIndex--;
            if (selectedActionIndex < 0) {
                selectedActionIndex = actions.length - 1; // Loop ke akhir
            }
        } else if (rightPressed) {
            selectedActionIndex++;
            if (selectedActionIndex >= actions.length) {
                selectedActionIndex = 0; // Loop ke awal
            }
        }
    }

    public String confirmAction() {
        return actions[selectedActionIndex]; // Mengembalikan aksi yang sedang dipilih
    }


    public void drawNPCDialog(Graphics2D g2, String speakerName) {
        int x = gp.tileSize * 1;
        int y = gp.tileSize * 8;
        int width = gp.tileSize * 14;
        int height = gp.tileSize * 3;

        drawSubwindow(g2, x, y, width, height);

        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.setColor(Color.WHITE);
        g2.drawString(speakerName + ":", x + 20, y + 35);
        g2.drawString(dialogues[currentDialogueIndex], x + 20, y + 70);

        if (currentDialogueIndex >= dialogues.length) {
            currentDialogueIndex = 0; // Reset dialog jika sudah selesai
            isTalking = false; // Selesai berbicara
            showActionMenu = true; // Tampilkan menu aksi setelah dialog selesai
        }
    }


    public boolean drawProposingAnswer(Graphics2D g2, String speakerName) {
        int x = gp.tileSize * 1;
        int y = gp.tileSize * 8;
        int width = gp.tileSize * 14;
        int height = gp.tileSize * 3;

        drawSubwindow(g2, x, y, width, height);

        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.setColor(Color.WHITE);
        g2.drawString(speakerName + ":", x + 20, y + 35);
        
        // Gambar pertanyaan pernikahan
        // Gambar pilihan jawaban
        if (heartPoints >= 150 && relationship != "Married" && gp.player.getEquippedItem().getName().equals("ring")) {
            g2.drawString(proposingAnswers[0], x + 20, y + 100); // Jawaban positif
            relationship = "Proposed"; // Set hubungan menjadi "Proposed"
            return true; // Mengembalikan true untuk menandakan bahwa pertanyaan pernikahan telah ditampilkan
        } else if (relationship == "Married") {
            g2.drawString(proposingAnswers[3], x + 20, y + 100); // Jawaban sudah menikah
            return true;

        } else {
            g2.drawString(proposingAnswers[1], x + 20, y + 100); // Jawaban menolak
            return false;
        }
        
    }






    

    public String getName() {
        return name;
    }

    public void addHeartPoints(int points) {
        heartPoints += points;
        if (heartPoints > 150) {
            heartPoints = 150; // Maksimal 100
        }
    }
}