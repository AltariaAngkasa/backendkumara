package com.kumara.backed.repository;
import com.kumara.backed.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    List<PaymentMethod> findByUserId(Long userId);
    void deleteByUserIdAndType(Long userId, String type); // Untuk reset
}
