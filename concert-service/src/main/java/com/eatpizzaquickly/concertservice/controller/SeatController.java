package com.eatpizzaquickly.concertservice.controller;

import com.eatpizzaquickly.concertservice.dto.request.SeatReservationRequest;
import com.eatpizzaquickly.concertservice.dto.response.ApiResponse;
import com.eatpizzaquickly.concertservice.dto.response.SeatListResponse;
import com.eatpizzaquickly.concertservice.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/concerts")
@RestController
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/{concertId}/seats")
    public ResponseEntity<ApiResponse<SeatListResponse>> getSeatList(@PathVariable Long concertId,
                                                                     @RequestHeader("X-Authenticated-User") Long userId) {
        SeatListResponse seatListResponse = seatService.findSeatList(concertId, userId);
        return ResponseEntity.ok(ApiResponse.success("좌석 조회 성공", seatListResponse));
    }

    @PostMapping("/{concertId}/seats/{seatId}")
    public ResponseEntity<ApiResponse<Void>> reserveSeat(@PathVariable Long concertId,
                                                         @PathVariable Long seatId,
                                                         @RequestHeader("X-Authenticated-User") Long userId,
                                                         @RequestBody SeatReservationRequest seatReservationRequest) {
        seatService.reserveSeat(userId, concertId, seatId, seatReservationRequest);
        return ResponseEntity.ok(ApiResponse.success("좌석 예매 성공"));
    }
}
