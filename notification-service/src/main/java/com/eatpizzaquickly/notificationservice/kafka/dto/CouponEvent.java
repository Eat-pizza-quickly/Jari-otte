package com.eatpizzaquickly.notificationservice.kafka.dto;

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
    private String email;
    private LocalDateTime timestamp;
    private String notificationMessage;

    @Builder
    public CouponEvent(String eventType, Long couponId, String couponCode, Long userId, String email, LocalDateTime timestamp, String notificationMessage) {
        this.eventType = eventType;
        this.couponId = couponId;
        this.couponCode = couponCode;
        this.userId = userId;
        this.email = email;
        this.timestamp = timestamp;
        this.notificationMessage = notificationMessage;
    }
}