package com.eatpizzaquickly.jariotte.domain.payment.entity;

import com.eatpizzaquickly.jariotte.domain.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor
public class Payment {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

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
}
