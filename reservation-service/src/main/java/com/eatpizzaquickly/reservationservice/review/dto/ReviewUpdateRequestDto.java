package com.eatpizzaquickly.reservationservice.review.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateRequestDto {
    private String content;

    @Size(max = 5, message = "별점은 5점을 넘을 수 없습니다.")
    private Integer reating;
}
