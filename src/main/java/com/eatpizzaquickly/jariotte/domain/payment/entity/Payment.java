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
    private String pay_uid;
    private int amount;
    private String pay_info;

    @Column(name = "pay_method")
    @Enumerated(EnumType.STRING)
    private PayMethod pay_method;

    @Column(name = "pay_status")
    @Enumerated(EnumType.STRING)
    private PayStatus pay_status;

    public Payment(String pay_uid, int amount, String payInfo, PayMethod payMethod, PayStatus payStatus) {
        this.pay_uid = pay_uid;
        this.amount = amount;
        this.pay_info = payInfo;
        this.pay_method = payMethod;
        this.pay_status = payStatus;
    }

    public void setPayStatus(PayStatus payStatus) {
        this.pay_status = payStatus;
    }
}
