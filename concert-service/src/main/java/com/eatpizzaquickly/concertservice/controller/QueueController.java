package com.eatpizzaquickly.concertservice.controller;

import com.eatpizzaquickly.concertservice.dto.response.ApiResponse;
import com.eatpizzaquickly.concertservice.dto.response.QueueResponse;
import com.eatpizzaquickly.concertservice.repository.ConcertRedisRepository;
import com.eatpizzaquickly.concertservice.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/concerts")
@RestController
public class QueueController {

    private final QueueService queueService;

    @GetMapping("/{concertId}/queue")
    public ResponseEntity<ApiResponse<QueueResponse>> getQueuePosition(@PathVariable Long concertId,
                                                                       @RequestHeader("X-Authenticated-User") Long userId) {
        QueueResponse queueResponse = queueService.findPosition(concertId, userId);
        return ResponseEntity.ok(ApiResponse.success("대기열 조회 성공", queueResponse));
    }
}
