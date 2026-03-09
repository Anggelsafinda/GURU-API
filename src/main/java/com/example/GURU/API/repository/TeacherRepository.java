package com.example.GURU.API.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.example.GURU.API.model.Teacher;

/**
 * Repository interface untuk entity Teacher (Data Guru).
 * 
 * Digunakan untuk melakukan operasi database (CRUD) terhadap tabel "teachers".
 * 
 * PENTING - Desain Khusus:
 * Mengextend interface "Repository" (bukan JpaRepository), dengan alasan:
 * - Spesifikasi project melarang "Delete/Remove" operation pada endpoint maupun repository
 * - Dengan mengextend Repository biasa, method delete() dari Spring Data tidak tersedia
 * - Method yang ekplisit di-define di sini adalah: save, findById, findAll, findByNip
 * - Ini adalah soft-delete design pattern: data tidak dihapus, hanya di-nonaktifkan (isActive=false)
 * 
 * Methods custom yang didefinisikan akan otomatis di-generate oleh Spring Data JPA:
 * - findByNip(...) → SELECT * FROM teachers WHERE nip = ?
 * 
 * Perbandingan:
 * - Repository: Hanya methods yang explicit di interface saja yang tersedia (lebih aman)
 * - JpaRepository: Menyediakan banyak methods CRUD termasuk delete (lebih fleksibel tapi kurang aman)
 */
public interface TeacherRepository extends Repository<Teacher, Long> {

    /**
     * Menyimpan atau meng-update data guru ke database.
     * 
     * Operasi:
     * - Jika guru belum ada (ID null), data akan di-INSERT
     * - Jika guru sudah ada (ID ada), data akan di-UPDATE
     * 
     * @param teacher Data guru yang akan disave/update (object Teacher)
     * @return Data guru yang berhasil disimpan dengan ID yang sudah ter-generate (jika insert)
     */
    Teacher save(Teacher teacher);

    /**
     * Mencari data guru berdasarkan ID unik (Primary Key).
     * 
     * @param id ID dari guru (Long)
     * @return Optional yang berisi Teacher jika ditemukan, atau kosong jika tidak ada
     */
    Optional<Teacher> findById(Long id);

    /**
     * Mengambil SEMUA daftar guru yang ada di database.
     * Ini akan mengembalikan semua guru baik yang aktif (isActive=true) 
     * maupun yang non-aktif (isActive=false).
     * 
     * Query yang di-generate: SELECT * FROM teachers
     * 
     * @return List yang berisi semua data guru (bisa kosong jika tidak ada guru)
     */
    List<Teacher> findAll();

    /**
     * Mencari data guru berdasarkan NIP (Nomor Induk Pegawai).
     * NIP adalah identifier unik untuk setiap guru.
     * 
     * Digunakan di:
     * - DataInitializer: untuk idempotent check saat seed data
     * - Controller Validation: untuk memastikan NIP unik sebelum buat/update guru
     * 
     * Query yang di-generate: SELECT * FROM teachers WHERE nip = ?
     * 
     * @param nip NIP dari guru (String)
     * @return Optional yang berisi Teacher jika ditemukan, atau kosong jika tidak ada
     */
    Optional<Teacher> findByNip(String nip);
}
