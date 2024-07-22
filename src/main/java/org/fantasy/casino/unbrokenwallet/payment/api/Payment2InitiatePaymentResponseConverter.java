package org.fantasy.casino.unbrokenwallet.payment.api;

import org.fantasy.casino.unbrokenwallet.payment.Payment;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class Payment2InitiatePaymentResponseConverter implements Converter<Payment, InitiatePaymentResponse> {

    @Override
    public InitiatePaymentResponse convert(Payment payment) {
        return new InitiatePaymentResponse(
                payment.getUuid(),
                payment.getAmount(),
                payment.getCurrency().name(),
                payment.getPaymentType().name(),
                payment.getState()
        );
    }
}
