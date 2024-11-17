package com.eatpizzaquickly.concertservice.controller;

import com.eatpizzaquickly.concertservice.dto.response.ApiResponse;
import com.eatpizzaquickly.concertservice.dto.response.SearchAutocompleteDto;
import com.eatpizzaquickly.concertservice.dto.response.SearchConcertListDto;
import com.eatpizzaquickly.concertservice.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/concerts/search")
@RequiredArgsConstructor
public class SearchTermController {
    private final SearchService searchService;

    /* 자동 완성 목록 결과 반환 */
    @GetMapping("/autocomplete")
    public ResponseEntity<ApiResponse<SearchAutocompleteDto>> autocomplete(@RequestParam String query) {
        SearchAutocompleteDto autocompleteDto = searchService.autocomplete(query);
        return ResponseEntity.ok(ApiResponse.success("자동완성 리스트 조회 성공", autocompleteDto));
    }

    /* 멀티필드검색 및 오타 허용 검색 (공연이름, 아티스트 이름) */
    @GetMapping
    public ResponseEntity<ApiResponse<SearchConcertListDto>> searchConcerts(
            @RequestParam String query,
            @RequestParam(name = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,   // 시작 날짜 (선택적)
            @RequestParam(name = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,     // 종료 날짜 (선택적)
            @PageableDefault Pageable pageable
    ) {
        SearchConcertListDto searchConcertListDto = searchService.searchConcerts(query, startDate, endDate, pageable);
        return ResponseEntity.ok(ApiResponse.success("공연 리스트 조회 성공", searchConcertListDto));
    }
}
