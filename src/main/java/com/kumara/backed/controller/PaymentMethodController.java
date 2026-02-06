package com.kumara.backed.controller;
import com.kumara.backed.model.PaymentMethod;
import com.kumara.backed.repository.PaymentMethodRepository;
import com.kumara.backed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentMethodController {
    @Autowired private PaymentMethodRepository paymentRepository;
    @Autowired private UserRepository userRepository;

    // 1. GET METHODS
    @GetMapping("/{userId}")
    public ResponseEntity<List<PaymentMethod>> getMethods(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentRepository.findByUserId(userId));
    }

    // 2. ADD METHOD
    @PostMapping("/{userId}/add")
    public ResponseEntity<?> addMethod(@PathVariable Long userId, @RequestBody PaymentMethod req) {
        // Cek apakah User ada?
        return userRepository.findById(userId).map(user -> {
            req.setUser(user);
            
            // Validasi manual agar tidak error database
            if (req.getType() == null) req.setType("UNKNOWN");
            if (req.getProvider() == null) req.setProvider("Unknown");
            
            paymentRepository.save(req);
            return ResponseEntity.ok(req);
        }).orElse(ResponseEntity.notFound().build());
    }

    // 3. DELETE METHOD
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMethod(@PathVariable Long id) {
        paymentRepository.deleteById(id);
        return ResponseEntity.ok("Deleted");
    }
}
