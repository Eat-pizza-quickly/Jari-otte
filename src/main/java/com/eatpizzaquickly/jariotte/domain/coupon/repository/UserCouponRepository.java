package com.eatpizzaquickly.jariotte.domain.coupon.repository;


import com.eatpizzaquickly.jariotte.domain.coupon.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    // 사용자의 이메일과 쿠폰 ID로 중복 발급 여부 확인
    boolean existsByCouponIdAndUserEmail(Long couponId, String email);

    // 특정 사용자의 모든 쿠폰 조회
    List<UserCoupon> findByUserEmail(String email);


}
