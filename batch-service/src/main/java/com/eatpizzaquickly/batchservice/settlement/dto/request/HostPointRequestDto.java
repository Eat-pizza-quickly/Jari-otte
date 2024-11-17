package com.eatpizzaquickly.batchservice.settlement.dto.request;


import com.eatpizzaquickly.batchservice.settlement.entity.HostPoint;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HostPointRequestDto {
    private Long hostId;
    private Long points;
    @JsonIgnore
    private Long payId;

    public static HostPointRequestDto from(HostPoint hostPoint) {
        return new HostPointRequestDto(
                hostPoint.getHostId(),
                hostPoint.getPoints(),
                hostPoint.getPayId());
    }
}
