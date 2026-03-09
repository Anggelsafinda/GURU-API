-- Membuat database (jika belum ada)
CREATE DATABASE IF NOT EXISTS guru_db;
USE guru_db;

-- Tabel untuk User (Admin)
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Tabel untuk Teacher (Guru)
CREATE TABLE IF NOT EXISTS teachers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nip VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    admin_id BIGINT,
    FOREIGN KEY (admin_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Insert data awal untuk User (Password: password123, sudah di hash menggunakan BCrypt)
-- Hash untuk "password123": $2a$10$wK/pGvSxwD3zB9I8yJ8H6u9/qYpZ9kK1mX9F57bVz5T8h8uY61P6W
-- Pastikan password asli di-hash dengan BCrypt yang sesuai dengan konfigurasi Spring Security Anda.
INSERT INTO users (username, password, role) VALUES 
('admin1', 'admin123', 'ROLE_ADMIN'),
('admin2', 'admin111', 'ROLE_ADMIN')
ON DUPLICATE KEY UPDATE username=username;

-- Insert data awal untuk Teacher
-- Mengaitkan guru dengan admin_id
-- Data untuk admin (ID=3 setelah admin1=1 dan admin2=2, atau sesuai urutan)
-- Data untuk admin1 (ID=1) dan admin2 (ID=2)
INSERT INTO teachers (nip, name, subject, is_active, admin_id) VALUES 
('198001012005011001', 'Budi Santoso', 'Matematika', TRUE, 1),
('198202022006022002', 'Siti Aminah', 'Bahasa Indonesia', TRUE, 1),
('198503032010031003', 'Andi Hermawan', 'Fisika', FALSE, 2),
('199004042015042004', 'Rina Marlina', 'Kimia', TRUE, 2),
('201001012010011001', 'Guru Matematika', 'Matematika', TRUE, (SELECT id FROM users WHERE username='admin')),
('201102022011022002', 'Guru Bahasa Indonesia', 'Bahasa Indonesia', TRUE, (SELECT id FROM users WHERE username='admin')),
('201203032012033003', 'Guru Fisika', 'Fisika', TRUE, (SELECT id FROM users WHERE username='admin')),
('201304042013044004', 'Guru Kimia', 'Kimia', TRUE, (SELECT id FROM users WHERE username='admin'))
ON DUPLICATE KEY UPDATE nip=nip;
