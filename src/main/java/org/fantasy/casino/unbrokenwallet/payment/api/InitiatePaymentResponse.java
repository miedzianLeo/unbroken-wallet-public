package org.fantasy.casino.unbrokenwallet.payment.api;

import org.fantasy.casino.unbrokenwallet.payment.PaymentState;

import java.math.BigDecimal;

public record InitiatePaymentResponse(
        String uuid,
        BigDecimal amount,
        String currency,
        String paymentType,
        PaymentState state
) {
}
