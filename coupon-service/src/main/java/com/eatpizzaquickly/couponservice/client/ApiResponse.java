package com.eatpizzaquickly.couponservice.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor  // Jackson 역직렬화를 위한 기본 생성자
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;

    // Jackson 역직렬화를 위한 생성자
    @JsonCreator
    public ApiResponse(
            @JsonProperty("status") String status,
            @JsonProperty("message") String message,
            @JsonProperty("data") T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // 기존 생성자들
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
}