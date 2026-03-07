package com.example.GURU.API.repository;

import com.example.GURU.API.model.Teacher;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface untuk entity Teacher.
 * Sengaja menggunakan `Repository` bukan `JpaRepository` agar fungsi bawaan 
 * delete() dari Spring Data JPA tidak terpanggil/tersedia, sesuai spesifikasi
 * "Dilarang membuat fungsi atau endpoint Delete/Remove (baik di Controller maupun di Repository)".
 */
public interface TeacherRepository extends Repository<Teacher, Long> {

    /**
     * Menyimpan atau meng-update data guru ke database.
     * 
     * @param teacher Data guru yang akan disave/update
     * @return Data guru yang berhasil disimpan
     */
    Teacher save(Teacher teacher);

    /**
     * Mencari data guru berdasarkan ID.
     * 
     * @param id ID dari guru
     * @return Data guru jika ditemukan
     */
    Optional<Teacher> findById(Long id);

    /**
     * Mengambil semua daftar guru yang ada di database.
     * 
     * @return Daftar / list seluruh data guru
     */
    List<Teacher> findAll();
}
