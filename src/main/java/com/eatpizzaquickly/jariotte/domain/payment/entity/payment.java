package com.eatpizzaquickly.jariotte.domain.payment.entity;

import com.eatpizzaquickly.jariotte.domain.reservation.entity.reservation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class payment {
    @Column(name = "pay_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pay_uid;
    private int amount;
    private String pay_info;

    @Column(name = "pay_method")
    @Enumerated(EnumType.STRING)
    private PayMethod pay_method;

    @Column(name = "pay_status")
    @Enumerated(EnumType.STRING)
    private PayStatus pay_status;
}
