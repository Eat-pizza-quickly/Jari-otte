package com.eatpizzaquickly.concertservice.repository;

import com.eatpizzaquickly.concertservice.entity.Concert;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.eatpizzaquickly.concertservice.entity.QConcert.concert;

@RequiredArgsConstructor
public class ConcertCustomRepositoryImpl implements ConcertCustomRepository{

    private final JPAQueryFactory queryFactory;

//    @Override
    public Page<Concert> searchByTitleOrArtists(String keyword, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.trim().isEmpty()) {
            String searchKeyword = "%" + keyword.toLowerCase() + "%";

            builder.or(concert.title.lower().like(searchKeyword))
                    .or(Expressions.stringTemplate(
                            "function('lower', {0})",
                            concert.artists
                    ).like(searchKeyword));
        }

        // 결과 조회
        List<Concert> results = queryFactory
                .selectFrom(concert)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(concert.id.desc())
                .fetch();

        // 총 개수 조회
        Long total = queryFactory
                .select(concert.count())
                .from(concert)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(results, pageable, total != null ? total : 0L);
    }
}
