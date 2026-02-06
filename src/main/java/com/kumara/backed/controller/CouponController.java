package com.kumara.backed.controller;

import com.kumara.backed.model.*;
import com.kumara.backed.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {
    @Autowired private CouponRepository couponRepository;
    @Autowired private UserRepository userRepository;

    // 1. LIST KUPON
    @GetMapping("/{userId}")
    public ResponseEntity<List<Coupon>> getCoupons(@PathVariable Long userId) {
        return ResponseEntity.ok(couponRepository.findByUserId(userId));
    }

    // 2. TAMBAH KUPON
    @PostMapping("/{userId}/add")
    public ResponseEntity<?> addCoupon(@PathVariable Long userId, @RequestBody Coupon coupon) {
        return userRepository.findById(userId).map(user -> {
            coupon.setUser(user);
            couponRepository.save(coupon);
            return ResponseEntity.ok(coupon);
        }).orElse(ResponseEntity.notFound().build());
    }

    // 3. EDIT KUPON
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCoupon(@PathVariable Long id, @RequestBody Coupon req) {
        return couponRepository.findById(id).map(coupon -> {
            coupon.setName(req.getName());
            coupon.setType(req.getType());
            coupon.setValue(req.getValue());
            coupon.setStock(req.getStock());
            couponRepository.save(coupon);
            return ResponseEntity.ok("Updated");
        }).orElse(ResponseEntity.notFound().build());
    }

    // 4. HAPUS KUPON
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable Long id) {
        couponRepository.deleteById(id);
        return ResponseEntity.ok("Deleted");
    }
}