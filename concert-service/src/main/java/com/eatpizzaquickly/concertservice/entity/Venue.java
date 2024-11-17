package com.eatpizzaquickly.concertservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Venue {

    @Column(name = "venue_Id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String venueName;

    private String location;

    private Integer seatCount;

    @Builder
    private Venue(String venueName, String location, Integer seatCount) {
        this.venueName = venueName;
        this.location = location;
        this.seatCount = seatCount;
    }
}
