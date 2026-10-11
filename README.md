# Sistem Pengajuan Desain Departemen COMINFO

---

## Deskripsi Singkat Program

COMINFO (Communication and Media Information) adalah departemen di himpunan mahasiswa Sistem Informasi, INFORSA. Di dalamnya ada divisi Visual Creative yang menangani urusan desain untuk seluruh departemen/biro INFORSA, mulai dari feeds dan poster sampai kebutuhan visual lain. Selama ini pengajuan desain dilakukan lewat formulir manual yang berisi nama pemohon, departemen/biro, jenis desain, informasi tambahan, dan deadline.

Untuk tugas mini project 4 mata kuliah Pemrograman Berorientasi Objek (PBO), saya membuat program CLI berbasis Java yang menggantikan formulir manual tersebut. Pemohon bisa menambah, melihat, mengubah, dan menghapus pengajuan desain. Pengajuan dibagi jadi dua jenis, yaitu **Desain Digital** (misalnya untuk Instagram Feed, Story, atau Web Banner) dan **Desain Cetak** (misalnya poster atau sertifikat fisik), karena data yang dibutuhkan keduanya berbeda.

Data pengajuan disimpan di database MySQL dan diakses lewat JDBC dengan Prepared Statement dan pola DAO (Data Access Object), jadi data tetap ada setelah program ditutup. Program memakai struktur MVC serta menerapkan abstraction dan polymorphism.

---

## Penjelasan Struktur Package

Project ini dibagi menjadi 6 package.

![Struktur Package](/aset/struktur-package.png)

Package **cominfo** hanya berisi class `Main`, pintu masuk program. Di sini dibuat object `PengajuanController` dan `PengajuanView`, lalu view dijalankan.

Package **model** berisi class yang menyimpan data beserta aturannya: `Pemohon`, class abstract `PengajuanDesain` dengan dua turunannya `PengajuanDigital` dan `PengajuanCetak`, serta interface `Trackable`. Validasi data di constructor dan setter juga ada di package ini.

Package **view** berisi `PengajuanView`, yang hanya menampilkan menu, membaca input user, dan menampilkan hasilnya. Semua pemrosesan data diteruskan ke controller.

Package **controller** berisi `PengajuanController`. Class ini mengecek ID, membuat object model, lalu meneruskannya ke DAO untuk ditambah, dicari, diubah, atau dihapus.

Package **DAO** berisi interface `PengajuanDAO`, yang mendaftar operasi apa saja yang tersedia ke database, dan class `PengajuanDAOImpl`, yang menjalankan operasi itu dengan query SQL lewat JDBC. Hanya package ini yang berhubungan langsung dengan database.

Package **util** berisi dua class. `InputValidator` membaca dan memvalidasi input user, seperti input kosong, bukan angka, atau di luar pilihan menu. `Database` menyimpan konfigurasi dan membuka koneksi ke MySQL.

---

## Penjelasan Alur Program

Program dijalankan lewat class `Main` di package `cominfo`, yang membuat object `PengajuanController` dan `PengajuanView`, lalu memanggil `view.jalankan()`. Saat `PengajuanController` dibuat, ia menyiapkan object `PengajuanDAOImpl` untuk dipakai selama program berjalan. Data awal diisi lewat file `.sql` di database, tidak lewat kode Java, jadi begitu menu Tampilkan dipilih, data langsung muncul tanpa perlu input dari awal.

`PengajuanView.jalankan()` menampilkan menu utama dengan 5 pilihan (Tambah, Tampilkan, Update, Hapus, Keluar) dalam perulangan `while` yang baru berhenti kalau user memilih 5. Pilihan menu dibaca lewat `InputValidator`. Kalau user mengetik huruf atau angka di luar pilihan, program memberi pemberitahuan dan meminta input ulang, jadi tidak crash.

![Menu Utama](/aset/menu.png)

Kalau user memilih **Tambah Pengajuan**, program meminta ID (harus angka positif dan belum dipakai), nama, departemen/biro, catatan, dan deadline. Input kosong ditolak dan user diminta mengisi lagi. Setelah itu user memilih jenis desain:

- Kalau pilih **Digital**, user memilih target platform (Instagram Feed / Story / Web Banner) dan format file (PNG / JPG / PDF) lewat menu pilihan, tanpa ketik manual.
- Kalau pilih **Cetak**, user memilih ukuran media (A4 Hardpaper / A3 Poster / Banner 2x1m) lewat menu pilihan, lalu mengisi jumlah cetak yang harus lebih dari 0.

