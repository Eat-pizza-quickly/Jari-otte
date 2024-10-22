package com.eatpizzaquickly.jariotte.domain.seat.controller;

import com.eatpizzaquickly.jariotte.domain.seat.dto.resopnse.SeatListResponse;
import com.eatpizzaquickly.jariotte.domain.seat.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/seats")
@RequiredArgsConstructor
@RestController
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/{concertId}")
    public ResponseEntity<SeatListResponse> getSeatList(@PathVariable Long concertId) {
        SeatListResponse seatListResponse = seatService.findSeatList(concertId);
        return ResponseEntity.ok(seatListResponse);
    }

}
