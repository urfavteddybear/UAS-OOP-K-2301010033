-- Database creation script for Sistem Manajemen Penyewaan Kamera
-- Created by: Wahyu
-- Date: June 14, 2025

-- Create database
CREATE DATABASE IF NOT EXISTS sewa_kamera_db;
USE sewa_kamera_db;

-- Table: users
CREATE TABLE users (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'operator') NOT NULL DEFAULT 'operator',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table: barang
CREATE TABLE barang (
    id_barang INT AUTO_INCREMENT PRIMARY KEY,
    nama_barang VARCHAR(100) NOT NULL,
    stok INT NOT NULL DEFAULT 0,
    harga_sewa DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table: pelanggan
CREATE TABLE pelanggan (
    id_pelanggan INT AUTO_INCREMENT PRIMARY KEY,
    nama_pelanggan VARCHAR(100) NOT NULL,
    alamat TEXT NOT NULL,
    telepon VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table: transaksi
CREATE TABLE transaksi (
    id_transaksi INT AUTO_INCREMENT PRIMARY KEY,
    id_pelanggan INT NOT NULL,
    id_user INT NOT NULL,
    tanggal_sewa DATE NOT NULL,
    tanggal_kembali DATE NOT NULL,
    durasi INT NOT NULL,
    total_harga DECIMAL(12,2) NOT NULL,
    status ENUM('Aktif', 'Selesai', 'Terlambat') NOT NULL DEFAULT 'Aktif',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_pelanggan) REFERENCES pelanggan(id_pelanggan) ON DELETE CASCADE,
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE
);

-- Table: detail_transaksi
CREATE TABLE detail_transaksi (
    id_detail INT AUTO_INCREMENT PRIMARY KEY,
    id_transaksi INT NOT NULL,
    id_barang INT NOT NULL,
    jumlah INT NOT NULL,
    harga_satuan DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_transaksi) REFERENCES transaksi(id_transaksi) ON DELETE CASCADE,
    FOREIGN KEY (id_barang) REFERENCES barang(id_barang) ON DELETE CASCADE
);

-- Insert default admin user
INSERT INTO users (nama, username, password, role) VALUES 
('Administrator', 'admin', '197f4186d0809ae24a02faa5f2319179ab80cb1a1416d72e78f375fe8edd0315', 'admin'),
('Operator User', 'operator', 'b3a78ea6432b350ee22a0e9ae06e06e38e6569887bd6daaf30a690db1cf3d1e5', 'operator');

-- Insert sample barang data
INSERT INTO barang (nama_barang, stok, harga_sewa) VALUES 
('Canon EOS 80D', 5, 150000),
('Nikon D7500', 3, 140000),
('Sony A7 III', 4, 200000),
('Canon 50mm f/1.8', 8, 50000),
('Nikon 85mm f/1.4', 6, 75000),
('Tripod Manfrotto', 10, 25000),
('Flash Speedlite', 12, 30000),
('Memory Card 64GB', 20, 15000);

-- Insert sample pelanggan data
INSERT INTO pelanggan (nama_pelanggan, alamat, telepon, email) VALUES 
('John Doe', 'Jl. Sudirman No. 123, Jakarta', '081234567890', 'john@email.com'),
('Jane Smith', 'Jl. Thamrin No. 456, Jakarta', '081234567891', 'jane@email.com'),
('Ahmad Rizki', 'Jl. Gatot Subroto No. 789, Jakarta', '081234567892', 'ahmad@email.com'),
('Sari Indah', 'Jl. Kuningan No. 101, Jakarta', '081234567893', 'sari@email.com'),
('Budi Santoso', 'Jl. Senayan No. 202, Jakarta', '081234567894', 'budi@email.com');

-- Create indexes for better performance
CREATE INDEX idx_transaksi_pelanggan ON transaksi(id_pelanggan);
CREATE INDEX idx_transaksi_user ON transaksi(id_user);
CREATE INDEX idx_transaksi_tanggal ON transaksi(tanggal_sewa, tanggal_kembali);
CREATE INDEX idx_detail_transaksi ON detail_transaksi(id_transaksi);
CREATE INDEX idx_detail_barang ON detail_transaksi(id_barang);

-- Show tables
SHOW TABLES;
