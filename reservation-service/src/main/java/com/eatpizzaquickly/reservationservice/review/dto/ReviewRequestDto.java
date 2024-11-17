package com.eatpizzaquickly.reservationservice.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReviewRequestDto {
    @NotEmpty
    private String content;
    @NotBlank
    @Size(max = 5, message = "별점은 5점을 넘을 수 없습니다.")
    private Integer rating;
}
