package com.eatpizzaquickly.batchservice.settlement.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TempPaymentQueryDslRepositoryImpl implements TempPaymentQueryDslRepository{
    private final JPAQueryFactory queryFactory;

}
