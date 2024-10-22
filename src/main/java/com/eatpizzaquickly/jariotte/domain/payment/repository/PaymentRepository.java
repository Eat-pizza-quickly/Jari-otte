package com.eatpizzaquickly.jariotte.domain.payment.repository;

import com.eatpizzaquickly.jariotte.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPay_uid(String orderId);
}
