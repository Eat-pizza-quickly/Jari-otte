package com.eatpizzaquickly.couponservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(
        name = "usercoupon",
        indexes = {
                @Index(name = "idx_user_coupon_expiry", columnList = "userId, couponId, expiryDate, isExpired"),
                @Index(name = "idx_user_coupon_status", columnList = "isUsed, isExpired, expiryDate")
        }
)
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;  // 외래 키가 아닌 단순 사용자 식별자

    private Long couponId;
    @Column(nullable = false)
    private Boolean isUsed = false;

    private LocalDate expiryDate;

    private Boolean isExpired = false;  // 인덱스에 맞추기 위해 추가한 컬럼


    private LocalDateTime issueDate = LocalDateTime.now();

    public void updateUserId(Long userId) {
        this.userId = userId;
    }

    public void markAsUsed() {
        this.isUsed = true;
    }

    // isExpired는 별도 컬럼으로 존재하지 않고 expiryDate로 판단
    public boolean isExpired(LocalDate currentDate) {
        return expiryDate.isBefore(currentDate);
    }

    @Builder
    public UserCoupon(Long userId, Long couponId, Boolean isUsed, LocalDate expiryDate, LocalDateTime issueDate) {
        this.userId = userId;
        this.couponId = couponId;
        this.isUsed = isUsed != null ? isUsed : false;  // 기본값 설정
        this.expiryDate = LocalDate.now().minusDays(1);
        this.issueDate = issueDate != null ? issueDate : LocalDateTime.now();  // 기본값 설정
    }
}
