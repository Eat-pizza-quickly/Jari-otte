package com.eatpizzaquickly.batchservice.coupon.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CouponDto {

    private Long id;
    private String couponCode;
    private LocalDate expiryDate;
    private boolean isActive;

    public CouponDto(Long id, String couponCode, LocalDate expiryDate, boolean isActive) {
        this.id = id;
        this.couponCode = couponCode;
        this.expiryDate = expiryDate;
        this.isActive = isActive;
    }
}
