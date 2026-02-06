package com.kumara.backed.controller;

import com.kumara.backed.model.*;
import com.kumara.backed.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;

    // List Produk User
    @GetMapping("/{userId}")
    public ResponseEntity<List<Product>> getProducts(@PathVariable Long userId) {
        return ResponseEntity.ok(productRepository.findByUserId(userId));
    }

    // Tambah Produk
    @PostMapping("/{userId}/add")
    public ResponseEntity<?> addProduct(@PathVariable Long userId, @RequestBody Product product) {
        return userRepository.findById(userId).map(user -> {
            product.setUser(user);
            productRepository.save(product);
            return ResponseEntity.ok(product);
        }).orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody Product productDetails) {
        return productRepository.findById(productId).map(product -> {
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            product.setStock(productDetails.getStock());
            product.setCategory(productDetails.getCategory());
            product.setTaxName(productDetails.getTaxName());
            product.setTaxRate(productDetails.getTaxRate());
            // imageUrl bisa diupdate jika ada logika upload
            
            productRepository.save(product);
            return ResponseEntity.ok("Produk berhasil diupdate");
        }).orElse(ResponseEntity.notFound().build());
    }

    // 4. HAPUS PRODUK
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        return productRepository.findById(productId).map(product -> {
            productRepository.delete(product);
            return ResponseEntity.ok("Produk berhasil dihapus");
        }).orElse(ResponseEntity.notFound().build());
    }
}