package com.eatpizzaquickly.userservice.dto;
import com.eatpizzaquickly.userservice.entity.User;
import com.eatpizzaquickly.userservice.entity.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {

    private Long id;
    private String email;

    private String nickname;

    private UserRole userRole;

    // password는 JSON 응답에서 제외되므로 제거할 수 있음
    @JsonIgnore
    private String password;

    public UserResponseDto(Long id,String email, String nickname, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.userRole = userRole;
    }

    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getUserRole()
        );
    }
}
