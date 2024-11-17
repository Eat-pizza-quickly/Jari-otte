package com.eatpizzaquickly.concertservice.dto.response;

import com.eatpizzaquickly.concertservice.dto.ConcertSimpleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PopularConcertResponse {
    List<ConcertSimpleDto> concertSimpleDtoList;

    public static PopularConcertResponse of(List<ConcertSimpleDto> concertSimpleDtoList) {
        return new PopularConcertResponse(concertSimpleDtoList);
    }
}
