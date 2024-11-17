package com.eatpizzaquickly.reservationservice.review.client.concert;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ConcertDetailResponse {
    private final Long concertId;
    private final String title;
    private final String location;
    private final String description;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public ConcertDetailResponse(Long concertId, String title, String location, String description, LocalDateTime startDate, LocalDateTime endDate) {
        this.concertId = concertId;
        this.title = title;
        this.location = location;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
