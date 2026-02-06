package com.kumara.backed.repository;

import com.kumara.backed.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Query Magic: Spring otomatis bikin query "SELECT * FROM customers WHERE user_id = ?"
    List<Customer> findByUserId(Long userId);
}