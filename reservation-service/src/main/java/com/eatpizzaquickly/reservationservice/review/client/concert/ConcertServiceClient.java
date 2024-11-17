package com.eatpizzaquickly.reservationservice.review.client.concert;

import com.eatpizzaquickly.reservationservice.common.advice.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "concert-service")
public interface ConcertServiceClient {

    @GetMapping(value = "/api/v1/concerts/{concertId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<ConcertDetailResponse> getConcert(@PathVariable("concertId") Long concertId);
}
