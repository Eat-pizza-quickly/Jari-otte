package com.eatpizzaquickly.concertservice.dto.response;

import com.eatpizzaquickly.concertservice.dto.ConcertSimpleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ConcertListResponse {
    private List<ConcertSimpleDto> concertSimpleDtoList;

    public static ConcertListResponse of(List<ConcertSimpleDto> concertSimpleDtoList) {
        return new ConcertListResponse(concertSimpleDtoList);
    }
}
