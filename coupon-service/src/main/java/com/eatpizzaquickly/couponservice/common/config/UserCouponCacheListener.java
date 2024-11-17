package com.eatpizzaquickly.couponservice.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class UserCouponCacheListener {

    private final CacheManager cacheManager;

    @EventListener
    public void handleUserCouponsChangedEvent(UserCouponsChangedEvent event) {
        Cache userCouponsCache = cacheManager.getCache("userCoupons");
        if (userCouponsCache != null) {
            userCouponsCache.evict(event.getUserId()); // 사용자 쿠폰 목록 캐시 무효화
        }
    }
}
