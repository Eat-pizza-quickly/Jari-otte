package com.eatpizzaquickly.concertservice.controller;

import com.eatpizzaquickly.concertservice.dto.request.VenueCreateRequest;
import com.eatpizzaquickly.concertservice.dto.response.VenueDetailResponse;
import com.eatpizzaquickly.concertservice.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/venues")
@RestController
public class VenueController {

    private final VenueService venueService;

    // 관리자 권한 필요
    @PostMapping
    public ResponseEntity<VenueDetailResponse> createVenue(@RequestBody VenueCreateRequest venueCreateRequest) {
        VenueDetailResponse venueDetailResponse = venueService.saveVenue(venueCreateRequest);
        return ResponseEntity.ok(venueDetailResponse);
    }

    @GetMapping("/{venueId}")
    public ResponseEntity<VenueDetailResponse> getVenue(@PathVariable Long venueId) {
        VenueDetailResponse venueDetailResponse = venueService.findVenue(venueId);
        return ResponseEntity.ok(venueDetailResponse);
    }
}
