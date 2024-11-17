package com.eatpizzaquickly.reservationservice.review.entity;

import com.eatpizzaquickly.reservationservice.review.dto.ReviewRequestDto;
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
    private String nickname;

    @Column(name = "user_email")
    private String userEmail;

    // 콘서트 ID(FK)
    @Column(name = "concert_id")
    private Long concertId;

    // 유저 ID (FK)
    @Column(name = "user_id")
    private Long userId;

    @Builder
    public Review(Integer rating, String content, String nickname, String userEmail, Long userId, Long concertId) {
        this.rating = rating;
        this.content = content;
        this.nickname = nickname;
        this.userEmail = userEmail;
        this.userId = userId;
        this.concertId = concertId;
    }

    public void update(ReviewRequestDto requestDto) {
        if (requestDto.getRating() != null) {
            this.rating = requestDto.getRating();
        }
        this.content = requestDto.getContent();
    }
}
