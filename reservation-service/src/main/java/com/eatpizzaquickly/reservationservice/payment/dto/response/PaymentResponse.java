package com.eatpizzaquickly.reservationservice.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private String paymentKey;         // 결제 키
    private String orderId;            // 주문 ID
    private String status;             // 결제 상태
    private Long totalAmount;          // 총 결제 금액
    private LocalDateTime approvedAt;  // 결제 승인 시각
    private String method;             // 결제 수단

    @JsonProperty("card")
    private CardResponse card;         // 카드 결제 정보
}