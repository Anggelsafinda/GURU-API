package com.example.GURU.API.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.GURU.API.model.Teacher;
import com.example.GURU.API.service.TeacherService;

/**
 * REST Controller untuk Data Guru.
 * Semua endpoint pada class ini diproteksi oleh Spring Security berjenis
 * Basic-Auth.
 * Path Base API-nya adalah "/api/teachers"
 */
@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    /**
     * ENDPOINT - GET /api/teachers
     * Berfungsi untuk (READ) menampilkan seluruh daftar guru.
     */
    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();
        return new ResponseEntity<>(teachers, HttpStatus.OK);
    }

    /**
     * ENDPOINT - GET /api/teachers/{id}
     * Berfungsi untuk (READ) menampilkan data guru khusus pada ID tertentu.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {
        return teacherService.getTeacherById(id)
                .map(teacher -> new ResponseEntity<>(teacher, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * ENDPOINT - POST /api/teachers
     * Berfungsi untuk (CREATE) menambahkan data guru baru (default status = Aktif).
     */
    @PostMapping
    public ResponseEntity<Teacher> createTeacher(@RequestBody Teacher teacher,
            @AuthenticationPrincipal UserDetails userDetails) {
        Teacher newTeacher = teacherService.createTeacher(teacher, userDetails.getUsername());
        return new ResponseEntity<>(newTeacher, HttpStatus.CREATED); // 201 Created
    }

    /**
     * ENDPOINT - PUT /api/teachers/{id}
     * Berfungsi untuk (UPDATE) mengubah detail guru,
     * Termasuk memanipulasi status "isActive" (True / False) untuk
     * mengaktifkan/nonaktifkan guru (sebagai pengganti Delete).
     */
    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long id, @RequestBody Teacher teacherDetails) {
        try {
            Teacher updatedTeacher = teacherService.updateTeacher(id, teacherDetails);
            return new ResponseEntity<>(updatedTeacher, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Error jika ID tidak ditemukan
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ============================================
    // NO DELETE ENDPOINT ALLOWED PER SPECIFICATION
    // ============================================
    // Hal ini sesuai dengan studi kasus teknis bahwa
    // Penghapusan data benar-benar dilarang untuk menjaga jejak digital.
}
