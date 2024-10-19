package com.eatpizzaquickly.jariotte.domain.coupon.entity;


import com.eatpizzaquickly.jariotte.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserCoupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // 외래 키 설정
    private User user;

    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false) // 외래 키 설정
    private Coupon coupon;
}