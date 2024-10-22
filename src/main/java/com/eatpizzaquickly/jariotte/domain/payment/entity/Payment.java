package com.eatpizzaquickly.jariotte.domain.payment.entity;

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
    private int amount;
    private String payInfo;

    @Column(name = "pay_method")
    @Enumerated(EnumType.STRING)
    private PayMethod payMethod;

    @Column(name = "pay_status")
    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;

    public Payment(String pay_uid, int amount, String payInfo, PayMethod payMethod, PayStatus payStatus) {
        this.payUid = pay_uid;
        this.amount = amount;
        this.payInfo = payInfo;
        this.payMethod = payMethod;
        this.payStatus = payStatus;
    }

    public void setPayStatus(PayStatus payStatus) {
        this.payStatus = payStatus;
    }
}
