package com.eatpizzaquickly.jariotte.domain.concert.dto.response;

import com.eatpizzaquickly.jariotte.domain.concert.entity.Venue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class VenueDetailResponse {
    private String location;
    private Integer seatCount;

    public static VenueDetailResponse from(Venue venue) {
        return new VenueDetailResponse(venue.getLocation(), venue.getSeatCount());
    }
}
