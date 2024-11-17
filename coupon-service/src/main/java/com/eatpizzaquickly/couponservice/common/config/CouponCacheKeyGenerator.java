package com.eatpizzaquickly.couponservice.common.config;

import org.springframework.stereotype.Component;

@Component
public class CouponCacheKeyGenerator {

    private static final String COUPON_KEY_PREFIX = "coupon:";
    private static final String USER_COUPONS_KEY_PREFIX = "user:coupons:";

    public String generateCouponKey(String couponCode) {
        return COUPON_KEY_PREFIX + couponCode;
    }

    public String generateUserCouponsKey(Long userId) {
        return USER_COUPONS_KEY_PREFIX + userId;
    }
}
