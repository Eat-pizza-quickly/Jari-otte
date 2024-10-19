package com.eatpizzaquickly.jariotte.domain.payment.entity;

import lombok.Getter;

@Getter
public enum PayStatus {
    READY, PAID, FAILED, CANCELLED;
}
