package com.eatpizzaquickly.concertservice.dto.response;

import com.eatpizzaquickly.concertservice.entity.Concert;
import com.eatpizzaquickly.concertservice.entity.Venue;
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
    private Integer seatCount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public static ConcertDetailResponse from(Concert concert, Venue venue, int seatCount) {
        return new ConcertDetailResponse(
                concert.getId(),
                concert.getTitle(),
                venue.getLocation(),
                concert.getDescription(),
                seatCount,
                concert.getStartDate(),
                concert.getEndDate());
    }
}
