package com.eatpizzaquickly.notificationservice.kafka.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentEvent {
    private String eventType;
    private Long paymentId;
    private String email;
    private Long amount;
    private String paymentStatus;
    private String notificationMessage;

    @Builder
    public PaymentEvent(String eventType, Long paymentId, String email, Long amount, String paymentStatus, String notificationMessage) {
        this.eventType = eventType;
        this.paymentId = paymentId;
        this.email = email;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.notificationMessage = notificationMessage;
    }
}