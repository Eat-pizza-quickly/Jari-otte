package com.eatpizzaquickly.batchservice.settlement.dto.response;

import com.eatpizzaquickly.batchservice.settlement.entity.PayStatus;
import com.eatpizzaquickly.batchservice.settlement.entity.SettlementStatus;
import lombok.Getter;

@Getter
public class PaymentResponseDto {
    private Long id;
    private SettlementStatus settlementStatus;
    private PayStatus payStatus;
    private Long amount;
    private Long concertId;

    public void setSettlementStatus(SettlementStatus settlementStatus) {
        this.settlementStatus = settlementStatus;
    }
}
