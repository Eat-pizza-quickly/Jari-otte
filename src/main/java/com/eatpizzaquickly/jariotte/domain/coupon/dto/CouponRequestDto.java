package com.eatpizzaquickly.jariotte.domain.coupon.dto;

import com.eatpizzaquickly.jariotte.domain.coupon.entity.CouponType;
import com.eatpizzaquickly.jariotte.domain.coupon.entity.DiscountType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponRequestDto {

    private String couponName;

    private String couponCode;

    private CouponType couponType;

    private DiscountType discountType;

    private int discount;

    private int price;

    private int quantity;

    public CouponRequestDto(String couponName,String couponCode, CouponType couponType, DiscountType discountType, int discount, int price, int quantity) {
        this.couponName = couponName;
        this.couponCode = couponCode;
        this.couponType = couponType;
        this.discountType = discountType;
        this.discount = discount;
        this.price = price;
        this.quantity = quantity;
    }
}
