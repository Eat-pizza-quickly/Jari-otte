package com.eatpizzaquickly.reservationservice.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardResponse {
    private Long amount;               // 카드 결제 금액
    private String issuerCode;         // 카드 발급사 코드
    private String acquirerCode;       // 카드 매입사 코드
    private String number;             // 카드 번호
    private String installmentPlanMonths; // 할부 개월 수
    private String approveNo;
}
