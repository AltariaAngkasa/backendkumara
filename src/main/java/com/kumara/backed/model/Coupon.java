package com.kumara.backed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    private String name; // Nama Kupon (Misal: DISKON10)
    
    private String type; // "PERCENT" atau "NOMINAL"
    
    private BigDecimal value; // Misal: 10.0 (10%) atau 5000 (Rp 5.000)
    
    private Integer stock; // Kuota kupon
}