package com.example.GURU.API.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity Class untuk menyimpan informasi Akun Admin.
 * Admin digunakan untuk melakukan login pada sistem.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password; // Menyimpan password yang sudah di-hash
    private String role; // Role user, misalnya "ROLE_ADMIN"

    /**
     * Constructor kosong (No-argument constructor) untuk entity User.
     * Diperlukan oleh Hibernate/JPA untuk membuat instance dari hasil query database.
     */
    public User() {}

    /**
     * Constructor untuk membuat instance User dengan semua parameter.
     * Digunakan saat membuat akun admin/pengguna baru.
     * 
     * @param username Username unik untuk login ke sistem
     * @param password Password user (WAJIB SUDAH DI-HASH menggunakan BCrypt sebelum disimpan)
     * @param role Role/peran user dalam sistem (format: "ROLE_X", contoh: "ROLE_ADMIN")
     */
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // ======================== Getter dan Setter ========================

    /**
     * Mendapatkan ID unik pengguna (admin) di database.
     * @return ID user
     */
    public Long getId() {
        return id;
    }

    /**
     * Mengatur ID unik pengguna (biasanya auto-generated).
     * @param id ID user baru
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Mendapatkan username pengguna untuk login.
     * @return Username pengguna
     */
    public String getUsername() {
        return username;
    }

    /**
     * Mengatur username pengguna untuk login.
     * @param username Username baru
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Mendapatkan password pengguna (dalam bentuk hash BCrypt).
     * PERHATIAN: Password selalu tersimpan dalam bentuk hash, bukan teks biasa.
     * @return Password yang sudah di-hash
     */
    public String getPassword() {
        return password;
    }

    /**
     * Mengatur password pengguna.
     * PENTING: Pastikan password sudah di-hash sebelum disimpan (gunakan PasswordEncoder).
     * @param password Password baru (harus dalam bentuk hash)
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Mendapatkan role/peran pengguna dalam sistem (contoh: ROLE_ADMIN).
     * @return Role pengguna
     */
    public String getRole() {
        return role;
    }

    /**
     * Mengatur role/peran pengguna dalam sistem.
     * Recommended format: "ROLE_X" (contoh: "ROLE_ADMIN", "ROLE_USER")
     * @param role Role pengguna baru
     */
    public void setRole(String role) {
        this.role = role;
    }
}
