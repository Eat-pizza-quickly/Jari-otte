package com.eatpizzaquickly.jariotte.domain.reservation.controller;

import com.eatpizzaquickly.jariotte.domain.common.advice.ApiResponse;
import com.eatpizzaquickly.jariotte.domain.common.dto.CustomUserDetails;
import com.eatpizzaquickly.jariotte.domain.reservation.dto.request.PostReservationRequest;
import com.eatpizzaquickly.jariotte.domain.reservation.dto.response.PostReservationResponse;
import com.eatpizzaquickly.jariotte.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostReservationResponse>> createReservation(
            @PathVariable Long concertId,
            @PathVariable Long seatId,
            @RequestBody PostReservationRequest request
    ) {
        PostReservationResponse response = reservationService.createReservation(concertId, seatId, request);
        return ResponseEntity.ok(ApiResponse.success("예약 성공", response));
    }

    @PostMapping("/reservations/{reservationId}/cancel")
    public ResponseEntity<ApiResponse> cancelReservation(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long reservationId
    ) {
        reservationService.cancelReservation(authUser.getEmail(), reservationId);
        return ResponseEntity.ok(ApiResponse.success("예약 취소", reservationId));
    }
}
