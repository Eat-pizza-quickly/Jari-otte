package com.eatpizzaquickly.batchservice.settlement.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(
                columnNames = { "paymentId" }) })
public class TempPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long paymentId;  // 원래 Payment의 ID
    @Enumerated(EnumType.STRING)
    private SettlementStatus settlementStatus;
    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;
    private Long amount;
    private Long concertId;

    @Builder
    public TempPayment(Long paymentId, SettlementStatus settlementStatus, PayStatus payStatus, Long amount, Long concertId) {
        this.paymentId = paymentId;
        this.settlementStatus = settlementStatus;
        this.payStatus = payStatus;
        this.amount = amount;
        this.concertId = concertId;
    }

    public void setSettlementStatus(SettlementStatus settlementStatus) {
        this.settlementStatus = settlementStatus;
    }
}
