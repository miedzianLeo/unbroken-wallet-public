package org.fantasy.casino.unbrokenwallet.payment.api;

import java.util.List;

public record GetPaymentsResponse(
        List<PaymentResponse> payments
) {
}