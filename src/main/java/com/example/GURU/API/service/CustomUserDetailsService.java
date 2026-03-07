package com.example.GURU.API.service;

import com.example.GURU.API.model.User;
import com.example.GURU.API.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service untuk memproses pengambilan data user untuk keperluan autentikasi Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Mengambil detail user berdasarkan username dari database.
     * Fungsi ini otomatis dipakai oleh Spring Security pada saat login (Basic Auth).
     *
     * @param username username dari form/request login
     * @return UserDetails object yang mengimplementasikan data login
     * @throws UsernameNotFoundException Error jika username tidak ditemukan
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User tidak ditemukan: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword()) // Password wajib sudah di-hash
                .roles(user.getRole().replace("ROLE_", "")) // Role (Spring Security menambahkan prefix ROLE_ secara otomatis)
                .build();
    }
}
