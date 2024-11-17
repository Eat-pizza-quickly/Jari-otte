package com.eatpizzaquickly.concertservice.dto;

import com.eatpizzaquickly.concertservice.entity.Concert;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ConcertSimpleDto {
    private Long concertId;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public static ConcertSimpleDto from(Concert concert) {
        return new ConcertSimpleDto(concert.getId(), concert.getTitle(), concert.getStartDate(), concert.getEndDate());
    }
}
