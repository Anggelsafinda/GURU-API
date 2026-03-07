package com.example.GURU.API.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.GURU.API.model.User;

/**
 * Repository interface untuk entity User.
 * Digunakan untuk melakukan operasi database terhadap tabel users.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Mencari pengguna (admin) berdasarkan username.
     * Fungsi ini akan dipanggil oleh Spring Security untuk proses otentikasi login.
     * 
     * @param username Nama user yang login
     * @return Optional berisi User jika ditemukan
     */
    Optional<User> findByUsername(String username);

    /**
     * Menghapus pengguna berdasarkan username.
     * Digunakan oleh DataInitializer untuk menghapus user lama sebelum membuat yang
     * baru.
     * 
     * @param username Username yang akan dihapus
     */
    void deleteByUsername(String username);
}
