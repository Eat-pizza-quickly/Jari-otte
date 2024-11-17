package com.eatpizzaquickly.concertservice.repository;

import com.eatpizzaquickly.concertservice.entity.Category;
import com.eatpizzaquickly.concertservice.entity.Concert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ConcertRepository extends JpaRepository<Concert, Long>, ConcertCustomRepository {

    @Query("SELECT c FROM Concert c JOIN FETCH c.venue WHERE c.id = :id")
    Optional<Concert> findByIdWithVenue(@Param("id") Long id);

    Page<Concert> findAllByCategory(Category name, Pageable pageable);

    List<Concert> findByEndDateBefore(LocalDate endDate);


    // 삭제
//    @Query("SELECT c FROM Concert c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
//            "OR LOWER(c.artists) LIKE LOWER(CONCAT('%', :keyword, '%'))")
//    Page<Concert> searchByTitleOrArtists(@Param("keyword") String keyword, Pageable pageable);
}
