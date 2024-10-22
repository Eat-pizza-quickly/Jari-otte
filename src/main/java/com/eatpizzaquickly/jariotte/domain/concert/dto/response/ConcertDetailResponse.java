package com.eatpizzaquickly.jariotte.domain.concert.dto.response;

import com.eatpizzaquickly.jariotte.domain.concert.entity.Concert;
import com.eatpizzaquickly.jariotte.domain.concert.entity.Venue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ConcertDetailResponse {
    private Long concertId;
    private String title;
    private String location;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public static ConcertDetailResponse from(Concert concert, Venue venue) {
        return new ConcertDetailResponse(
                concert.getId(),
                concert.getTitle(),
                venue.getLocation(),
                concert.getDescription(),
                concert.getStartDate(),
                concert.getEndDate());
    }
}
