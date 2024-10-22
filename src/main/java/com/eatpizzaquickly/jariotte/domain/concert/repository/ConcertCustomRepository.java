package com.eatpizzaquickly.jariotte.domain.concert.repository;

import com.eatpizzaquickly.jariotte.domain.concert.entity.Concert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConcertCustomRepository {
    Page<Concert> searchByTitleOrArtists(String keyword, Pageable pageable);
}
