package com.kumara.backed.repository;

import com.kumara.backed.dto.ReportItemDTO;
import com.kumara.backed.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.kumara.backed.model.TransactionItem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    // 1. Ambil list transaksi urut dari yang terbaru
    List<Transaction> findByUserIdOrderByIdDesc(Long userId);
    //List<Transaction> findByUserIdOrderByTransactionDateDesc(Long userId);
    List<Transaction> findByUserIdAndTransactionDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    // 2. Query Grafik: Hitung total per hari untuk user tertentu dalam rentang tanggal
    @Query("SELECT t.transactionDate, SUM(t.amount) FROM Transaction t " +
           "WHERE t.user.id = :userId AND t.transactionDate >= :startDate " +
           "GROUP BY t.transactionDate ORDER BY t.transactionDate ASC")
    List<Object[]> findDailyRevenue(@Param("userId") Long userId, @Param("startDate") LocalDate startDate);

    @Query("SELECT new com.kumara.backed.dto.ReportItemDTO(ti.product.name, SUM(ti.quantity), SUM(ti.priceAtPurchase * ti.quantity)) " +
           "FROM TransactionItem ti " +
           "WHERE ti.transaction.user.id = :userId " +
           "GROUP BY ti.product.name " +
           "ORDER BY SUM(ti.quantity) DESC LIMIT 1")
    ReportItemDTO findBestSellingProduct(@Param("userId") Long userId);

    // 2. Data Laporan (Group by Product dalam rentang tanggal)
    @Query("SELECT new com.kumara.backed.dto.ReportItemDTO(ti.product.name, SUM(ti.quantity), SUM(ti.priceAtPurchase * ti.quantity)) " +
           "FROM TransactionItem ti " +
           "WHERE ti.transaction.user.id = :userId " +
           "AND ti.transaction.transactionDate BETWEEN :startDate AND :endDate " +
           "GROUP BY ti.product.name")
    List<ReportItemDTO> getSalesReport(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}