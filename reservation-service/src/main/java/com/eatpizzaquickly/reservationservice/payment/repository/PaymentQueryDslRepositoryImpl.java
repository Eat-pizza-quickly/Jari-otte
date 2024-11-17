package com.eatpizzaquickly.reservationservice.payment.repository;

import com.eatpizzaquickly.reservationservice.payment.dto.response.PaymentResponseDto;
import com.eatpizzaquickly.reservationservice.payment.entity.PayStatus;
import com.eatpizzaquickly.reservationservice.payment.entity.Payment;
import com.eatpizzaquickly.reservationservice.payment.entity.QPayment;
import com.eatpizzaquickly.reservationservice.payment.entity.SettlementStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.eatpizzaquickly.reservationservice.payment.entity.QPayment.payment;

@RequiredArgsConstructor
public class PaymentQueryDslRepositoryImpl implements PaymentQueryDslRepository {
    private final JPAQueryFactory queryFactory;


    public Page<PaymentResponseDto> getPaymentsByStatus(
            SettlementStatus settlementStatus,
            PayStatus payStatus,
            LocalDateTime before,
            int currentOffset,
            int chunk) {
        Pageable pageable = PageRequest.of(0, chunk);

        List<PaymentResponseDto> results = queryFactory
                .select(Projections.constructor(
                        PaymentResponseDto.class,
                        payment.id,
                        payment.settlementStatus,
                        payment.payStatus,
                        payment.amount,
                        payment.reservation.concertId))
                .from(payment)
                .where(
                        payment.settlementStatus.eq(settlementStatus),
                        payment.payStatus.eq(payStatus),
                        payment.paidAt.before(before),
                        payment.id.gt(currentOffset))
                .orderBy(payment.id.asc())
                .limit(chunk)
                .fetch();

        return new PageImpl<>(results, pageable, chunk);
    }

}
