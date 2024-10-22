package com.eatpizzaquickly.jariotte.domain.review.dto;

import com.eatpizzaquickly.jariotte.domain.review.entity.Review;
import com.eatpizzaquickly.jariotte.domain.user.dto.UserResponseDto;
import com.eatpizzaquickly.jariotte.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewResponseDto {
    private String content;
    private int rating;
    private String author;

    public ReviewResponseDto(String content, int rating, String author) {
        this.content = content;
        this.rating = rating;
        this.author = author;
    }

    public static ReviewResponseDto from(Review savedReview) {
        return new ReviewResponseDto(
                savedReview.getContent(),
                savedReview.getRating(),
                savedReview.getAuthor().getNickname()
        );
    }
}
