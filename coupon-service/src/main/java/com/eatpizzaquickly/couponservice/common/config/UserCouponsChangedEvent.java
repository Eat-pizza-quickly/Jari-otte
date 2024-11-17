package com.eatpizzaquickly.couponservice.common.config;

public class UserCouponsChangedEvent {
    private final Long userId;

    public UserCouponsChangedEvent(Object source, Long userId) {
        super();
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
