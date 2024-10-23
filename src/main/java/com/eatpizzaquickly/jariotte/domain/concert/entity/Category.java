package com.eatpizzaquickly.jariotte.domain.concert.entity;

import com.eatpizzaquickly.jariotte.domain.common.exception.BadRequestException;

import java.util.Arrays;

public enum Category {
    CONCERT, MUSICAL, THEATER, ESPORT;

    public static Category of(String category) {
        return Arrays.stream(Category.values())
                .filter(r -> r.name().equalsIgnoreCase(category))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("유효하지 않은 Category"));
    }
}
