package com.eatpizzaquickly.jariotte.domain.coupon.repository;

import com.eatpizzaquickly.jariotte.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponsRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCouponCode(String couponCode);

}
