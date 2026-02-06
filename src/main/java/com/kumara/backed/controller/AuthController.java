package com.kumara.backed.controller;

import com.kumara.backed.dto.*;
import com.kumara.backed.model.User;
import com.kumara.backed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.kumara.backed.security.JwtUtils;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;

    // Untuk hashing password agar tidak tersimpan plain text di database (Security Standard)
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // REGISTER ENDPOINT
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        // 1. Validasi Email
        if (userRepository.existsByEmail(request.email())) {
            return ResponseEntity.badRequest().body("Email sudah terdaftar!");
        }

        // 2. Buat User Baru
        User newUser = new User();
        newUser.setFullName(request.fullName());
        newUser.setEmail(request.email());
        newUser.setBusinessName(request.businessName());
        
        // 3. Hash Password (Aman)
        String hashedPassword = passwordEncoder.encode(request.password());
        newUser.setPasswordHash(hashedPassword);

        // 4. Simpan ke Database
        User savedUser = userRepository.save(newUser);
        String token = jwtUtils.generateToken(newUser.getEmail());

        return ResponseEntity.ok(new AuthResponse(
            savedUser.getId(),
            savedUser.getFullName(),
            savedUser.getEmail(),
            savedUser.getRole(),
            savedUser.getBusinessName(),
            token // Nanti diganti JWT
        ));
    }

    // LOGIN ENDPOINT
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        // 1. Cari User by Email
        Optional<User> userOpt = userRepository.findByEmail(request.email());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String token = jwtUtils.generateToken(user.getEmail());
            // 2. Cek Password (Raw vs Hash)
            if (passwordEncoder.matches(request.password(), user.getPasswordHash())) {
                // Login Sukses
                return ResponseEntity.ok(new AuthResponse(
                    user.getId(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getRole(),
                    user.getBusinessName(),
                    token
                ));
            }
        }

        // Login Gagal
        return ResponseEntity.status(401).body("Email atau Password salah!");
    }
}