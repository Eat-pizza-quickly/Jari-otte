package com.eatpizzaquickly.reservationservice.review.service;

import com.eatpizzaquickly.reservationservice.common.advice.ApiResponse;
import com.eatpizzaquickly.reservationservice.common.exception.NotFoundException;
import com.eatpizzaquickly.reservationservice.common.exception.UnauthorizedException;
import com.eatpizzaquickly.reservationservice.reservation.entity.Reservation;
import com.eatpizzaquickly.reservationservice.reservation.entity.ReservationStatus;
import com.eatpizzaquickly.reservationservice.reservation.repository.ReservationRepository;
import com.eatpizzaquickly.reservationservice.review.client.concert.ConcertDetailResponse;
import com.eatpizzaquickly.reservationservice.review.client.concert.ConcertServiceClient;
import com.eatpizzaquickly.reservationservice.review.client.user.UserResponseDto;
import com.eatpizzaquickly.reservationservice.review.client.user.UserServiceClient;
import com.eatpizzaquickly.reservationservice.review.dto.ReviewRequestDto;
import com.eatpizzaquickly.reservationservice.review.dto.ReviewResponseDto;
import com.eatpizzaquickly.reservationservice.review.entity.Review;
import com.eatpizzaquickly.reservationservice.review.exception.ReviewExceptionEnum;
import com.eatpizzaquickly.reservationservice.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final UserServiceClient userServiceClient;
    private final ConcertServiceClient concertServiceClient;
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    /* 댓글 생성 */
    @Transactional
    public ReviewResponseDto createReview(Long userId, Long concertId, ReviewRequestDto requestDto) {
        // 유저 ID로 유저 정보 요청
        UserResponseDto userResponseDto = userServiceClientUser(userId);
        // 유저 검증
        userException(userResponseDto);

        // 콘서트 아이디 요청
        ApiResponse<ConcertDetailResponse> concertDetailResponse = concertServiceClientConcert(concertId);
        // 검증
        concertException(concertDetailResponse);

        // 예매 데이터 가져오기
        Reservation reservation = reservationOrElseThrow(concertDetailResponse.getData().getConcertId());
        // 티켓 결제 됐는지 확인
        reservationException(reservation, ReservationStatus.CONFIRMED);

        Review review = Review.builder()
                .rating(requestDto.getRating())
                .content(requestDto.getContent())
                .nickname(userResponseDto.getNickname())
                .userEmail(userResponseDto.getEmail())
                .userId(userId)
                .concertId(concertDetailResponse.getData().getConcertId())
                .build();

        Review savedReview = reviewRepository.save(review);

        return ReviewResponseDto.from(savedReview);
    }

    /* 댓글 조회 */
    public Page<ReviewResponseDto> findReviews(Long concertId, int page, int size) {
        // 콘서트 아이디 요청
        ApiResponse<ConcertDetailResponse> concertDetailResponse = concertServiceClientConcert(concertId);
        // 검증
        concertException(concertDetailResponse);

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Review> reviews = reviewRepository.findAllByConcertId(concertDetailResponse.getData().getConcertId(), pageable);
        return reviews.map(ReviewResponseDto::from);
    }

    /* 댓글 수정 */
    @Transactional
    public ReviewResponseDto updateReview(Long userId, Long concertId, Long reviewId, ReviewRequestDto requestDto) {
        // 유저 ID로 유저 정보 요청
        UserResponseDto userResponseDto = userServiceClientUser(userId);
        // 유저 검증
        userException(userResponseDto);

        // 콘서트 아이디 요청
        ApiResponse<ConcertDetailResponse> concertDetailResponse = concertServiceClientConcert(concertId);
        // 검증
        concertException(concertDetailResponse);

        // 리뷰 찾기
        Review review = findReview(reviewId);
        // 리뷰 작성 이메일 가져오기
        String owner = review.getUserEmail();
        // 본인이 작성했는지 확인
        reviewUpdateException(owner, userResponseDto, ReviewExceptionEnum.REVIEW_UPDATE_ERROR.getMessage());

        review.update(requestDto);
        Review updatedReview = reviewRepository.save(review);
        return ReviewResponseDto.from(updatedReview);
    }

    /* 댓글 삭제 */
    @Transactional
    public void deleteReview(Long userId, Long concertId, Long reviewId) {
        // 유저 ID로 유저 정보 요청
        UserResponseDto userResponseDto = userServiceClientUser(userId);
        // 검증
        userException(userResponseDto);

        // 콘서트 아이디 요청
        ApiResponse<ConcertDetailResponse> concertDetailResponse = concertServiceClientConcert(concertId);
        // 검증
        concertException(concertDetailResponse);

        // 리뷰 찾기
        Review review = findReview(reviewId);
        // 리뷰 작성 이메일 가져오기
        String owner = review.getUserEmail();
        // 본인이 작성했는지 확인
        reviewUpdateException(owner, userResponseDto, ReviewExceptionEnum.REVIEW_DELETE_ERROR.getMessage());

        reviewRepository.delete(review);
    }

    /* 유저 요청 */
    private UserResponseDto userServiceClientUser(Long userId) {
        return userServiceClient.getUserById(userId);
    }

    /* 유저 검증 */
    private void userException(UserResponseDto userResponseDto) {
        // 검증
        if (userResponseDto.getEmail() == null || !userResponseDto.getEmail().isEmpty()) {
            throw new NotFoundException("해당 유저 정보가 없습니다.");
        }
    }

    /* 콘서트 요청 */
    private ApiResponse<ConcertDetailResponse> concertServiceClientConcert(Long concertId) {
        return concertServiceClient.getConcert(concertId);
    }

    /* 콘서트 검증 */
    private void concertException(ApiResponse<ConcertDetailResponse> concertDetailResponse) {
        // 검증
        if (concertDetailResponse == null) {
            throw new NotFoundException("해당 콘서트가 존재하지 않습니다.");
        }
    }

    /* 예매 데이터 가져오기 */
    private Reservation reservationOrElseThrow(Long concertId) {
        return reservationRepository.findByConcertId(concertId)
                .orElseThrow(() -> new NotFoundException("해당 예약이 존재하지 않습니다."));
    }

    /* 티켓 결제 됐는지 확인 */
    private void reservationException(Reservation reservation, ReservationStatus status) {
        // 티켓 결제 됐는지 확인
        if (!reservation.getStatus().equals(status)) {
            throw new UnauthorizedException("리뷰를 입력할 권한이 없습니다.");
        }
    }

    /* 리뷰 찾기 */
    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(
                () -> new NotFoundException("리뷰를 찾지 못했습니다."));
    }

    /* 리뷰 수정 권한 */
    private void reviewUpdateException(String owner, UserResponseDto userResponseDto, String msg) {
        if (!owner.equals(userResponseDto.getEmail())) {
            throw new UnauthorizedException(msg);
        }
    }
}
