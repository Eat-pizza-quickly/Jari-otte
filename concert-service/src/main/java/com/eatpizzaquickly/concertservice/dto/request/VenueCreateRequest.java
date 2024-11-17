package com.eatpizzaquickly.concertservice.dto.request;

import lombok.Getter;

@Getter
public class VenueCreateRequest {
    private String venueName;
    private String location;
    private Integer seatCount;
}
