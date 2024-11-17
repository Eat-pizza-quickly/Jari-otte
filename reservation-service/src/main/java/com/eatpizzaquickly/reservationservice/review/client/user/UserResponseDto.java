package com.eatpizzaquickly.reservationservice.review.client.user;

import lombok.Getter;

@Getter
public class UserResponseDto {
    private final String email;
    private final String nickname;

    public UserResponseDto(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
