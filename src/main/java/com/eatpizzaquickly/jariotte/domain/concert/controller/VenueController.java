package com.eatpizzaquickly.jariotte.domain.concert.controller;

import com.eatpizzaquickly.jariotte.domain.concert.dto.reqeuest.VenueCreateRequest;
import com.eatpizzaquickly.jariotte.domain.concert.dto.response.VenueDetailResponse;
import com.eatpizzaquickly.jariotte.domain.concert.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/venue")
@RestController
public class VenueController {

    private final VenueService venueService;

    @PreAuthorize("hasRole('ADMIN')")
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
