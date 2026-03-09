package com.example.GURU.API.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entity Class untuk menyimpan informasi data Guru.
 * Berisi NIP, Nama, Mata Pelajaran, dan Status (Aktif/Non-Aktif).
 */
@Entity
@Table(name = "teachers")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nip; // Nomor Induk Pegawai
    private String name; // Nama Guru
    private String subject; // Mata Pelajaran yang diampu

    // Status guru: true untuk "Aktif", false untuk "Non-Aktif"
    // Digunakan sebagai pengganti fungsi Delete (Soft-Delete)
    private boolean isActive = true;

    // Relasi saling terhubung antara Guru dan Admin (User)
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;

    /**
     * Constructor kosong (No-argument constructor) untuk entity Teacher.
     * Diperlukan oleh Hibernate/JPA untuk membuat instance dari hasil query database.
     */
    public Teacher() {
    }

    /**
     * Constructor untuk membuat instance Teacher dengan semua parameter.
     * Digunakan saat membuat data guru baru dengan nilai lengkap.
     * 
     * @param nip Nomor Induk Pegawai (Unique identifier guru)
     * @param name Nama lengkap guru
     * @param subject Mata pelajaran yang akan diampu guru
     * @param isActive Status guru: true = Aktif, false = Non-Aktif (Soft-Delete)
     * @param admin User admin yang membuat/mengelola guru ini
     */
    public Teacher(String nip, String name, String subject, boolean isActive, User admin) {
        this.nip = nip;
        this.name = name;
        this.subject = subject;
        this.isActive = isActive;
        this.admin = admin;
    }

    // ======================== Getter dan Setter ========================

    /**
     * Mendapatkan ID unik guru di database.
     * @return ID guru
     */
    public Long getId() {
        return id;
    }

    /**
     * Mengatur ID unik guru (biasanya auto-generated).
     * @param id ID guru baru
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Mendapatkan Nomor Induk Pegawai (NIP) guru.
     * @return NIP guru
     */
    public String getNip() {
        return nip;
    }

    /**
     * Mengatur Nomor Induk Pegawai (NIP) guru.
     * @param nip NIP baru guru
     */
    public void setNip(String nip) {
        this.nip = nip;
    }

    /**
     * Mendapatkan nama lengkap guru.
     * @return Nama guru
     */
    public String getName() {
        return name;
    }

    /**
     * Mengatur nama lengkap guru.
     * @param name Nama guru baru
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Mendapatkan mata pelajaran yang diampu guru.
     * @return Nama mata pelajaran
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Mengatur mata pelajaran yang diampu guru.
     * @param subject Mata pelajaran baru
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Mengecek status guru apakah aktif atau non-aktif.
     * @return true jika guru aktif, false jika non-aktif
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Mengatur status aktif/non-aktif guru (soft-delete).
     * @param active true untuk aktif, false untuk non-aktif
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Mendapatkan admin yang membuat/mengelola guru ini.
     * @return User admin yang terkait
     */
    public User getAdmin() {
        return admin;
    }

    /**
     * Mengatur admin yang membuat/mengelola guru ini.
     * @param admin User admin baru
     */
    public void setAdmin(User admin) {
        this.admin = admin;
    }
}
