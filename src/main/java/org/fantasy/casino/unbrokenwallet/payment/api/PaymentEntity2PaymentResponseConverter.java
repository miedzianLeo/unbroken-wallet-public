package org.fantasy.casino.unbrokenwallet.payment.api;

import org.fantasy.casino.unbrokenwallet.payment.Payment;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class PaymentEntity2PaymentResponseConverter implements Converter<Payment, PaymentResponse> {

    @Override
    public PaymentResponse convert(Payment payment) {
        return new PaymentResponse(
                payment.getUuid(),
                payment.getAmount(),
                payment.getCurrency().name(),
                payment.getPaymentType().name(),
                payment.getState()
        );
    }
}
