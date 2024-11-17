package com.eatpizzaquickly.reservationservice.payment.entity;

import com.eatpizzaquickly.reservationservice.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor
public class Payment extends Timestamped {
    @Column(name = "pay_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pay_uid")
    private String payUid;

    private Long amount;

    private String payInfo;

    @Column(name = "pay_method")
    @Enumerated(EnumType.STRING)
    private PayMethod payMethod;

    @Column(name = "pay_status")
    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;

    private String paymentKey;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(name = "settlement_status")
    @Enumerated(EnumType.STRING)
    private SettlementStatus settlementStatus = SettlementStatus.UNSETTLED;

    private LocalDateTime settledAt;

    private LocalDateTime paidAt;

    public Payment(String pay_uid, Long price, String payInfo, PayMethod payMethod, PayStatus payStatus, Reservation reservation) {
        this.payUid = pay_uid;
        this.amount = price;
        this.payInfo = payInfo;
        this.payMethod = payMethod;
        this.payStatus = payStatus;
        this.reservation = reservation;
    }

    public void setPayStatus(PayStatus payStatus) {
        this.payStatus = payStatus;
    }

    public void setPaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
    }

    public void setSettlementStatus(SettlementStatus settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public void setPaidAt(LocalDateTime currentTime) {
        this.paidAt = currentTime;
    }

    public void setSettledAt(LocalDateTime currentTime) {
        this.settledAt = currentTime;
    }
}