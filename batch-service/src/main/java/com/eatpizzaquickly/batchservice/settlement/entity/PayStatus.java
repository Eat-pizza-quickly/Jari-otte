package com.eatpizzaquickly.batchservice.settlement.entity;


import lombok.Getter;

@Getter
public enum PayStatus {
    READY, PAID, CANCELLED, FAILED;
}
