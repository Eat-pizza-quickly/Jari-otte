package com.eatpizzaquickly.couponservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class UserResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String userRole;  // 필드 추가


    public UserResponseDto(Long id, String email, String nickname, String userRole) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.userRole = userRole;
    }
}
