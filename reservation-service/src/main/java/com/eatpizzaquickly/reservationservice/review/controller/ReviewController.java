package com.eatpizzaquickly.reservationservice.review.controller;

import com.eatpizzaquickly.reservationservice.common.advice.ApiResponse;
import com.eatpizzaquickly.reservationservice.review.dto.ReviewRequestDto;
import com.eatpizzaquickly.reservationservice.review.dto.ReviewResponseDto;
import com.eatpizzaquickly.reservationservice.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    /* 댓글 추가 */
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponseDto>> createReview(
            @RequestHeader("X-Authenticated-User") Long userId,
            @RequestParam Long concertId,
            @RequestBody ReviewRequestDto requestDto
    ) {
        ReviewResponseDto result = reviewService.createReview(userId, concertId, requestDto);
        return ResponseEntity.ok(ApiResponse.success("댓글 등록 성공", result));
    }

    /* 댓글 조회 */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ReviewResponseDto>>> findReviews(
            @RequestParam Long concertId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ReviewResponseDto> result = reviewService.findReviews(concertId, page, size);
        return ResponseEntity.ok(ApiResponse.success("댓글 조회 성공", result));
    }

    /* 댓글 수정 */
    @PutMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> updateReview(
            @RequestHeader("X-Authenticated-User") Long userId,
            @RequestParam Long concertId,
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDto requestDto
    ) {
        ReviewResponseDto result = reviewService.updateReview(userId, concertId, reviewId, requestDto);
        return ResponseEntity.ok(ApiResponse.success("댓글 수정 성공", result));
    }

    /* 댓글 삭제 */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(
            @RequestHeader("X-Authenticated-User") Long userId,
            @RequestParam Long concertId,
            @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(userId, concertId, reviewId);
        return ResponseEntity.ok(ApiResponse.success("댓글 삭제 성공", reviewId));
    }
}
