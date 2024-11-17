package com.eatpizzaquickly.concertservice.entity;

import com.eatpizzaquickly.concertservice.util.StringListConvertor;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.List;

@SQLDelete(sql = "UPDATE concert SET deleted = true WHERE concert_id = ?")
@SQLRestriction("deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Concert {

    @Column(name = "concert_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    private String thumbnailUrl;

    private int avgRating;

    @Column(nullable = false)
    private int seatCount;

    @Enumerated(value = EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Convert(converter = StringListConvertor.class)
    @Column(nullable = false)
    private List<String> artists;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    private Long hostId;

    private Boolean deleted = false;

    @Builder
    private Concert(String title,
                    String description,
                    String thumbnailUrl,
                    int avgRating,
                    int seatCount,
                    Category category,
                    LocalDateTime startDate,
                    LocalDateTime endDate,
                    List<String> artists,
                    Venue venue,
                    Long hostId) {
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.avgRating = avgRating;
        this.seatCount = seatCount;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.artists = artists;
        this.venue = venue;
        this.hostId = hostId;
    }

    public void decreaseSeatCount() {
        this.seatCount--;
    }

    public void updateSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }
}
