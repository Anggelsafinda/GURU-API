package com.example.GURU.API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GuruApiApplication {

	/**
	 * Proses utama (entry point) dari Spring Boot Application.
	 * Fungsi ini menginisialisasi aplikasi Spring dengan konteks dan bean.
	 * 
	 * @param args Argumen baris perintah (Optional)
	 */
	public static void main(String[] args) {
		SpringApplication.run(GuruApiApplication.class, args);
	}

}
