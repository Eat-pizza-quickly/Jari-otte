package com.eatpizzaquickly.concertservice.client.reservation;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "reservation-service")
public interface ReservationServiceClient {

    @PostMapping(value = "/api/v1/reservations", consumes = MediaType.APPLICATION_JSON_VALUE)
    PostReservationResponse createReservation(@RequestBody PostReservationRequest request);

}
