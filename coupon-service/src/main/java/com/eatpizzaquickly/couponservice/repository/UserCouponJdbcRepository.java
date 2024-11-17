package com.eatpizzaquickly.couponservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class UserCouponJdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    public int bulkDeleteExpiredUserCoupons(List<Long> userCouponIds) {
        if (userCouponIds.isEmpty()) {
            return 0;
        }

        // Placeholder를 동적으로 생성
        String placeholders = String.join(",", userCouponIds.stream().map(id -> "?").toArray(String[]::new));
        String sql = "DELETE FROM user_coupon WHERE id IN (" + placeholders + ") AND expiry_date <= CURRENT_DATE AND is_used = false";

        // 사용자 ID 리스트를 배열로 변환
        Object[] params = userCouponIds.toArray();

        return jdbcTemplate.update(sql, params);
    }
}
