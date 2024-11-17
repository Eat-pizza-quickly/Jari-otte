package com.eatpizzaquickly.reservationservice.review.dto;

import com.eatpizzaquickly.reservationservice.review.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewResponseDto {
    private String content;
    private Integer rating;
    private Long userId;
    private String nickname;

    public ReviewResponseDto(String content, Integer rating, Long userId, String nickname) {
        this.content = content;
        this.rating = rating;
        this.userId = userId;
        this.nickname = nickname;
    }

    public static ReviewResponseDto from(Review savedReview) {
        return new ReviewResponseDto(
                savedReview.getContent(),
                savedReview.getRating(),
                savedReview.getUserId(),
                savedReview.getNickname()
        );
    }
}
