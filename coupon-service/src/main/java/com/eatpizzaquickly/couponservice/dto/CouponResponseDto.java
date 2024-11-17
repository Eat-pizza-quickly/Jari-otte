package com.eatpizzaquickly.couponservice.dto;

import com.eatpizzaquickly.couponservice.entity.Coupon;
import com.eatpizzaquickly.couponservice.entity.CouponType;
import com.eatpizzaquickly.couponservice.entity.DiscountType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class CouponResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;  // 직렬화 버전 ID 설정

    private String couponCode;
    private String couponName;
    private CouponType couponType;
    private DiscountType discountType;
    private int discount;
    private int price;
    private int quantity;
    private Boolean isActive;

    // Coupon 엔티티로부터 CouponResponseDto를 생성하는 from 메서드
    public static CouponResponseDto from(Coupon coupon) {
        CouponResponseDto responseDto = new CouponResponseDto();
        responseDto.couponCode = coupon.getCouponCode();
        responseDto.couponName = coupon.getCouponName();
        responseDto.couponType = coupon.getCouponType();
        responseDto.discountType = coupon.getDiscountType();
        responseDto.discount = coupon.getDiscount();
        responseDto.price = coupon.getPrice();
        responseDto.quantity = coupon.getQuantity();
        responseDto.isActive = coupon.getIsActive();
        return responseDto;
    }
}