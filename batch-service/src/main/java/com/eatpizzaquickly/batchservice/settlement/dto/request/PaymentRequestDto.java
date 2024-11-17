package com.eatpizzaquickly.batchservice.settlement.dto.request;

import com.eatpizzaquickly.batchservice.settlement.entity.PayStatus;
import com.eatpizzaquickly.batchservice.settlement.entity.SettlementStatus;
import com.eatpizzaquickly.batchservice.settlement.entity.TempPayment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentRequestDto {
    private Long id;
    private SettlementStatus settlementStatus;
    private PayStatus payStatus;
    private Long amount;

    public static PaymentRequestDto from(TempPayment tempPayment) {
        return new PaymentRequestDto(
                tempPayment.getPaymentId(),
                tempPayment.getSettlementStatus(),
                tempPayment.getPayStatus(),
                tempPayment.getAmount()
        );
    };
}
