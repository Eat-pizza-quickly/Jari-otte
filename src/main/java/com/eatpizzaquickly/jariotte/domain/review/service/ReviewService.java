package com.eatpizzaquickly.jariotte.domain.review.service;

import com.eatpizzaquickly.jariotte.domain.common.exception.NotFoundException;
import com.eatpizzaquickly.jariotte.domain.common.exception.UnauthorizedException;
import com.eatpizzaquickly.jariotte.domain.concert.entity.Concert;
import com.eatpizzaquickly.jariotte.domain.concert.repository.ConcertRepository;
import com.eatpizzaquickly.jariotte.domain.review.dto.ReviewRequestDto;
import com.eatpizzaquickly.jariotte.domain.review.dto.ReviewResponseDto;
import com.eatpizzaquickly.jariotte.domain.review.dto.ReviewUpdateRequestDto;
import com.eatpizzaquickly.jariotte.domain.review.entity.Review;
import com.eatpizzaquickly.jariotte.domain.review.repository.ReviewRepository;
import com.eatpizzaquickly.jariotte.domain.user.entity.User;
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
                new NotFoundException("유저를 찾을 수 없습니다."));

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

    @Transactional
    public ReviewResponseDto updateReview(String authUser, Long concertId, Long reviewId, ReviewUpdateRequestDto requestDto) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() ->
                new NotFoundException("리뷰를 찾을 수 없습니다."));
        String owner = review.getAuthor().getEmail();
        if (!owner.equals(authUser)) {
            throw new UnauthorizedException("리뷰 수정 권한이 없습니다.");
        }
        review.update(requestDto);
        Review updatedReview = reviewRepository.save(review);
        return ReviewResponseDto.from(updatedReview);
    }

    @Transactional
    public void deleteReview(String authUser, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() ->
                new NotFoundException("리뷰를 찾을 수 없습니다."));
        String owner = review.getAuthor().getEmail();
        if (!owner.equals(authUser)) {
            throw new UnauthorizedException("리뷰 수정 권한이 없습니다.");
        }
        reviewRepository.delete(review);
    }
}
