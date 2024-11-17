package com.eatpizzaquickly.couponservice.dto;


import com.eatpizzaquickly.couponservice.entity.CouponType;
import com.eatpizzaquickly.couponservice.entity.DiscountType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    private LocalDate expiryDate;

    public CouponRequestDto(String couponName,String couponCode, CouponType couponType, DiscountType discountType, int discount, int price, int quantity, LocalDate expiryDate) {
        this.couponName = couponName;
        this.couponCode = couponCode;
        this.couponType = couponType;
        this.discountType = discountType;
        this.discount = discount;
        this.price = price;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }
}
