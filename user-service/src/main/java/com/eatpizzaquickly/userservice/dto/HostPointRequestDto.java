package com.eatpizzaquickly.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HostPointRequestDto {
    private Long hostId;
    private Long points;

}
