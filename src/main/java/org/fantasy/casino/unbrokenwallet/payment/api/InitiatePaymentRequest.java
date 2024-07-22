package org.fantasy.casino.unbrokenwallet.payment.api;

import java.math.BigDecimal;

public record InitiatePaymentRequest(
        BigDecimal amount,
        String currency,
        String paymentType,
        String playerUuid
) {
}