Data yang sudah lengkap dikirim ke controller lewat `tambahPengajuanDigital()` atau `tambahPengajuanCetak()`. Controller membuat object `PengajuanDigital` atau `PengajuanCetak`, lalu memanggil `tambahDigital()` atau `tambahCetak()` milik DAO untuk menyimpannya ke tabel `pengajuan`. Kalau ada data yang tidak lolos validasi di model, view menangkap pesan errornya dan menampilkannya ke user.

![Tambah Pengajuan Digital](/aset/tambah-digital.png)

![Tambah Pengajuan Cetak](/aset/tambah-cetak.png)

![Validasi Input](/aset/validasi-input.png)

Untuk **Tampilkan Pengajuan**, controller memanggil `getSemua()` milik DAO untuk membaca seluruh isi tabel `pengajuan`. Tiap baris diubah menjadi object `PengajuanDigital` atau `PengajuanCetak` sesuai kolom `kategori`. View lalu menelusuri hasilnya, memanggil `tampilkanDetail()` milik tiap object, dan menambahkan baris `Tracker` dari `getRincianStatus()`. Kalau belum ada data, program menampilkan pesan bahwa data masih kosong.

![Tampilkan Pengajuan](/aset/tampilkan.png)

Pada **Update**, user memasukkan ID lalu program mencarinya lewat `cariBerdasarkanId()` di controller, yang meneruskannya ke `getById()` di DAO. Kalau ketemu, user memilih mau mengubah catatan saja atau catatan sekaligus status. Controller kemudian memanggil `updateRevisi()` atau `updateRevisiDanStatus()`, dan DAO menjalankan perintah `UPDATE` ke database. Kalau ID tidak ditemukan, program menampilkan pesan data tidak ditemukan.

![Update Catatan](/aset/update-catatan.png)

![Update Catatan dan Status](/aset/update-catatan-dan-status.png)

Pada **Hapus**, user juga memasukkan ID. Controller memanggil `hapusPengajuan()`, yang meneruskannya ke `hapus()` di DAO untuk menjalankan perintah `DELETE`. Kalau ID tidak cocok, program memberi pesan tidak ditemukan.

![Hapus Pengajuan](/aset/hapus.png)

Menu **Keluar** menghentikan perulangan dan program selesai. Karena data tersimpan di database, pengajuan yang sudah ditambah atau diubah masih ada saat program dijalankan lagi.

---

## Penjelasan Penerapan Encapsulation dan Inheritance

**Encapsulation** semua atribut di class `Pemohon`, `PengajuanDesain`, `PengajuanDigital`, dan `PengajuanCetak` dideklarasikan `private`, jadi tidak bisa diakses langsung dari luar class. Nilainya diambil atau diubah lewat method `public` seperti getter dan setter (`getNamaLengkap()`, `getIdPengajuan()`, `setStatus()`, dan seterusnya). Setter diberi validasi: kalau nilainya kosong atau tidak masuk akal (misalnya jumlah cetak 0), setter melempar `IllegalArgumentException` dengan pesan yang menjelaskan kesalahannya.

![Encapsulation Pemohon](/aset/encapsulation-pemohon.png)

**Inheritance** `PengajuanDesain` adalah superclass abstract yang menyimpan atribut dan method umum (id, pemohon, catatan, deadline, status). Dua turunannya, `PengajuanDigital` dan `PengajuanCetak`, memakai `extends PengajuanDesain` dan memanggil `super(...)` di constructor untuk mewarisi data dari superclass. Masing-masing lalu menambah atribut khususnya: target platform dan format file untuk Digital, ukuran media dan jumlah cetak untuk Cetak.

![Inheritance-superclass](/aset/super-class.png)

![Inheritance-subclass-digital](/aset/subclass-digital.png)

![Inheritance-subclass-cetak](/aset/subclass-cetak.png)

---

## Penjelasan Penerapan Polymorphism dan Abstraction

**Abstraction** `PengajuanDesain` adalah abstract class, jadi object-nya tidak bisa dibuat langsung. Di dalamnya ada dua abstract method, `getKategori()` dan `tampilkanDetail()`, yang hanya berisi deklarasi tanpa isi. Setiap subclass wajib menulis ulang keduanya, karena cara menampilkan detail pengajuan Digital dan Cetak memang berbeda.

![Abstraction](/aset/abstraction.png)

