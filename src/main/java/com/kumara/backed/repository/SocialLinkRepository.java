package com.kumara.backed.repository;

import com.kumara.backed.model.SocialLink;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SocialLinkRepository extends JpaRepository<SocialLink, Long> {
    List<SocialLink> findByUserId(Long userId);
    
    // Cari apakah user sudah punya link untuk platform tertentu (biar bisa update, bukan insert doang)
    Optional<SocialLink> findByUserIdAndPlatformName(Long userId, String platformName);
}