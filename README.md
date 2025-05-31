# Spakbor Hills

Spakbor Hills adalah permainan 2D _Top Down RPG Game_ yang kami kerjakan untuk memenuhi tugas besar IF2010 - Pemorgraman Berorientasi Objek. 

## Lore
Setelah kegagalannya yang ke-420 dalam mencoba menguasai wilayah Danville, Dr. Asep Spakbor mulai merasakan kelelahan yang mendalam. Tidak ada ledakan besar, tidak ada kekacauan spektakuler—hanya kekalahan yang sunyi. Seperti biasa, Agen Purry telah menghentikannya. Tapi kali ini... rasanya berbeda. Bukan kekalahan biasa, tapi semacam... titik balik. Atau mungkin, titik lelah.

Sayangnya, masalah tidak berhenti di sana. Kondisi ekonomi global memburuk. Tarif ekspor-impor melonjak, bahan baku inator naik harga, dan investasi Dr. Asep anjlok semua. Saat ia menatap saldo tabungannya yang semakin menipis, dan menyaksikan nilai tukar mata uang Danville merosot tajam, satu kenyataan menghantam:


“Dia akan bangkrut”


Bukan hanya gagal menguasai dunia—dunianya sendiri pun hancur berantakan.

Dalam keputusasaan itu, Dr. Asep memutuskan untuk me-reset hidupnya dengan melakukan sesuatu yang belum pernah ia lakukan: bertani.


“Kalau aku tidak bisa menguasai dunia, setidaknya aku bisa menguasai lahan dan tanaman!”

Kata Dr. Asep Spakbor dengan semangat yang... agak terlalu berapi-api.

Sayangnya, seperti biasa, ide brilian itu berubah jadi bencana. Bukannya panen, tidak ada tanamannya yang berhasil tumbuh. Satu-satunya yang berhasil tumbuh... adalah frustrasi.

Di sisi lain, O.W.C.A—Organisasi Warga Cool Abiez—mulai melihat sesuatu yang tidak pernah mereka temui sebelumnya: kesempatan untuk berdamai. Untuk pertama kalinya, Dr. Asep tidak sedang membangun alat penghancur dunia. Ia hanya ingin menanam dan hidup sederhana. Dan O.W.C.A tahu. Jika mereka bisa menjadikan Dr. Asep Spakbor petani sukses, mungkin, untuk pertama kalinya dalam sejarah Danville... segalanya akan damai.

Lalu, apa hubungannya dengan kamu? Seorang agen biasa yang kelihatan paling sering menggunakan komputer padahal kamu cuma sering main Minesweeper di jam kerja.. Tapi malam itu, kamu tiba-tiba dipanggil langsung oleh Purry dan petinggi-petinggi lainnya melalui briefing rahasia.

**Misi kamu jelas:**

**“Buatkan game bertani khusus untuk Dr. Asep Spakbor.”**


Game yang cukup menarik untuk membuatnya belajar, cukup aman agar tidak ada kerusakan, dan cukup cerdas untuk mengubah mantan ilmuwan jahat menjadi seorang petani handal.

Kamu tidak tahu kenapa kamu yang dipilih. Tapi agen Purry sudah mengangguk. Dan kamu tahu, kalau seekor platipus sudah mengangguk, maka dunia sedang serius. Ini bukan cuma soal membuat game. Ini soal menyelamatkan dunia.

# How To Run The Game
Berikut adalah langkah-langkah untuk menjalankan game ini di komputer Anda.

## Prasyarat

Pastikan Anda memiliki perangkat lunak berikut terinstal di sistem Anda:

1.  **Java Development Kit (JDK)**:
    * Versi 11 atau lebih baru direkomendasikan (misalnya, JDK 17, JDK 21).
    * Anda bisa mengunduhnya dari [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) atau alternatif OpenJDK seperti [Adoptium Temurin](https://adoptium.net/).
    * Pastikan `JAVA_HOME` environment variable sudah terkonfigurasi dan `java` serta `javac` bisa diakses dari terminal/command prompt.

2.  **(Opsional, tapi Sangat Direkomendasikan)** **Integrated Development Environment (IDE)**:
    * IDE akan mempermudah proses kompilasi dan menjalankan game, terutama jika Anda ingin melihat kode sumbernya.
    * Pilihan populer:
        * [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) (Community Edition gratis)
        * [Eclipse IDE for Java Developers](https://www.eclipse.org/downloads/packages/)
        * [Visual Studio Code](https://code.visualstudio.com/) dengan ekstensi Java (misalnya, "Extension Pack for Java" dari Microsoft).

3.  **(Opsional)** **Git**:
    * Jika Anda ingin mengambil kode sumber langsung dari repositori Git.
    * Unduh dari [git-scm.com](https://git-scm.com/downloads).

## Mendapatkan Kode Sumber

Ada beberapa cara untuk mendapatkan kode sumber game:

1.  **Melalui Git (Direkomendasikan jika Anda ingin berkontribusi atau mendapatkan update terbaru):**
    Buka terminal atau Git Bash, lalu jalankan perintah:
    ```bash
    git clone [URL_GIT_REPOSITORI_ANDA]
    ```
    Ganti `[URL_GIT_REPOSITORI_ANDA]` dengan URL repositori Git game Anda.

2.  **Mengunduh sebagai ZIP:**
    * Jika kode di-hosting di platform seperti GitHub, biasanya ada opsi "Download ZIP".
    * Setelah diunduh, ekstrak file ZIP tersebut ke direktori pilihan Anda.

## Cara Menjalankan

Ada beberapa cara untuk menjalankan game, tergantung preferensi Anda:

### 1. Melalui IDE (Cara Paling Mudah Jika Memiliki Kode Sumber)

Ini adalah cara yang paling direkomendasikan jika Anda memiliki kode sumber dan ingin menjalankannya.

1.  **Buka Proyek di IDE:**
    * Buka IDE Anda (IntelliJ IDEA, Eclipse, VS Code).
    * Pilih opsi "Open Project" atau "Import Project".
    * Navigasi ke direktori tempat Anda menyimpan kode sumber game dan buka folder proyeknya. IDE biasanya akan otomatis mendeteksi konfigurasi proyek Java.

2.  **Temukan Kelas Utama (Main Class):**
    * Kelas utama adalah file Java yang berisi metode `public static void main(String[] args)`.
    * Berdasarkan struktur proyek game Java pada umumnya dan kode yang pernah Anda berikan, kelas utama kemungkinan besar adalah `main.Main.java` (yang membuat instance dari `JFrame` dan `GamePanel`).

3.  **Jalankan Game:**
    * Setelah proyek berhasil dibuka dan diindeks oleh IDE, cari file kelas utama tersebut (`Main.java` di dalam package `main`).
    * Klik kanan pada file tersebut dan pilih opsi seperti "Run 'Main.main()'" atau "Run As Java Application".
    * Atau, buka file tersebut dan cari tombol "Play" (segitiga hijau) di samping metode `main` atau di toolbar IDE.

    Game seharusnya akan terkompilasi (jika perlu) dan jendela game akan muncul.
## Authors

- [@Derick Amadeus Budiono]
- [@Michael Ballard Isaiah Silaen]
- [@Gabriella Jennifer Sendi]
- [@Ahmad Evander Ruizhi Xavier]

