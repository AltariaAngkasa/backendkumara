package com.kumara.backed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    private String name;
    private String description;
    
    // Gambar kita simpan URL/Path-nya saja (String)
    private String imageUrl; 
    private String category; // Makanan, Minuman, Jasa, dll

    private BigDecimal price;
    private Integer stock;

    // Pengaturan Pajak
    private String taxName; // Misal: PPN
    private Double taxRate; // Misal: 11.0 (persen)
}