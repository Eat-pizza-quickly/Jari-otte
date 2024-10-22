package com.eatpizzaquickly.jariotte.domain.review.repository;

import com.eatpizzaquickly.jariotte.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    Page<Review> findAllByConcertId(Long concertId, Pageable pageable);
}
