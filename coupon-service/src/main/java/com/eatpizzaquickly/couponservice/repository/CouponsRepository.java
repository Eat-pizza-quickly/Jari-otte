package com.eatpizzaquickly.couponservice.repository;


import com.eatpizzaquickly.couponservice.entity.Coupon;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CouponsRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCouponCode(String couponCode);


    // idx_coupon_code_type_active_expiry 인덱스 활용
    @Query("SELECT c FROM Coupon c " +
            "WHERE c.isActive = true " +
            "AND c.expiryDate < :currentDate " +
            "ORDER BY c.id ASC")
    List<Coupon> findExpiredCoupons(@Param("currentDate") LocalDate currentDate, Pageable pageable);

    @Modifying
    @Query("UPDATE Coupon c " +
            "SET c.isActive = false " +
            "WHERE c.id IN :couponIds " +
            "AND c.isActive = true " +
            "AND c.expiryDate < :currentDate")
    int deactivateExpiredCoupons(
            @Param("couponIds") List<Long> couponIds,
            @Param("currentDate") LocalDate currentDate
    );

}
