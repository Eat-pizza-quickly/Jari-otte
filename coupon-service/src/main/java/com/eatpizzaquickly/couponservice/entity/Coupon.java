package com.eatpizzaquickly.couponservice.entity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
@Table(name = "Coupon", indexes = {
        @Index(name = "idx_coupon_code_type_active_expiry", columnList = "couponCode, couponType, isActive, expiryDate")
})

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon implements Serializable {

    private static final long serialVersionUID = 1L;  // serialVersionUID 추가

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    private String couponCode;

    private String couponName;

    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private int discount = 0;

    private int price = 0;

    private int quantity = 0;

    private  Boolean isActive = true;

    private LocalDate expiryDate;


    @Builder
    private Coupon(String couponCode, String couponName, CouponType couponType,DiscountType discountType ,int discount, int price, int quantity,LocalDate expiryDate) {
        this.couponCode = couponCode;
        this.couponName = couponName;
        this.couponType = couponType;
        this.discountType = discountType;
        this.discount = discount;
        this.price = price;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    public void decreaseQuantity() {
        if (this.quantity > 0) {
            this.quantity--;
        } else {
            throw new IllegalStateException("수량이 0인 쿠폰입니다.");
        }
    }

    public void decreaseQuantity(int amount) {
        if (this.quantity >= amount) {
            this.quantity -= amount;
        } else {
            throw new IllegalStateException("발급할 수량이 부족합니다.");
        }
    }

    public boolean isCouponActive() {
        // 만료 날짜를 확인해 활성 상태를 반환
        if (expiryDate != null && expiryDate.isBefore(LocalDate.now())) {
            this.isActive = false; // 만료 시 비활성화 처리
        }
        return this.isActive;
    }


}
