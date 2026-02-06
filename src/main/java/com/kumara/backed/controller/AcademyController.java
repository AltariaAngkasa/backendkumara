package com.kumara.backed.controller;

import com.kumara.backed.dto.ModuleDTO;
import com.kumara.backed.model.Module;
import com.kumara.backed.model.ModuleCompletion;
import com.kumara.backed.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.kumara.backed.repository.ModuleCompletionRepository;
import com.kumara.backed.repository.ModuleRepository;
import com.kumara.backed.repository.UserRepository;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/academy")
public class AcademyController {

    @Autowired private ModuleRepository moduleRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ModuleCompletionRepository completionRepository;

    // 1. LIHAT MATERI (Semua orang boleh lihat)
    @GetMapping("/modules")
    public ResponseEntity<List<Module>> getModules() {
        return ResponseEntity.ok(moduleRepository.findAll());
    }

    // 2. TAMBAH MATERI (Hanya ADMIN)
    @PostMapping("/{userId}/add")
    public ResponseEntity<?> addModule(@PathVariable Long userId, @RequestBody Module module) {
        return checkAdmin(userId) ? 
            ResponseEntity.ok(moduleRepository.save(module)) : 
            ResponseEntity.status(403).body("Hanya Admin yang boleh menambah materi");
    }

    // 3. HAPUS MATERI (Hanya ADMIN)
    @DeleteMapping("/{userId}/{moduleId}")
    public ResponseEntity<?> deleteModule(@PathVariable Long userId, @PathVariable Long moduleId) {
        if (!checkAdmin(userId)) return ResponseEntity.status(403).body("Bukan Admin");
        
        moduleRepository.deleteById(moduleId);
        return ResponseEntity.ok("Materi dihapus");
    }

    // Helper: Cek apakah user adalah ADMIN
    private boolean checkAdmin(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
    }

    // 4. CEK STATUS (Apakah user ini sudah menyelesaikan materi ini?)
    @GetMapping("/{userId}/{moduleId}/status")
    public ResponseEntity<Boolean> checkStatus(@PathVariable Long userId, @PathVariable Long moduleId) {
        boolean exists = completionRepository.findByUserIdAndModuleId(userId, moduleId).isPresent();
        return ResponseEntity.ok(exists);
    }

    // 5. TANDAI SELESAI / BATAL SELESAI (Toggle)
    @PostMapping("/{userId}/{moduleId}/toggle")
    public ResponseEntity<?> toggleCompletion(@PathVariable Long userId, @PathVariable Long moduleId) {
        var existing = completionRepository.findByUserIdAndModuleId(userId, moduleId);
        
        if (existing.isPresent()) {
            // Kalau sudah ada, hapus (Batal Selesai) -> Balik jadi belum hijau
            completionRepository.delete(existing.get());
            return ResponseEntity.ok(false); // Return status baru: Belum Selesai
        } else {
            // Kalau belum ada, simpan (Tandai Selesai) -> Jadi hijau
            ModuleCompletion completion = new ModuleCompletion();
            completion.setUserId(userId);
            completion.setModuleId(moduleId);
            completionRepository.save(completion);
            return ResponseEntity.ok(true); // Return status baru: Selesai
        }
    }
}

