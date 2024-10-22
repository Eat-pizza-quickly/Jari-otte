package com.eatpizzaquickly.jariotte.domain.user.controller;

import com.eatpizzaquickly.jariotte.domain.common.advice.ApiResponse;
import com.eatpizzaquickly.jariotte.domain.common.config.JwtUtils;
import com.eatpizzaquickly.jariotte.domain.common.dto.CustomUserDetails;
import com.eatpizzaquickly.jariotte.domain.user.dto.UserRequestDto;
import com.eatpizzaquickly.jariotte.domain.user.dto.UserResponseDto;
import com.eatpizzaquickly.jariotte.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;


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

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String token) {
        userService.logout(token);
        return ResponseEntity.ok(new ApiResponse<>("로그아웃 성공"));
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
