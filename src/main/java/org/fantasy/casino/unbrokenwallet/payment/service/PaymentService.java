package org.fantasy.casino.unbrokenwallet.payment.service;

import org.fantasy.casino.unbrokenwallet.payment.Payment;
import org.fantasy.casino.unbrokenwallet.payment.PaymentState;
import org.fantasy.casino.unbrokenwallet.payment.api.InitiatePaymentRequest;

import java.util.List;

public interface PaymentService {

    Payment initiatePayment(InitiatePaymentRequest payment);

    Payment updatePaymentState(String paymentUid, PaymentState state);

    List<Payment> getPayments(String playerUuid);
}
