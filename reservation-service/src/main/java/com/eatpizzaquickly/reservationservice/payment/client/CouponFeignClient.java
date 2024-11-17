package com.eatpizzaquickly.reservationservice.payment.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "coupon-service")
public interface CouponFeignClient {

    @GetMapping("/api/v1/coupons/apply")
    Long applyCoupon(@RequestParam("couponId") Long couponId, @RequestParam("amount") Long amount);
}
