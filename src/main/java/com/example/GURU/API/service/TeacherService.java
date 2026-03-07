package com.example.GURU.API.service;

import com.example.GURU.API.model.Teacher;
import com.example.GURU.API.model.User;
import com.example.GURU.API.repository.TeacherRepository;
import com.example.GURU.API.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service Layer untuk memisahkan business logic dengan controller.
 * Mengelola logic Create, Read, Update data guru (Tidak ada Delete).
 */
@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * (READ) Mengambil semua data guru di sistem.
     * 
     * @return List seluruh guru (baik aktif maupun non-aktif)
     */
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    /**
     * (READ) Mengambil 1 data guru berdasarkan ID.
     * 
     * @param id ID guru
     * @return Data guru, bisa kosong (Optional) jika tidak ketemu
     */
    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    /**
     * (CREATE) Menambahkan data guru baru ke database.
     * 
     * @param teacher  Entity data guru yang dikirim dari controller
     * @param username Username admin yang meng-create data ini
     * @return Data guru setelah disimpan
     */
    public Teacher createTeacher(Teacher teacher, String username) {
        // Cari user admin yang sedang login
        User admin = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Admin tidak ditemukan: " + username));

        teacher.setAdmin(admin); // Mengaitkan guru dengan admin yang membuatnya
        // Secara default user baru diciptakan dalam keadaan aktif (True)
        teacher.setActive(true);
        return teacherRepository.save(teacher);
    }

    /**
     * (UPDATE) Memperbarui data guru berdasarkan ID, termasuk fitur
     * pengubahan status (Aktif menjadi Non-Aktif / sebaliknya).
     * 
     * @param id             ID guru yang ingin diubah
     * @param teacherDetails data perubahan (NIP, Name, Subject, isActive)
     * @return Data guru yang sudah ter-update
     */
    public Teacher updateTeacher(Long id, Teacher teacherDetails) {
        Teacher existingTeacher = teacherRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Guru dengan ID " + id + " tidak ditemukan."));

        // Timpa nilai data lama dengan nilai yang baru
        existingTeacher.setNip(teacherDetails.getNip());
        existingTeacher.setName(teacherDetails.getName());
        existingTeacher.setSubject(teacherDetails.getSubject());

        // Pembaruan status: sebagai *soft-delete* (Non-Aktifkan/Aktifkan)
        existingTeacher.setActive(teacherDetails.isActive());

        return teacherRepository.save(existingTeacher);
    }

    // Catatan Spesifikasi: "Dilarang membuat fungsi atau endpoint
    // Delete/Remove (baik di Controller maupun di Repository)"
    // => OLEH KARENA ITU, TIDAK ADA FUNGSI `deleteTeacher()` di sini.
}
