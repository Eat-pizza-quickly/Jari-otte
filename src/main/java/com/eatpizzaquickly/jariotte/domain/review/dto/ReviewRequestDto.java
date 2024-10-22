package com.eatpizzaquickly.jariotte.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {
    @NotEmpty
    private String content;
    @NotBlank
    @Size(max = 5,message = "별점은 5점을 넘을 수 없습니다.")
    private int rating;
}
