package com.kumara.backed.controller;

import com.kumara.backed.dto.CustomerRequest;
import com.kumara.backed.model.Customer;
import com.kumara.backed.model.User;
import com.kumara.backed.repository.CustomerRepository;
import com.kumara.backed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. TAMBAH PELANGGAN BARU
    // URL: POST /api/customers/{userId}/add
    @PostMapping("/{userId}/add")
public ResponseEntity<?> addCustomer(
        @PathVariable Long userId,
        @RequestBody CustomerRequest request
) {
    System.out.println("REQUEST ADD CUSTOMER, userId = " + userId);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("USER TIDAK DITEMUKAN"));

    Customer customer = new Customer();
    customer.setUser(user);
    customer.setName(request.name());
    customer.setPhone(request.phone());
    customer.setAddress(request.address());
    customer.setNotes(request.notes());

    Customer saved = customerRepository.save(customer);
    return ResponseEntity.ok(saved);
}


    // 2. LIHAT SEMUA PELANGGAN MILIK USER TERTENTU
    // URL: GET /api/customers/{userId}
    @GetMapping("/{userId}")
    public ResponseEntity<List<Customer>> getCustomersByUser(@PathVariable Long userId) {
        List<Customer> customers = customerRepository.findByUserId(userId);
        return ResponseEntity.ok(customers);
    }
}