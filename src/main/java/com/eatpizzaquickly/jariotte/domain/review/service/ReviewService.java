package com.eatpizzaquickly.jariotte.domain.review.service;

import com.eatpizzaquickly.jariotte.domain.common.exception.NotFoundException;
import com.eatpizzaquickly.jariotte.domain.concert.entity.Concert;
import com.eatpizzaquickly.jariotte.domain.review.dto.ReviewRequestDto;
import com.eatpizzaquickly.jariotte.domain.review.dto.ReviewResponseDto;
import com.eatpizzaquickly.jariotte.domain.review.entity.Review;
import com.eatpizzaquickly.jariotte.domain.review.repository.ReviewRepository;
import com.eatpizzaquickly.jariotte.domain.user.entity.User;
import com.eatpizzaquickly.jariotte.domain.user.exception.NotMatchException;
import com.eatpizzaquickly.jariotte.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ConcertRepository concertRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewResponseDto createReview(ReviewRequestDto requestDto, Long concertId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new NotMatchException("유저를 찾을 수 없습니다."));

        Review review = Review.builder()
                .rating(requestDto.getRating())
                .content(requestDto.getContent())
                .author(user)
                .build();

        Concert concert = concertRepository.findById(concertId).orElseThrow(() ->
                new NotFoundException("콘서트가 없습니다."));

        review.addConcert(concert);
        Review savedReview = reviewRepository.save(review);

        return ReviewResponseDto.from(savedReview);
    }

    public Page<ReviewResponseDto> findReviews(Long concertId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Review> reviews = reviewRepository.findAllByConcertId(concertId, pageable);
        return reviews.map(ReviewResponseDto::from);
    }
}
