package com.eatpizzaquickly.jariotte.domain.concert.dto;

import com.eatpizzaquickly.jariotte.domain.concert.entity.Concert;
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
