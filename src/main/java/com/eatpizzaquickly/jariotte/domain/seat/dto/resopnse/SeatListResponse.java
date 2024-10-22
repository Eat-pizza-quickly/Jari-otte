package com.eatpizzaquickly.jariotte.domain.seat.dto.resopnse;

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
