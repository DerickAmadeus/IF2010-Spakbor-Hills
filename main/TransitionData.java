package main; // Anda bisa meletakkannya di package yang sesuai, misal 'Map' atau 'Events'

import java.awt.Rectangle;

public class TransitionData {
    public int sourceMapID; // ID peta asal tempat transisi ini berada
    public Rectangle sourceArea; // Area pemicu dalam koordinat PIXEL dunia di peta asal
    public int targetMapID; // ID peta tujuan
    public int targetPlayerX; // Koordinat X baru pemain dalam PIXEL dunia di peta tujuan
    public int targetPlayerY; // Koordinat Y baru pemain dalam PIXEL dunia di peta tujuan
    public boolean requiresInteraction; // Apakah perlu menekan tombol interaksi (belum diimplementasikan penuh di contoh ini)
    public int cooldownFrames = 0; // Cooldown dalam frame untuk mencegah re-trigger instan
    public static final int DEFAULT_COOLDOWN = 30; // Sekitar 0.5 detik pada 60 FPS

    /**
     * Konstruktor untuk TransitionData.
     * @param sourceMapID ID peta asal.
     * @param srcX_tile X-koordinat tile kiri atas area pemicu (dalam satuan tile).
     * @param srcY_tile Y-koordinat tile kiri atas area pemicu (dalam satuan tile).
     * @param srcWidth_tiles Lebar area pemicu (dalam satuan tile).
     * @param srcHeight_tiles Tinggi area pemicu (dalam satuan tile).
     * @param targetMapID ID peta tujuan.
     * @param targetPlayerTileX X-koordinat tile baru pemain di peta tujuan.
     * @param targetPlayerTileY Y-koordinat tile baru pemain di peta tujuan.
     * @param requiresInteraction True jika pemain perlu menekan tombol interaksi.
     * @param gpTileSize Ukuran tile dalam pixel (misalnya, GamePanel.tileSize).
     */
    public TransitionData(int sourceMapID, int srcX_tile, int srcY_tile, int srcWidth_tiles, int srcHeight_tiles,
                        int targetMapID, int targetPlayerTileX, int targetPlayerTileY,
                        boolean requiresInteraction, int gpTileSize) {
        this.sourceMapID = sourceMapID;
        // Area pemicu dikonversi ke koordinat pixel
        this.sourceArea = new Rectangle(srcX_tile * gpTileSize, srcY_tile * gpTileSize,
                                        srcWidth_tiles * gpTileSize, srcHeight_tiles * gpTileSize);
        this.targetMapID = targetMapID;
        // Posisi target pemain dikonversi ke koordinat pixel
        this.targetPlayerX = targetPlayerTileX * gpTileSize;
        this.targetPlayerY = targetPlayerTileY * gpTileSize;
        this.requiresInteraction = requiresInteraction;
    }

    /**
     * Memeriksa apakah area solid pemain memicu transisi ini.
     * @param playerAbsoluteSolidArea Area solid pemain dalam koordinat dunia absolut.
     * @param currentMapID ID peta tempat pemain saat ini berada.
     * @return True jika transisi terpicu, false jika tidak.
     */
    public boolean isTriggered(Rectangle playerAbsoluteSolidArea, int currentMapID) {
        if (this.sourceMapID != currentMapID || cooldownFrames > 0) {
            return false; // Bukan peta yang benar atau sedang cooldown
        }
        return playerAbsoluteSolidArea.intersects(this.sourceArea);
        
    }

    public void updateCooldown() {
        if (cooldownFrames > 0) {
            cooldownFrames--;
        }
    }

    public void startCooldown() {
        this.cooldownFrames = DEFAULT_COOLDOWN;
    }

}