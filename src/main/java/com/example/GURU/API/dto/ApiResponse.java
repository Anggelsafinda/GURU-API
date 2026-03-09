package com.example.GURU.API.dto;

/**
 * Generic DTO untuk standardisasi response API.
 * Mengikuti standar industri dengan format:
 * {
 *   "status": true/false,
 *   "message": "deskripsi pesan",
 *   "data": {...}
 * }
 */
public class ApiResponse<T> {
    private boolean status;
    private String message;
    private T data;

    // Constructors

    /**
     * Constructor kosong (No-argument constructor) untuk ApiResponse.
     * Digunakan untuk membuat instance ApiResponse yang kosong, 
     * kemudian di-populate melalui setter methods.
     */
    public ApiResponse() {
    }

    /**
     * Constructor lengkap untuk membuat ApiResponse dengan semua field.
     * Ini adalah constructor yang paling sering digunakan untuk mengembalikan response.
     * 
     * @param status Status response (true = sukses, false = gagal)
     * @param message Pesan deskriptif untuk client (contoh: "Data berhasil didapatkan")
     * @param data Payload data yang akan dikirim ke client (bisa null untuk error response)
     */
    public ApiResponse(boolean status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    /**
     * Constructor untuk response tanpa data payload.
     * Biasanya digunakan untuk response yang hanya berisi status dan pesan (tanpa data).
     * 
     * @param status Status response (true = sukses, false = gagal)
     * @param message Pesan deskriptif untuk client
     */
    public ApiResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }

    // ======================== Getter & Setter ========================

    /**
     * Mengecek status response (success atau failure).
     * @return true jika request berhasil, false jika gagal
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * Mengatur status response (success atau failure).
     * @param status true untuk sukses, false untuk gagal
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * Mendapatkan pesan deskriptif response (contoh: "Data berhasil didapatkan").
     * @return Pesan response
     */
    public String getMessage() {
        return message;
    }

    /**
     * Mengatur pesan deskriptif response.
     * @param message Pesan baru untuk response
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Mendapatkan data payload response (bisa List, Object, atau yang lain).
     * Generic type T memungkinkan fleksibilitas tipe data.
     * @return Data response
     */
    public T getData() {
        return data;
    }

    /**
     * Mengatur data payload response.
     * @param data Data baru untuk response
     */
    public void setData(T data) {
        this.data = data;
    }
}
