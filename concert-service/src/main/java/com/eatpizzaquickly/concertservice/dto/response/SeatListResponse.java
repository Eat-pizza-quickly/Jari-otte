package com.eatpizzaquickly.concertservice.dto.response;

import com.eatpizzaquickly.concertservice.dto.SeatDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class SeatListResponse {
    List<SeatDto> seatDtoList;

    public static SeatListResponse of(List<SeatDto> seatDtoList) {
        return new SeatListResponse(seatDtoList);
    }
}
