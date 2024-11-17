package com.eatpizzaquickly.reservationservice.payment.dto.request;

import com.eatpizzaquickly.reservationservice.payment.entity.HostPoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HostPointRequestDto {
    private Long hostId;
    private Long points;

    public static HostPointRequestDto from(HostPoint hostPoint) {
        return new HostPointRequestDto(
                hostPoint.getHostId(),
                hostPoint.getPoints());
    }
}
