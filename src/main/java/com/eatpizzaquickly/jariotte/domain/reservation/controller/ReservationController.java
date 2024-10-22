package com.eatpizzaquickly.jariotte.domain.reservation.controller;

import com.eatpizzaquickly.jariotte.domain.reservation.dto.request.PostReservationRequest;
import com.eatpizzaquickly.jariotte.domain.reservation.dto.response.PostReservationResponse;
import com.eatpizzaquickly.jariotte.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/concerts/{concertId}/seats/{seatId}")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/reservations")
    public PostReservationResponse createReservation(
            @PathVariable Long concertId,
            @PathVariable Long seatId,
            @RequestBody PostReservationRequest request
    ) {
        return reservationService.createReservation(concertId, seatId, request);
    }
}
