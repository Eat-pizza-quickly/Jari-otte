package com.eatpizzaquickly.jariotte.domain.concert.controller;

import com.eatpizzaquickly.jariotte.domain.common.advice.ApiResponse;
import com.eatpizzaquickly.jariotte.domain.concert.dto.ConcertSimpleDto;
import com.eatpizzaquickly.jariotte.domain.concert.dto.reqeuest.ConcertCreateRequest;
import com.eatpizzaquickly.jariotte.domain.concert.dto.response.ConcertDetailResponse;
import com.eatpizzaquickly.jariotte.domain.concert.dto.response.ConcertListResponse;
import com.eatpizzaquickly.jariotte.domain.concert.service.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/concerts")
@RestController
public class ConcertController {

    private final ConcertService concertService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ConcertDetailResponse> createConcert(@RequestBody ConcertCreateRequest concertCreateRequest) {
        ConcertDetailResponse concertDetailResponse = concertService.saveConcert(concertCreateRequest);
        return ResponseEntity.ok(concertDetailResponse);
    }


    @GetMapping("/{concertId}")
    public ResponseEntity<ConcertDetailResponse> getConcert(@PathVariable Long concertId) {
        ConcertDetailResponse concertDetailResponse = concertService.findConcert(concertId);
        return ResponseEntity.ok(concertDetailResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<ConcertListResponse> getConcertList(@RequestParam(required = false) String keyword, @PageableDefault Pageable pageable) {
        ConcertListResponse concertListResponse = concertService.searchConcert(keyword, pageable);
        return ResponseEntity.ok(concertListResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{concertId}")
    public ResponseEntity<Void> deleteConcert(@PathVariable Long concertId) {
        concertService.deleteConcert(concertId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ConcertSimpleDto>>> searchByCategory(
            @RequestParam(name = "category") String category,
            @PageableDefault Pageable pageable
    ) {
        Page<ConcertSimpleDto> concertSimpleDtos = concertService.searchByCategory(category, pageable);
        return ResponseEntity.ok(ApiResponse.success("조회 성공", concertSimpleDtos));
    }
}
