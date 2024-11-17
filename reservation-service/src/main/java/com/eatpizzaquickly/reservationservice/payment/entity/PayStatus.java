package com.eatpizzaquickly.reservationservice.payment.entity;


import lombok.Getter;

@Getter
public enum PayStatus {
    READY, PAID, CANCELLED,FAILED;
}