**Polymorphism (Method Overriding)** `getKategori()` dan `tampilkanDetail()` di-override di `PengajuanDigital` dan `PengajuanCetak` dengan anotasi `@Override`. Digital menampilkan target rilis dan format file, Cetak menampilkan ukuran media dan jumlah cetak. Data dari database dikumpulkan dalam `ArrayList` bertipe `PengajuanDesain` yang isinya campuran object Digital dan Cetak. Di `PengajuanView`, list itu ditelusuri dengan satu perulangan yang memanggil `p.tampilkanDetail()`, dan Java otomatis menjalankan versi method sesuai jenis object-nya.

![Overriding-digital](/aset/overriding-digital.png)

![Overriding-cetak](/aset/overriding-cetak.png)

![Pemanggilan di View](/aset/pemanggilan-view.png)

**Polymorphism (Method Overloading)** di `PengajuanDesain.java` ada dua method `updateInformasi()` dengan parameter berbeda: satu menerima `catatanRevisi` saja, satu lagi menerima `catatanRevisi` dan `status` sekaligus. Dua versi ini sesuai dengan dua pilihan di menu update, yaitu mengubah catatan saja atau catatan beserta status.

![Overloading](/aset/overloading.png)

---

## Penjelasan Penerapan JDBC

Koneksi ke database dibuat oleh class `Database` di package `util`. Class ini menyimpan URL (`jdbc:mysql://localhost:3306/cominfo`), username, dan password, lalu membuka koneksi ke MySQL lewat `DriverManager` di method `getConnection()`. Driver yang dipakai adalah `mysql-connector-j-8.3.0`, ditambahkan sebagai dependency lewat Maven (`pom.xml`).

![Class Database](/aset/database.png)

Semua data pengajuan disimpan dalam satu tabel bernama `pengajuan` di database `cominfo`. Kolomnya `id_pengajuan`, `nama_pemohon`, `departemen`, `kategori`, `catatan`, `deadline`, dan `status`, ditambah kolom khusus Digital (`target_platform`, `format_file`) dan kolom khusus Cetak (`ukuran_media`, `jumlah_cetak`). Kolom `kategori` berisi `Digital` atau `Cetak`, dan nilainya menentukan object mana yang dibuat saat data dibaca.

Data awal diisi lewat file `.sql` yang berisi perintah `INSERT`, tidak lewat kode Java. File itu tinggal di-import ke MySQL, jadi saat menu Tampilkan dijalankan pertama kali, data langsung tampil dari database.

![Hasil SELECT data awal](/aset/select-database.png)

Untuk pola DAO, interface `PengajuanDAO` mendefinisikan 7 operasi: `tambahDigital()`, `tambahCetak()`, `getSemua()`, `getById()`, `updateRevisi()`, `updateRevisiDanStatus()`, dan `hapus()`. Class `PengajuanDAOImpl` mengimplementasikan semuanya dengan query SQL. Controller hanya memegang variabel bertipe `PengajuanDAO`, jadi tidak perlu tahu cara data disimpan, dan view tidak menyentuh database sama sekali.

![Interface DAO](/aset/dao-interface.png)

Query yang membawa nilai dari user ditulis dengan tanda `?` sebagai placeholder, lalu diisi lewat `setInt()` dan `setString()` sebelum dijalankan. Dengan begitu nilai input dikirim terpisah dari perintah SQL, sehingga lebih aman dari SQL injection. Perintah yang mengubah data (`INSERT`, `UPDATE`, `DELETE`) dijalankan dengan `executeUpdate()`, sedangkan `SELECT` dengan `executeQuery()`.

![Insert dengan Prepared Statement](/aset/prepared-insert.png)

![Update dan Delete dengan Prepared Statement](/aset/prepared-update-delete.png)

Pada `getSemua()` dan `getById()`, hasil query dibaca baris demi baris lewat `ResultSet`. Setiap baris diubah menjadi object `Pemohon`, lalu `PengajuanDigital` atau `PengajuanCetak` sesuai nilai kolom `kategori`, dan status dari database dipasang lewat `updateStatus()`.

![Membaca ResultSet](/aset/resultset.png)

Koneksi dan statement dibuka dengan `try-with-resources`, jadi otomatis tertutup setelah selesai dipakai tanpa `close()` manual. Error dari database ditangkap dengan `catch (SQLException e)` dan pesannya ditampilkan, sehingga program tidak langsung berhenti. Method `updateRevisi()`, `updateRevisiDanStatus()`, dan `hapus()` mengembalikan `true` kalau ada baris yang terpengaruh (`executeUpdate() > 0`), dan `false` kalau tidak ada.
