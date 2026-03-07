package com.example.GURU.API.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.GURU.API.service.CustomUserDetailsService;

/**
 * Konfigurasi proteksi Spring Security menggunakan Basic Authentication.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Menyediakan PasswordEncoder (BCrypt) untuk membandingkan
     * password database (yang tersimpan di-hash) dengan input user.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Konfigurasi Authentication Provider memakai UserDetailsService dan PasswordEncoder.
     * Provider ini yang akan digunakan Spring Security untuk mengvalidasi username dan password.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * AuthenticationManager untuk menangani proses autentikasi.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws java.lang.Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Konfigurasi Security Filter Chain.
     * Semua rute ke /api/teachers/** akan diproteksi memerlukan login (Basic Auth).
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws java.lang.Exception {
        http
            .csrf(csrf -> csrf.disable()) // Nonaktifkan CSRF untuk kemudahan REST API
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/teachers/**").authenticated() // Proteksi endpoint guru
                .anyRequest().permitAll() // Sisanya bebas
            )
            .httpBasic(Customizer.withDefaults()) // Atur autentikasi dengan mode Basic Auth
            .authenticationProvider(authenticationProvider()); // Gunakan DaoAuthenticationProvider kita

        return http.build();
    }
}
