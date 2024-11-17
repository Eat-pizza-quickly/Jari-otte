package com.eatpizzaquickly.couponservice.kafka;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CouponEvent {
    private String eventType;
    private Long couponId;
    private String couponCode;
    private Long userId;
    private LocalDateTime timestamp;
    private String email;  // 이메일 필드 추가
    private String notificationMessage;  // 알림 메시지 추가

    @Builder
    public CouponEvent(String eventType, Long couponId, Long userId,String couponCode,LocalDateTime timestamp,
                       String email, String notificationMessage) {
        this.eventType = eventType;
        this.couponId = couponId;
        this.userId = userId;
        this.couponCode = couponCode;
        this.timestamp = timestamp;
        this.email = email;
        this.notificationMessage = notificationMessage;
    }
}
