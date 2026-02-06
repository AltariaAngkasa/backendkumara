package com.kumara.backed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.EAGER) // Eager agar nama customer langsung terbawa
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private BigDecimal amount; // Pakai BigDecimal untuk uang, jangan Double/Float

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate; // Tanggal transaksi (tanpa jam)

    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // @PrePersist
    // protected void onCreate() {
    //     createdAt = LocalDateTime.now();
    //     if (transactionDate == null) {
    //         transactionDate = LocalDate.now();
    //     }
    // }

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
    private List<TransactionItem> items;

    @Column(name = "status")
    private String status; // Isinya "LUNAS" atau "BELUM_LUNAS"

    @Column(name = "discount_amount")
    private BigDecimal discountAmount; // Total rupiah yang dipotong

    @Column(name = "coupon_name")
    private String couponName; // Nama kupon yang dipakai (untuk invoice)

    @Column(name = "payment_method")
    private String paymentMethod; // Contoh: "Transfer - BCA" atau "Cash"

    @Column(name = "cash_received")
    private BigDecimal cashReceived; // Uang yang diterima (khusus cash)

    @Column(name = "change_amount")
    private BigDecimal changeAmount; // Kembalian (khusus cash)

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (transactionDate == null) transactionDate = LocalDate.now();
        if (status == null) status = "BELUM_LUNAS"; // Default belum lunas
        if (discountAmount == null) discountAmount = BigDecimal.ZERO;
    }
}