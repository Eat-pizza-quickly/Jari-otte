package com.eatpizzaquickly.reservationservice.payment.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class UserResponseDto {
    private Long id;
    private String email;
    private String nickname;

    public UserResponseDto(Long id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }
}
