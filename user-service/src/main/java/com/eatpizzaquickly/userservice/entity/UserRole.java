package com.eatpizzaquickly.userservice.entity;

import jakarta.ws.rs.NotFoundException;

import java.util.Arrays;

public enum UserRole {
    ADMIN, HOST, USER;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("유효하지 않은 UerRole"));
    }
}