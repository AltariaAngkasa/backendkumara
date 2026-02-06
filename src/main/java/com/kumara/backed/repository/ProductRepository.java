package com.kumara.backed.repository;

import com.kumara.backed.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Mencari semua produk berdasarkan ID User (Pemilik Toko)
    List<Product> findByUserId(Long userId);
}