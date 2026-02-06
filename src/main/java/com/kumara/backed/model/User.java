package com.kumara.backed.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data // Lombok otomatis bikin Getter, Setter, toString
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "avatar_url")
    private String avatarUrl; 

    // @PrePersist
    // protected void onCreate() {
    //     createdAt = LocalDateTime.now();
    // }
    @Column(columnDefinition = "VARCHAR(20) DEFAULT 'USER'")
    private String role; // "USER" atau "ADMIN"

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (role == null) role = "USER"; // Default user biasa
    }
}