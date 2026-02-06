package com.kumara.backed.controller;

import com.kumara.backed.dto.UserUpdateRequest;
import com.kumara.backed.model.User;
import com.kumara.backed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.io.IOException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired private UserRepository userRepository;

    // Ambil Data Profil
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    // Update Profil
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        return userRepository.findById(id).map(user -> {
            user.setFullName(request.fullName());
            // user.setAvatarUrl(request.avatarUrl()); // Jika ada kolom avatar di tabel users
            userRepository.save(user);
            return ResponseEntity.ok("Profil berhasil diupdate");
        }).orElse(ResponseEntity.notFound().build());
    }

    // Upload Foto Profil
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestParam("fullName") String fullName,
            @RequestParam("businessName") String businessName,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        return userRepository.findById(id).map(user -> {
            user.setFullName(fullName);
            user.setBusinessName(businessName);
            // Logika Simpan Gambar
            if (file != null && !file.isEmpty()) {
                try {
                    // Nama file: userId_namaasli.jpg
                    String fileName = id + "_" + file.getOriginalFilename();
                    
                    // Simpan ke folder 'user-photos'
                    Path uploadPath = Paths.get("./user-photos");
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }
                    
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    
                    // Simpan URL aksesnya ke database
                    // Contoh akses nanti: http://localhost:8080/photos/1_avatar.jpg
                    user.setAvatarUrl(fileName);
                    
                } catch (IOException e) {
                    return (ResponseEntity<?>) ResponseEntity.internalServerError().body("Gagal upload gambar");
                }
            }

            User updatedUser = userRepository.save(user);
            return (ResponseEntity<?>) ResponseEntity.ok(updatedUser); // Kembalikan data user lengkap biar frontend bisa update
        }).orElse(ResponseEntity.notFound().build());
    }
}