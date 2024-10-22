package com.eatpizzaquickly.jariotte.domain.concert.dto.reqeuest;

import lombok.Getter;

@Getter
public class VenueCreateRequest {
    private String venueName;
    private String location;
    private Integer seatCount;
}
