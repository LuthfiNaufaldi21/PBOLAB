# 🌱 CrowdCare

> Platform crowdfunding sosial berbasis desktop yang menghubungkan donatur dengan penggalang dana secara transparan dan aman.

<br>

## 📌 Deskripsi Aplikasi

**CrowdCare** adalah aplikasi crowdfunding desktop yang dikembangkan menggunakan Java sebagai proyek UAS Pemrograman Berorientasi Objek. Aplikasi ini memungkinkan siapa saja untuk membuat campaign penggalangan dana maupun berdonasi ke campaign yang sudah ada, dengan proses verifikasi oleh admin untuk memastikan transparansi dan keamanan platform.

CrowdCare memiliki tiga jenis pengguna dengan peran yang berbeda:
- **Donor** — dapat menelusuri dan berdonasi ke campaign aktif
- **Fundraiser (Penggalang Dana)** — dapat membuat dan mengelola campaign
- **Admin** — memverifikasi campaign sebelum ditayangkan ke publik

<br>

## ✨ Fitur-Fitur Utama

### 🔐 Autentikasi & Manajemen Akun
- Registrasi akun dengan pemilihan role (Donor / Fundraiser)
- Login dengan validasi username dan password
- Edit profil dan ganti password
- Session management per pengguna

### 🎯 Manajemen Campaign (Fundraiser)
- Buat campaign baru dengan judul, deskripsi, target dana, tanggal, kategori, dan foto
- Validasi input: target dana minimal Rp 10.000, tanggal selesai tidak boleh sebelum tanggal mulai
- Pantau status campaign (PENDING / ACTIVE / REJECTED)
- Lihat daftar campaign milik sendiri beserta dana yang terkumpul

### 💰 Donasi (Donor)
- Telusuri semua campaign aktif yang sudah disetujui
- Filter campaign berdasarkan kategori
- Donasi ke campaign pilihan dengan nominal bebas
- Lihat riwayat seluruh donasi yang pernah dilakukan

### 🛡️ Panel Admin
- Dashboard dengan statistik platform (total user, campaign, donasi)
- Setujui atau tolak campaign yang masuk (status PENDING)
- Kelola dan pantau seluruh pengguna terdaftar
- Akses laporan aktivitas platform

<br>

## 🏗️ Arsitektur & Teknologi

| Komponen | Teknologi |
|---|---|
| Frontend / GUI | JavaFX 21 + FXML |
| Backend / REST API | Spring Boot 3.2.5 |
| Database | H2 Database (file-based) |
| ORM | Spring Data JPA / Hibernate |
| Validasi | Spring Boot Validation (`@NotBlank`, `@Min`, dll.) |
| Keamanan | Spring Security |
| Build Tool | Apache Maven |
| Bahasa | Java 17 |

### Struktur Arsitektur
```
src/main/java/com/crowdcare/
├── model/          → Abstract class & domain model (4 pilar PBO)
│   ├── User.java        (abstract - Abstraction & Encapsulation)
│   ├── Donor.java       (Inheritance & Polymorphism)
│   ├── Fundraiser.java  (Inheritance & Polymorphism)
│   └── Admin.java       (Inheritance & Polymorphism)
├── entity/         → JPA Entity untuk ORM
├── repository/     → Repository layer (Spring Data JPA)
├── service/        → Service layer (business logic)
├── api/            → REST API Controller (Spring Boot)
├── controller/     → JavaFX Controller (MVC)
├── config/         → Security & Data Seeder
└── session/        → Session management JavaFX
```

<br>

## ⚙️ Cara Menjalankan Aplikasi

### Prasyarat

Pastikan perangkat kamu sudah terpasang:
- **Java Development Kit (JDK) 17** atau lebih baru → [Download JDK](https://adoptium.net/)
- **Apache Maven 3.8+** → [Download Maven](https://maven.apache.org/download.cgi)
- **Git** → [Download Git](https://git-scm.com/)

### Langkah Instalasi

**1. Clone repositori ini**
```bash
git clone https://github.com/[username]/UAS_PBO_CrowdCare_[NamaKelompok].git
cd UAS_PBO_CrowdCare_[NamaKelompok]
```

**2. Build proyek menggunakan Maven**
```bash
mvn clean install -DskipTests
```

**3. Jalankan aplikasi**
```bash
mvn javafx:run
```

Aplikasi akan membuka jendela login CrowdCare. Spring Boot berjalan di background secara otomatis pada port **8080**.

### Akun Default (Data Seeder)

Saat pertama kali dijalankan, aplikasi otomatis membuat akun berikut:

| Role | Username | Password |
|---|---|---|
| Admin | `admin` | `admin123` |
| Fundraiser | `fundraiser1` | `pass123` |
| Donor | `donor1` | `pass123` |

> **Catatan:** H2 Console dapat diakses di `http://localhost:8080/h2-console` dengan JDBC URL `jdbc:h2:file:./crowdcare-db` untuk keperluan debugging database.

<br>

## 🎥 Video Presentasi

▶️ **[Tonton di YouTube](...)**

<br>

## 👥 Anggota Kelompok

| Nama | NIM |
|---|---|
| [Ahmad Fazri Berutu] | [241401015] |
| [Luthfi Naufaldi] | [241401036] |
| [Akief Maulana] | [241401072] |
| [Argha Niqie Wijaksono] | [241401096] |

<br>

---
*UAS Laboratorium Pemrograman Berorientasi Objek — Semester Genap T.A. 2025/2026*  
*Program Studi Ilmu Komputer, Fasilkom-TI, Universitas Sumatera Utara*
