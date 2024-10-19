package com.eatpizzaquickly.jariotte.user.controller;

import com.eatpizzaquickly.jariotte.domain.common.advice.ApiResponse;
import com.eatpizzaquickly.jariotte.domain.common.dto.CustomUserDetails;
import com.eatpizzaquickly.jariotte.user.dto.UserRequestDto;
import com.eatpizzaquickly.jariotte.user.dto.UserResponseDto;
import com.eatpizzaquickly.jariotte.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jari-otte/users")
public class UserController {

    private final UserService userService;


    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> signUp(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto user = userService.signUp(userRequestDto);
        return ResponseEntity.ok(ApiResponse.success("회원가입 성공 ", user));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody UserRequestDto userRequestDto) {
        String token = userService.login(userRequestDto);
        return ResponseEntity.ok(ApiResponse.success("로그인 성공",token));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUsers(@AuthenticationPrincipal CustomUserDetails authUser) {
        UserResponseDto user = userService.MyPage(authUser.getEmail());
        return ResponseEntity.ok(ApiResponse.success("마이페이지 조회 성공",user));
    }

    @PatchMapping("/my")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(@AuthenticationPrincipal CustomUserDetails authUser,
                                                                   @Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto user = userService.updateUser(authUser.getEmail(), userRequestDto);
        return ResponseEntity.ok(ApiResponse.success("수정 성공 ",user));
    }

    @DeleteMapping("/my")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@AuthenticationPrincipal CustomUserDetails authUser,
                                                          @Valid @RequestBody UserRequestDto userRequestDto) {
         userService.deleteUser(authUser.getEmail(),userRequestDto.getPassword());
         return ResponseEntity.ok(ApiResponse.success("탈퇴성공"));


    }

}
