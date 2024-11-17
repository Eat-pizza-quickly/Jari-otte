package com.eatpizzaquickly.concertservice.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ConcertCreateRequest {
    private String title;
    private String location;
    private String description;
    private List<String> artists;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String category;
    private Long venueId;
    private String thumbnailUrl;
}
