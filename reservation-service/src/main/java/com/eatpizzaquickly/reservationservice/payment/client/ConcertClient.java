package com.eatpizzaquickly.reservationservice.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "concert-service")
public interface ConcertClient {

    @GetMapping("/api/v1/concerts/{concertId}/host")
    Long findHostIdByConcertId(@PathVariable(name = "concertId") Long concertId);
}
