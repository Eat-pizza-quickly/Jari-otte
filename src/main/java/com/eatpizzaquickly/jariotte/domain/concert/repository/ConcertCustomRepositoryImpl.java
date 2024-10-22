package com.eatpizzaquickly.jariotte.domain.concert.repository;

import com.eatpizzaquickly.jariotte.domain.concert.entity.Concert;
import com.eatpizzaquickly.jariotte.domain.concert.entity.QConcert;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.eatpizzaquickly.jariotte.domain.concert.entity.QConcert.*;

@RequiredArgsConstructor
public class ConcertCustomRepositoryImpl implements ConcertCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Concert> searchByTitleOrArtists(String keyword, Pageable pageable) {

        List<Concert> results = queryFactory.selectFrom(concert)
                .where(
                        concert.title.containsIgnoreCase(keyword)
                                .or(concert.artists.any().contains(keyword))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(concert)
                .where(
                        concert.title.containsIgnoreCase(keyword)
                                .or(concert.artists.any().containsIgnoreCase(keyword))
                )
                .fetch().size();

        return new PageImpl<>(results, pageable, total);
    }
}
