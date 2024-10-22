package com.eatpizzaquickly.jariotte.domain.seat.dto.resopnse;

import com.eatpizzaquickly.jariotte.domain.seat.entity.Seat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SeatDto {
    private Long id;
    private Integer seatNumber;
    private Boolean isReserved;

    public static SeatDto from(Seat seat) {
        return new SeatDto(seat.getId(), seat.getSeatNumber(), seat.isReserved());
    }
}
