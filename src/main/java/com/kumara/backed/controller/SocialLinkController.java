package com.kumara.backed.controller;

import com.kumara.backed.dto.SocialLinkRequest;
import com.kumara.backed.model.SocialLink;
import com.kumara.backed.model.User;
import com.kumara.backed.repository.SocialLinkRepository;
import com.kumara.backed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/links")
public class SocialLinkController {

    @Autowired private SocialLinkRepository linkRepository;
    @Autowired private UserRepository userRepository;

    // 1. GET ALL LINKS
    @GetMapping("/{userId}")
    public ResponseEntity<List<SocialLink>> getLinks(@PathVariable Long userId) {
        return ResponseEntity.ok(linkRepository.findByUserId(userId));
    }

    // 2. SAVE OR UPDATE LINK
    @PostMapping("/{userId}/save")
    public ResponseEntity<?> saveLink(@PathVariable Long userId, @RequestBody SocialLinkRequest request) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return ResponseEntity.badRequest().body("User tidak ditemukan");

        // Cek apakah link platform ini sudah ada?
        Optional<SocialLink> existingLink = linkRepository.findByUserIdAndPlatformName(userId, request.platform());

        SocialLink link;
        if (existingLink.isPresent()) {
            // Update link lama
            link = existingLink.get();
            link.setUrlLink(request.url());
        } else {
            // Buat baru
            link = new SocialLink();
            link.setUser(user);
            link.setPlatformName(request.platform());
            link.setUrlLink(request.url());
        }

        linkRepository.save(link);
        return ResponseEntity.ok("Link berhasil disimpan");
    }
}