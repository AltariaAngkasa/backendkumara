package com.kumara.backed.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TransactionDTOs {

    public record TransactionItemRequest(
        Long productId,
        Integer quantity
    ) {}


    // Request saat user input transaksi
    public record TransactionRequest(
        Long customerId,
        BigDecimal amount,
        String description,
        LocalDate date,
        List<TransactionItemRequest> items,
        Long couponId,

        String paymentMethod, // String deskripsi
        BigDecimal cashReceived,
        BigDecimal changeAmount,
        String status // Langsung kirim status (LUNAS/BELUM)
    ) {}

    // Response untuk data Grafik (Tanggal & Total Pendapatan)
    public record ChartDataPoint(
        String date, // Format: "Mon", "Tue" atau "01 Oct"
        BigDecimal total
    ) {}
}