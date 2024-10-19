package com.eatpizzaquickly.jariotte.user.entity;

import com.eatpizzaquickly.jariotte.domain.common.exception.NotFoundException;

import java.util.Arrays;

public enum UserRole {
    ADMIN,USER;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("유효하지 않은 UerRole"));
    }
}
