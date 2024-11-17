package com.eatpizzaquickly.batchservice.settlement.processor;


import com.eatpizzaquickly.batchservice.common.client.ConcertClient;
import com.eatpizzaquickly.batchservice.settlement.dto.request.PaymentRequestDto;
import com.eatpizzaquickly.batchservice.settlement.dto.response.PaymentResponseDto;
import com.eatpizzaquickly.batchservice.settlement.entity.HostPoint;
import com.eatpizzaquickly.batchservice.settlement.entity.SettlementStatus;
import com.eatpizzaquickly.batchservice.settlement.entity.TempPayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import static com.eatpizzaquickly.batchservice.settlement.common.BatchConstant.COMMISSION;


@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentProcessor {
    private final ConcertClient concertClient;

    public ItemProcessor<TempPayment, TempPayment> settlementSettledProcessor() {
        return payment -> {
            payment.setSettlementStatus(SettlementStatus.SETTLED);
            return payment;
        };
    }

    public ItemProcessor<TempPayment, HostPoint> pointAdditionProcessor() {
        return payment -> {
            Long payId = payment.getPaymentId();
            Long hostId = concertClient.findHostIdByConcertId(payment.getConcertId());
            Long points = calculatePoints(payment.getAmount()); // 수수료 떼고 정산
            return new HostPoint(payId, hostId, points);
        };
    }

    public ItemProcessor<PaymentResponseDto, TempPayment> paymentSettleProcessor() {
        return payment -> {
            log.info("Payment {} 정산 진행 중", payment.getId());
            payment.setSettlementStatus(SettlementStatus.PROGRESS);
            return TempPayment.builder()
                    .paymentId(payment.getId())
                    .payStatus(payment.getPayStatus())
                    .settlementStatus(payment.getSettlementStatus())
                    .amount(payment.getAmount())
                    .concertId(payment.getConcertId())
                    .build();
        };
    }

    Long calculatePoints(Long amount) {
        return (long) (amount * COMMISSION);
    }

    public ItemProcessor<TempPayment, PaymentRequestDto> updatePaymentProcessor() {
        return PaymentRequestDto::from;
    }
}
