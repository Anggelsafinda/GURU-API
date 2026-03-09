package com.example.GURU.API.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.GURU.API.service.CustomUserDetailsService;

/**
 * Konfigurasi Spring Security menggunakan Basic Authentication (HTTP Basic Auth).
 * 
 * Fitur-fitur:
 * - Authentication: Basic Auth (username + password)
 * - Authorization: Role-based access control
 * - CORS: Konfigurasi global untuk cross-origin requests
 * - CSRF: Dinonaktifkan untuk REST API
 * - Password Encoding: BCrypt untuk hashing password
 * 
 * Alur Otentikasi:
 * 1. Client mengirim request dengan header Authorization: Basic <base64(username:password)>
 * 2. Spring Security decode header tersebut
 * 3. DaoAuthenticationProvider validasi username dan password
 * 4. CustomUserDetailsService mencari user di database
 * 5. PasswordEncoder membandingkan password input dengan password hash di database
 * 6. Jika valid, user di-authenticate
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Membaca daftar CORS allowed origins dari application.properties
     * Property: cors.allowed.origins
     * Format: comma-separated list (misal: http://localhost:5173,http://192.168.1.10:3000)
     * Fallback: http://localhost:5173,http://localhost:8000,http://localhost:8082 (jika tidak dikonfigurasi)
     */
    @Value("${cors.allowed.origins:http://localhost:5173,http://localhost:8000,http://localhost:8082}")
    private String allowedOrigins;

    /**
     * Menyediakan PasswordEncoder menggunakan algoritma BCrypt.
     * BCrypt adalah algoritma hashing yang:
     * - Irreversible (tidak bisa di-decrypt)
     * - Menggunakan salt (random) untuk setiap password
     * - Adaptive (bisa disesuaikan tingkat kesulitannya)
     * 
     * Fungsi ini digunakan untuk:
     * 1. Hashing password saat user baru dibuat
     * 2. Membandingkan password input dengan password hash di database saat login
     * 
     * @return BCryptPasswordEncoder bean instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Konfigurasi CORS (Cross-Origin Resource Sharing) secara global.
     * CORS memungkinkan aplikasi frontend yang berjalan di origin berbeda untuk mengakses API ini.
     * 
     * Konfigurasi:
     * - Allowed Origins: Dibaca dari property `cors.allowed.origins` di application.properties
     *   Dapat di-override menggunakan environment variable: CORS_ALLOWED_ORIGINS
     *   Format: comma-separated list tanpa spasi (misal: http://localhost:5173,http://192.168.1.10:3000)
     *   Default: http://localhost:5173,http://localhost:8000,http://localhost:8082
     * - Allowed Methods: GET, POST, PUT, OPTIONS (DELETE dilarang per spesifikasi)
     * - Allowed Headers: Semua headers (*) diizinkan
     * - Credentials: true (cookie/auth header dipertahankan)
     * - Max Age: 3600 detik (cache preflight response selama 1 jam)
     * 
     * MUDAH untuk MULTI-DEVICE:
     * - Tidak perlu hardcode IP di code, cukup edit application.properties atau set environment variable
     * - Supaya device A bisa akses: tambah IP device A ke cors.allowed.origins
     * - Contoh: cors.allowed.origins=http://localhost:5173,http://192.168.18.25:3000,http://192.168.18.10:5173
     * 
     * @return CorsConfigurationSource dengan konfigurasi berlaku ke semua rute (**)
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Parse comma-separated origins dari property file
        configuration.setAllowedOrigins(java.util.Arrays.asList(
            allowedOrigins.split(",")
        ));
        
        configuration.setAllowedMethods(java.util.Arrays.asList(
            "GET", "POST", "PUT", "OPTIONS"
        ));
        configuration.setAllowedHeaders(java.util.Arrays.asList(
            "*"
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Konfigurasi DaoAuthenticationProvider untuk autentikasi berbasis database.
     * Authentication Provider ini bertanggung jawab untuk:
     * 1. Load user details dari database melalui CustomUserDetailsService
     * 2. Membandingkan password input dengan password hash menggunakan PasswordEncoder
     * 3. Mengembalikan Authentication object jika valid, atau throw exception jika invalid
     * 
     * @return DaoAuthenticationProvider yang sudah dikonfigurasi
     */
    @Bean
    @SuppressWarnings("deprecation")
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Bean AuthenticationManager dari Spring Security configuration.
     * AuthenticationManager bertanggung jawab mengelola proses otentikasi keseluruhan.
     * 
     * Fungsi ini biasanya digunakan untuk:
     * - Custom authentication logic di controller/service
     * - Programmatic authentication (manual authentication dari code)
     * 
     * Akan menggunakan DaoAuthenticationProvider yang sudah dikonfigurasi di bean lain.
     * 
     * @param config AuthenticationConfiguration dari Spring Security
     * @return AuthenticationManager
     * @throws Exception Jika terjadi error saat membuat AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws java.lang.Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Konfigurasi Security Filter Chain untuk melindungi API dari akses tidak sah.
     * 
     * Filter Chain ini mendefinisikan aturan keamanan global untuk semua HTTP request:
     * 
     * 1. CORS Configuration:
     *    - Mengaktifkan CORS dengan corsConfigurationSource() yang sudah dikonfigurasi
     *    - Memungkinkan request from allowed origins
     * 
     * 2. CSRF Protection:
     *    - CSRF (Cross-Site Request Forgery) dinonaktifkan untuk REST API
     *    - CSRF umumnya hanya perlu untuk aplikasi web tradisional dengan session
     * 
     * 3. Authorization Rules - Request Matching:
     *    - /api/teachers/**: Semua endpoint di path ini HARUS sudah authenticated (login)
     *    - /: Endpoint root dan endpoint lain diizinkan untuk public access (tanpa login)
     * 
     * 4. Authentication Method:
     *    - HTTP Basic Authentication diaktifkan
     *    - Client mengirim header: Authorization: Basic <base64(username:password)>
     * 
     * 5. Authentication Provider:
     *    - Menggunakan DaoAuthenticationProvider yang sudah dikonfigurasi
     * 
     * Flow Request:
     * 1. Request masuk ke system
     * 2. CORS filter mengecek origin (jika preflight request)
     * 3. Authorization filter mengecek path apakah memerlukan authentication
     * 4. Jika memerlukan authentication, Spring Security meminta credentials
     * 5. Basic Auth decoder mengekstrak username dan password dari Authorization header
     * 6. DaoAuthenticationProvider validasi credentials
     * 7. Jika valid, request dilanjutkan; jika tidak, return 401 Unauthorized
     * 
     * @param http HttpSecurity object untuk konfigurasi filter chain
     * @return SecurityFilterChain yang sudah dikonfigurasi
     * @throws Exception Jika terjadi error pada saat konfigurasi
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Aktifkan CORS dengan global configuration
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // Nonaktifkan CSRF untuk REST API
            .csrf(AbstractHttpConfigurer::disable)
            // Konfigurasi otorisasi/autentikasi
            .authorizeHttpRequests(auth -> auth
                // Semua request ke /api/teachers/** harus sudah login (authenticated)
                .requestMatchers("/api/teachers/**").authenticated()
                // Request lain diizinkan tanpa autentikasi
                .anyRequest().permitAll()
            )
            // Aktifkan HTTP Basic Authentication
            .httpBasic(Customizer.withDefaults())
            // Gunakan DaoAuthenticationProvider yang sudah dikonfigurasi
            .authenticationProvider(authenticationProvider());

        return http.build();
    }
}
