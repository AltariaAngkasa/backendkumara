package com.kumara.backed.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "modules")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private String videoUrl;
    private String duration;
    private String level; // Pemula, Lanjutan

    @Column(name = "module_type")
    private String moduleType; // Isinya: "VIDEO" atau "ARTICLE"

    @Column(columnDefinition = "LONGTEXT") // Agar muat teks artikel yang sangat panjang
    private String content;
}