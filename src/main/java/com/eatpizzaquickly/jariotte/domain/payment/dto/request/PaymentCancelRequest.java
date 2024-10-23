package com.eatpizzaquickly.jariotte.domain.payment.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentCancelRequest {
    private String cancelReason;
}
