package com.eatpizzaquickly.jariotte.domain.review.controller;

import com.eatpizzaquickly.jariotte.domain.common.advice.ApiResponse;
import com.eatpizzaquickly.jariotte.domain.common.dto.CustomUserDetails;
import com.eatpizzaquickly.jariotte.domain.review.dto.ReviewRequestDto;
import com.eatpizzaquickly.jariotte.domain.review.dto.ReviewResponseDto;
import com.eatpizzaquickly.jariotte.domain.review.dto.ReviewUpdateRequestDto;
import com.eatpizzaquickly.jariotte.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/concerts/{concertId}")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> createReview(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long concertId,
            @RequestBody ReviewRequestDto requestDto) {
        ReviewResponseDto result = reviewService.createReview(requestDto, concertId, authUser.getEmail());
        return ResponseEntity.ok(ApiResponse.success("댓글 등록 성공", result));
    }

    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse<Page<ReviewResponseDto>>> findReviews(
            @PathVariable Long concertId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ReviewResponseDto> result = reviewService.findReviews(concertId, page, size);
        return ResponseEntity.ok(ApiResponse.success("댓글 조회 성공", result));
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> updateReview(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long concertId,
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateRequestDto requestDto
    ) {
        ReviewResponseDto result = reviewService.updateReview(authUser.getEmail(), concertId, reviewId, requestDto);
        return ResponseEntity.ok(ApiResponse.success("댓글 수정 성공", result));
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReveiw(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(authUser.getEmail(), reviewId);
        return ResponseEntity.ok(ApiResponse.success("댓글 삭제 성공", reviewId));
    }
}
