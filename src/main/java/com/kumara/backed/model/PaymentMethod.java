package com.kumara.backed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "payment_methods")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    // Tipe: "CASH", "TRANSFER", "EWALLET"
    private String type; 
    
    // Nama Provider: "BCA", "BRI", "GOPAY", "OVO", "CASH"
    private String provider; 
    
    // Nomor Rekening / HP (Bisa kosong kalau CASH)
    private String accountNumber; 
    
    // Atas Nama (Opsional)
    private String accountHolder;
}