package com.eatpizzaquickly.jariotte.domain.coupon.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupone")
public class Coupon {

    @Id @GeneratedValue
    @Column(name = "coupon_id")
    private Long id;

    private String couponCode;

    private String couponName;

    private CouponType couponType;

    private int discount = 0;

    private int price = 0;

    private int quantity = 0;

    private  Boolean isActive = false;


    @Builder
    private Coupon(String couponCode, String couponName, CouponType couponType, int discount, int price, int quantity) {
        this.couponCode = couponCode;
        this.couponName = couponName;
        this.couponType = couponType;
        this.discount = discount;
        this.price = price;
        this.quantity = quantity;
    }

    public void checkIsActive(){
        this.isActive = true;

    }



}
