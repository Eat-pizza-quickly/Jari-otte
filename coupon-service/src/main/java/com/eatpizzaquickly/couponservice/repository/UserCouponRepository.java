package com.eatpizzaquickly.couponservice.repository;

import com.eatpizzaquickly.couponservice.entity.UserCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    boolean existsByUserIdAndCouponId(Long userId, Long couponId);

    List<UserCoupon> findByUserIdAndIsUsedFalse(Long userId);

    @Query("SELECT uc.userId FROM UserCoupon uc WHERE uc.couponId = :couponId")
    List<Long> findUserIdsByCouponId(@Param("couponId") Long couponId);

    @Query("SELECT uc FROM UserCoupon uc " +
            "WHERE DATE(uc.expiryDate) <= DATE(:currentDate) " +
            "AND uc.isUsed = false " +
            "ORDER BY uc.id ASC")
    Page<UserCoupon> findExpiredUserCoupons(@Param("currentDate") LocalDate currentDate, Pageable pageable);



    @Modifying
    @Query("DELETE FROM UserCoupon uc " +
            "WHERE uc.id IN :userCouponIds " +
            "AND uc.expiryDate <= CURRENT_DATE " +  // < 를 <= 로 수정
            "AND uc.isUsed = false")
    int deleteExpiredUserCoupons(@Param("userCouponIds") List<Long> userCouponIds);


}