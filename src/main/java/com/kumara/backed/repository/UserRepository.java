package com.kumara.backed.repository;

import com.kumara.backed.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Kita butuh fungsi ini untuk cek login & register
    Optional<User> findByEmail(String email);
    
    // Cek apakah email sudah dipakai saat registrasi
    boolean existsByEmail(String email);
}