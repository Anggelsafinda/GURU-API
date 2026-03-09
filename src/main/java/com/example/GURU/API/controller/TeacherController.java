package com.example.GURU.API.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.GURU.API.dto.ApiResponse;
import com.example.GURU.API.model.Teacher;
import com.example.GURU.API.service.TeacherService;

/**
 * REST Controller untuk Data Guru.
 * Semua endpoint pada class ini diproteksi oleh Spring Security berjenis
 * Basic-Auth.
 * Path Base API-nya adalah "/api/teachers"
 */
@CrossOrigin(
    origins = {"http://localhost:8000", "http://localhost:5173", "http://localhost:8082", "http://192.168.18.25:8082", "http://192.168.18.10:8082"},
    allowedHeaders = {"*"},
    methods = {org.springframework.web.bind.annotation.RequestMethod.GET,
               org.springframework.web.bind.annotation.RequestMethod.POST,
               org.springframework.web.bind.annotation.RequestMethod.PUT,
               org.springframework.web.bind.annotation.RequestMethod.OPTIONS},
    allowCredentials = "true",
    maxAge = 3600
)
@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    /**
     * ENDPOINT - GET /api/teachers
     * Berfungsi untuk (READ) menampilkan seluruh daftar guru di sistem.
     * Mengembalikan semua guru baik yang aktif maupun yang sudah di-nonaktifkan.
     *
     * @return ResponseEntity berisi ApiResponse dengan list seluruh data guru
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Teacher>>> getAllTeachers() {
        try {
            // Mengambil semua data guru dari service
            List<Teacher> teachers = teacherService.getAllTeachers();
            
            // Membuat response sukses dengan HTTP 200 (OK)
            ApiResponse<List<Teacher>> response = new ApiResponse<>(
                    true,
                    "Data guru berhasil didapatkan",
                    teachers);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // Menangkap error sistem yang tidak terduga
            ApiResponse<List<Teacher>> response = new ApiResponse<>(
                    false,
                    "Gagal mengambil data guru: " + e.getMessage(),
                    null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * ENDPOINT - GET /api/teachers/{id}
     * Berfungsi untuk (READ) menampilkan data guru khusus berdasarkan ID tertentu.
     * 
     * @param id ID guru yang akan diambil datanya
     * @return ResponseEntity berisi ApiResponse dengan data guru jika ditemukan,
     *         atau error 404 jika guru tidak ditemukan
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Teacher>> getTeacherById(@PathVariable Long id) {
        try {
            // Mencari guru berdasarkan ID menggunakan service
            return teacherService.getTeacherById(id)
                    // Jika guru ditemukan (Optional tidak kosong)
                    .map(teacher -> {
                        ApiResponse<Teacher> response = new ApiResponse<>(
                                true,
                                "Data guru berhasil didapatkan",
                                teacher);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    })
                    // Jika guru tidak ditemukan (Optional kosong)
                    .orElse(new ResponseEntity<>(
                            new ApiResponse<>(false, "Guru dengan ID " + id + " tidak ditemukan", null),
                            HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            // Menangkap error sistem yang tidak terduga
            ApiResponse<Teacher> response = new ApiResponse<>(
                    false,
                    "Gagal mengambil data guru: " + e.getMessage(),
                    null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * ENDPOINT - POST /api/teachers
     * Berfungsi untuk (CREATE) menambahkan data guru baru (default status = Aktif).
     * Validasi: NIP, Name, dan Subject harus diisi.
     */
    /**
     * ENDPOINT - POST /api/teachers
     * Berfungsi untuk (CREATE) menambahkan data guru baru (default status = Aktif).
     * 
     * Validasi input:
     * - NIP (Nomor Induk Pegawai) harus diisi
     * - Nama guru harus diisi
     * - Mata pelajaran harus diisi
     * 
     * @param teacher Data guru yang akan ditambahkan (dari request body)
     * @param userDetails Informasi user yang login (diambil dari Spring Security)
     * @return ResponseEntity berisi ApiResponse dengan status dan data guru yang dibuat
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Teacher>> createTeacher(@RequestBody Teacher teacher,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // ==================== VALIDASI INPUT ====================
            // Memastikan NIP tidak kosong atau hanya whitespace
            if (teacher.getNip() == null || teacher.getNip().trim().isEmpty()) {
                ApiResponse<Teacher> response = new ApiResponse<>(
                        false,
                        "NIP guru tidak boleh kosong",
                        null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            
            // Memastikan nama guru tidak kosong atau hanya whitespace
            if (teacher.getName() == null || teacher.getName().trim().isEmpty()) {
                ApiResponse<Teacher> response = new ApiResponse<>(
                        false,
                        "Nama guru tidak boleh kosong",
                        null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            
            // Memastikan mata pelajaran tidak kosong atau hanya whitespace
            if (teacher.getSubject() == null || teacher.getSubject().trim().isEmpty()) {
                ApiResponse<Teacher> response = new ApiResponse<>(
                        false,
                        "Mata pelajaran tidak boleh kosong",
                        null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Menyimpan guru baru dengan service layer
            Teacher newTeacher = teacherService.createTeacher(teacher, userDetails.getUsername());
            
            // Return response sukses dengan HTTP 201 (CREATED)
            ApiResponse<Teacher> response = new ApiResponse<>(
                    true,
                    "Data guru berhasil ditambahkan",
                    newTeacher);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            ApiResponse<Teacher> response = new ApiResponse<>(
                    false,
                    "Gagal menambahkan data guru: " + e.getMessage(),
                    null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ApiResponse<Teacher> response = new ApiResponse<>(
                    false,
                    "Error pada server: " + e.getMessage(),
                    null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * ENDPOINT - PUT /api/teachers/{id}
     * Berfungsi untuk (UPDATE) mengubah detail guru.
     * 
     * Termasuk memanipulasi status "isActive" (True / False) untuk
     * mengaktifkan/nonaktifkan guru (sebagai pengganti Delete = Soft-Delete).
     * 
     * Validasi:
     * - ID harus valid dan guru harus ada di database
     * - Field NIP, Nama, dan Mata Pelajaran harus terisi
     * 
     * @param id ID guru yang akan diupdate
     * @param teacherDetails Data guru baru (yang akan menimpa data lama)
     * @return ResponseEntity berisi ApiResponse dengan data guru yang sudah diupdate
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Teacher>> updateTeacher(@PathVariable Long id, @RequestBody Teacher teacherDetails) {
        try {
            // ==================== VALIDASI INPUT ====================
            // Memastikan NIP tidak kosong
            if (teacherDetails.getNip() == null || teacherDetails.getNip().trim().isEmpty()) {
                ApiResponse<Teacher> response = new ApiResponse<>(
                        false,
                        "NIP guru tidak boleh kosong",
                        null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            
            // Memastikan nama guru tidak kosong
            if (teacherDetails.getName() == null || teacherDetails.getName().trim().isEmpty()) {
                ApiResponse<Teacher> response = new ApiResponse<>(
                        false,
                        "Nama guru tidak boleh kosong",
                        null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            
            // Memastikan mata pelajaran tidak kosong
            if (teacherDetails.getSubject() == null || teacherDetails.getSubject().trim().isEmpty()) {
                ApiResponse<Teacher> response = new ApiResponse<>(
                        false,
                        "Mata pelajaran tidak boleh kosong",
                        null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Melakukan update data guru melalui service layer
            Teacher updatedTeacher = teacherService.updateTeacher(id, teacherDetails);
            
            // Return response sukses dengan HTTP 200 (OK)
            ApiResponse<Teacher> response = new ApiResponse<>(
                    true,
                    "Data guru berhasil diperbarui",
                    updatedTeacher);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            ApiResponse<Teacher> response = new ApiResponse<>(
                    false,
                    e.getMessage(),
                    null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ApiResponse<Teacher> response = new ApiResponse<>(
                    false,
                    "Error pada server: " + e.getMessage(),
                    null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ============================================
    // NO DELETE ENDPOINT ALLOWED PER SPECIFICATION
    // ============================================
    // Hal ini sesuai dengan studi kasus teknis bahwa
    // Penghapusan data benar-benar dilarang untuk menjaga jejak digital.
}
