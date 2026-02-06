package com.kumara.backed.controller;

import com.kumara.backed.dto.ReportItemDTO;
import com.kumara.backed.dto.TransactionDTOs.*;
import com.kumara.backed.model.*;
import com.kumara.backed.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired private TransactionRepository transactionRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private CouponRepository couponRepository;

    // 1. TAMBAH TRANSAKSI
    @PostMapping("/{userId}/add")
    public ResponseEntity<?> addTransaction(@PathVariable Long userId, @RequestBody TransactionRequest request) {
        User user = userRepository.findById(userId).orElse(null);
        Customer customer = customerRepository.findById(request.customerId()).orElse(null);

        if (user == null || customer == null) return ResponseEntity.badRequest().body("Data tidak lengkap");

        Transaction trx = new Transaction();
        trx.setUser(user);
        trx.setCustomer(customer);
        trx.setDescription(request.description());
        trx.setTransactionDate(request.date() != null ? request.date() : LocalDate.now());
        trx.setPaymentMethod(request.paymentMethod());
        trx.setCashReceived(request.cashReceived());
        trx.setChangeAmount(request.changeAmount());
        if(request.status() != null) trx.setStatus(request.status());

        // Hitung Total & Set Item
        List<TransactionItem> items = new ArrayList<>();
        // BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal subTotal = BigDecimal.ZERO;
        for (var itemReq : request.items()) {
            Product product = productRepository.findById(itemReq.productId()).orElse(null);
            if (product != null) {
                TransactionItem item = new TransactionItem();
                item.setTransaction(trx);
                item.setProduct(product);
                item.setQuantity(itemReq.quantity());
                item.setPriceAtPurchase(product.getPrice());
                
                // Rumus: Harga * Qty + Pajak
                BigDecimal subtotal = product.getPrice().multiply(new BigDecimal(itemReq.quantity()));
                if(product.getTaxRate() != null && product.getTaxRate() > 0) {
                     BigDecimal tax = subtotal.multiply(BigDecimal.valueOf(product.getTaxRate() / 100));
                     subtotal = subtotal.add(tax);
                }

                subTotal = subTotal.add(subtotal);
                items.add(item);
                
                // Kurangi Stok (Opsional)
                product.setStock(product.getStock() - itemReq.quantity());
                productRepository.save(product);
            }
        }

        trx.setItems(items);
        BigDecimal discount = BigDecimal.ZERO;
        if (request.couponId() != null) {
            Coupon coupon = couponRepository.findById(request.couponId()).orElse(null);
            
            // Cek stok kupon
            if (coupon != null && coupon.getStock() > 0) {
                trx.setCouponName(coupon.getName());
                
                if ("PERCENT".equalsIgnoreCase(coupon.getType())) {
                    // Rumus: Subtotal * (Persen / 100)
                    discount = subTotal.multiply(coupon.getValue()).divide(BigDecimal.valueOf(100));
                } else {
                    // Rumus: Nominal langsung
                    discount = coupon.getValue();
                }

                // Kurangi Stok Kupon
                coupon.setStock(coupon.getStock() - 1);
                couponRepository.save(coupon);
            }
        }

        trx.setDiscountAmount(discount);
        
        // Total Akhir = Subtotal - Diskon
        BigDecimal finalTotal = subTotal.subtract(discount);
        if (finalTotal.compareTo(BigDecimal.ZERO) < 0) finalTotal = BigDecimal.ZERO; // Jangan minus
        
        trx.setAmount(finalTotal);

        transactionRepository.save(trx);
        return ResponseEntity.ok("Transaksi berhasil");

        // trx.setAmount(totalAmount); // Total otomatis dihitung backend

        // transactionRepository.save(trx);
        // return ResponseEntity.ok("Transaksi berhasil");
    }

    // 2. LIST TRANSAKSI TERBARU (History)
    @GetMapping("/{userId}/history")
    public ResponseEntity<List<Transaction>> getHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(transactionRepository.findByUserIdOrderByIdDesc(userId));
    }

    // 3. DATA GRAFIK (7 Hari Terakhir)
    @GetMapping("/{userId}/chart")
    public ResponseEntity<List<ChartDataPoint>> getChartData(@PathVariable Long userId) {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(6);
        List<Object[]> rawData = transactionRepository.findDailyRevenue(userId, sevenDaysAgo);

        // Mapping data agar yang omzetnya 0 tetap muncul di grafik
        Map<LocalDate, BigDecimal> revenueMap = new HashMap<>();
        for (Object[] row : rawData) {
            revenueMap.put((LocalDate) row[0], (BigDecimal) row[1]);
        }

        List<ChartDataPoint> chartData = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        // Loop dari 7 hari lalu sampai hari ini
        for (int i = 0; i < 7; i++) {
            LocalDate date = sevenDaysAgo.plusDays(i);
            BigDecimal total = revenueMap.getOrDefault(date, BigDecimal.ZERO);
            chartData.add(new ChartDataPoint(date.format(formatter), total));
        }

        return ResponseEntity.ok(chartData);
    }

    @GetMapping("/{userId}/best-seller")
    public ResponseEntity<?> getBestSeller(@PathVariable Long userId) {
        ReportItemDTO best = transactionRepository.findBestSellingProduct(userId);
        if (best == null) return ResponseEntity.ok(new ReportItemDTO("Belum ada data", 0L, BigDecimal.ZERO));
        return ResponseEntity.ok(best);
    }

    // 5. GET REPORT DATA (Daily, Weekly, Monthly)
    @GetMapping("/{userId}/report")
    public ResponseEntity<List<ReportItemDTO>> getReport(
            @PathVariable Long userId, 
            @RequestParam String type) { // type: daily, weekly, monthly
        
        LocalDate end = LocalDate.now();
        LocalDate start = LocalDate.now();

        if (type.equalsIgnoreCase("weekly")) {
            start = end.minusWeeks(1);
        } else if (type.equalsIgnoreCase("monthly")) {
            start = end.minusMonths(1);
        }
        // kalau daily, start = end (hari ini saja)

        return ResponseEntity.ok(transactionRepository.getSalesReport(userId, start, end));
    }

    @PutMapping("/{transactionId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long transactionId, @RequestParam String status) {
        return transactionRepository.findById(transactionId).map(trx -> {
            trx.setStatus(status);
            transactionRepository.save(trx);
            return ResponseEntity.ok("Status berhasil diubah");
        }).orElse(ResponseEntity.notFound().build());
    }
}