package com.eatpizzaquickly.jariotte.domain.review.entity;

import com.eatpizzaquickly.jariotte.domain.concert.entity.Concert;
import com.eatpizzaquickly.jariotte.domain.review.dto.ReviewUpdateRequestDto;
import com.eatpizzaquickly.jariotte.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer rating;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id", nullable = false)
    private Concert concert;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Builder
    public Review(Integer rating, String content, User author) {
        this.rating = rating;
        this.content = content;
        this.author = author;
    }

    public void addConcert(Concert concert) {
        this.concert = concert;
    }

    public void update(ReviewUpdateRequestDto requestDto) {
        if (requestDto.getRating() != null) {
            this.rating = requestDto.getRating();
        }
        this.content = requestDto.getContent();
    }
}
