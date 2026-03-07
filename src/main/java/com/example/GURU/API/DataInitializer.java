package com.example.GURU.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.GURU.API.model.User;
import com.example.GURU.API.repository.UserRepository;

/**
 * Komponen inisialisasi data. Dijalankan sekali ketika aplikasi Spring Boot selesai berjalan.
 * Digunakan untuk menyuntikkan (seeding) satu akun user (Admin) yang bisa digunakan untuk login.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws java.lang.Exception {
        // Cari user admin
        var existingAdmin = userRepository.findByUsername("admin");
        
        if (existingAdmin.isPresent()) {
            // Jika ada, update password dengan BCrypt
            User admin = existingAdmin.get();
            admin.setPassword(passwordEncoder.encode("admin123"));
            userRepository.save(admin);
            System.out.println("✓ Password admin berhasil diupdate dengan BCrypt encoding.");
        } else {
            // Jika tidak ada, buat user baru
            User admin = new User(
                    "admin", 
                    passwordEncoder.encode("admin123"),
                    "ROLE_ADMIN"
            );
            userRepository.save(admin);
            System.out.println("✓ User admin berhasil dibuat dengan password BCrypt.");
        }
        
        System.out.println("✓ Username: admin | Password: admin123");
    }
}
