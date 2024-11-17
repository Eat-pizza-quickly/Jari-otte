package com.eatpizzaquickly.reservationservice.review.repository;

import com.eatpizzaquickly.reservationservice.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByConcertId(Long concertId, Pageable pageable);
}
