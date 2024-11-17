package com.eatpizzaquickly.concertservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class QueueResponse {
    private final Integer position;
    private final String url;
    private final boolean isReservation;
}
