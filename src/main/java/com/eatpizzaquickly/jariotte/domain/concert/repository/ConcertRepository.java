package com.eatpizzaquickly.jariotte.domain.concert.repository;

import com.eatpizzaquickly.jariotte.domain.concert.entity.Category;
import com.eatpizzaquickly.jariotte.domain.concert.entity.Concert;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;

public interface ConcertRepository extends JpaRepository<Concert, Long> {
    Page<Concert> findAllByCategory(Category name, Pageable pageable);
}
