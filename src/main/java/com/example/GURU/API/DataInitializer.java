package com.example.GURU.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.GURU.API.model.Teacher;
import com.example.GURU.API.model.User;
import com.example.GURU.API.repository.TeacherRepository;
import com.example.GURU.API.repository.UserRepository;

/**
 * Komponen inisialisasi data aplikasi (Data Seeding).
 * 
 * Class ini mengimplementasikan interface CommandLineRunner dari Spring Boot.
 * Fungsi run() akan otomatis dipanggil SEKALI saja setelah aplikasi Spring Boot 
 * selesai startup dan context sudah fully initialized.
 * 
 * Tujuan Inisialisasi:
 * 1. Membuat akun admin default dengan username "admin" dan password "admin123"
 *    (agar developer/operator bisa login sejak pertama kali)
 * 2. Meng-inject (seed) data guru (teachers) contoh ke database
 *    (agar ada data yang bisa digunakan untuk testing endpoint)
 * 
 * Catatan Penting:
 * - Inisialisasi hanya terjadi sekali pada saat startup
 * - Menggunakan idempotent check (by NIP) untuk mencegah duplikasi saat restart
 * - Perubahan data manual (via API) tidak akan dihapus oleh DataInitializer di startup berikutnya
 * - Password admin di-hash menggunakan BCrypt untuk keamanan
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws java.lang.Exception {
        /**
         * Alur Inisialisasi Data:
         * 1. Cek apakah user admin "admin" sudah ada
         * 2. Jika ada, update password dengan BCrypt hash (untuk consistency)
         * 3. Jika tidak ada, buat user admin baru dengan password default "admin123"
         * 4. Inisialisasi data guru (teachers) contoh dengan idempotent check (by NIP)
         * 5. Tampilkan statistik ke console
         */
        
        // ==================== INISIALISASI USER ADMIN ====================
        // Cari user admin di database
        var existingAdmin = userRepository.findByUsername("admin");
        User admin;
        
        if (existingAdmin.isPresent()) {
            // Jika user admin sudah ada, update password dengan BCrypt encoding terbaru
            admin = existingAdmin.get();
            admin.setPassword(passwordEncoder.encode("admin123"));
            userRepository.save(admin);
            System.out.println("✓ Password admin berhasil diupdate dengan BCrypt encoding.");
        } else {
            // Jika user admin belum ada, buat user admin baru
            // Password WAJIB di-hash dengan BCrypt sebelum disimpan ke database
            admin = new User(
                    "admin", 
                    passwordEncoder.encode("admin123"),
                    "ROLE_ADMIN"
            );
            userRepository.save(admin);
            System.out.println("✓ User admin berhasil dibuat dengan password BCrypt.");
        }
        
        // Tampilkan informasi login default ke console
        System.out.println("✓ Username: admin | Password: admin123");
        
        // ==================== INISIALISASI DATA GURU ====================
        // Array data guru contoh yang akan diinisialisasi
        // Format: NIP, Nama Guru, Mata Pelajaran, Status (Aktif), Admin Pembuat
        Teacher[] teachersToInsert = {
            new Teacher("198001012005011001", "Budi Santoso", "Matematika", true, admin),
            new Teacher("198202022006022002", "Siti Aminah", "Bahasa Indonesia", true, admin),
            new Teacher("198503032010031003", "Andi Hermawan", "Fisika", false, admin),
            new Teacher("199004042015042004", "Rina Marlina", "Kimia", true, admin),
            new Teacher("201001012010011001", "Guru Matematika", "Matematika", true, admin),
            new Teacher("201102022011022002", "Guru Bahasa Indonesia", "Bahasa Indonesia", true, admin),
            new Teacher("201203032012033003", "Guru Fisika", "Fisika", true, admin),
            new Teacher("201304042013044004", "Guru Kimia", "Kimia", true, admin)
        };
        
        // Iterasi setiap guru dalam array untuk save ke database
        int insertedCount = 0;
        for (Teacher teacher : teachersToInsert) {
            // Cek apakah guru dengan NIP yang sama sudah ada di database (idempotent check)
            // Ini untuk mencegah duplikasi data ketika aplikasi di-restart
            if (teacherRepository.findByNip(teacher.getNip()).isEmpty()) {
                teacherRepository.save(teacher);
                insertedCount++;
            }
        }
        
        // Tampilkan statistik inisialisasi guru ke console
        if (insertedCount > 0) {
            System.out.println("✓ Data guru (teachers) berhasil diinisialisasi: " + insertedCount + " guru ditambahkan.");
        } else {
            System.out.println("✓ Data guru sudah ada di database.");
        }
    }
}
