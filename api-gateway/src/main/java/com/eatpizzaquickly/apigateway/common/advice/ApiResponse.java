package com.eatpizzaquickly.apigateway.common.advice;

import lombok.Getter;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;

    @Builder
    public ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // 기존의 생성자들도 유지
    public ApiResponse(String message, T data) {
        this.status = "success";
        this.message = message;
        this.data = data;
    }

    public ApiResponse(String message) {
        this.status = "success";
        this.message = message;
    }

    // 성공 응답을 위한 static 메서드들
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, data);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(message);
    }

    // 실패 응답을 위한 static 메서드 추가
    public static <T> ApiResponse<T> error(String status, String message, T data) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String status, String message) {
        return error(status, message, null);
    }
}