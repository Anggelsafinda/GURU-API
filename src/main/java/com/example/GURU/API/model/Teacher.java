package com.example.GURU.API.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

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

    public Teacher() {
    }

    public Teacher(String nip, String name, String subject, boolean isActive, User admin) {
        this.nip = nip;
        this.name = name;
        this.subject = subject;
        this.isActive = isActive;
        this.admin = admin;
    }

    // Getter dan Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }
}
