-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Oct 11, 2026 at 01:52 PM
-- Server version: 8.0.30
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cominfo`
--

-- --------------------------------------------------------

--
-- Table structure for table `pengajuan`
--

CREATE TABLE `pengajuan` (
  `id_pengajuan` int NOT NULL,
  `nama_pemohon` varchar(100) NOT NULL,
  `departemen` varchar(100) NOT NULL,
  `kategori` enum('Digital','Cetak') NOT NULL,
  `catatan` varchar(255) DEFAULT NULL,
  `deadline` varchar(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT 'Diterima',
  `target_platform` varchar(50) DEFAULT NULL,
  `format_file` varchar(20) DEFAULT NULL,
  `ukuran_media` varchar(50) DEFAULT NULL,
  `jumlah_cetak` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `pengajuan`
--

INSERT INTO `pengajuan` (`id_pengajuan`, `nama_pemohon`, `departemen`, `kategori`, `catatan`, `deadline`, `status`, `target_platform`, `format_file`, `ukuran_media`, `jumlah_cetak`) VALUES
(1, 'Yahya Jailani', 'Biro EDEN', 'Digital', 'Poster INSTAND', '09/09/2026', 'Diterima', 'Instagram Feed', 'PNG', NULL, NULL),
(2, 'Ahmad Ahdasuki', 'Departemen PSD', 'Cetak', 'Sertifikat ISC', '17/10/2026', 'Diterima', NULL, NULL, 'A4 Hardpaper', 50);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `pengajuan`
--
ALTER TABLE `pengajuan`
  ADD PRIMARY KEY (`id_pengajuan`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
