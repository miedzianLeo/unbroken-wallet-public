package org.fantasy.casino.unbrokenwallet.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fantasy.casino.unbrokenwallet.account.Account;
import org.fantasy.casino.unbrokenwallet.account.AccountRepository;
import org.fantasy.casino.unbrokenwallet.common.Currency;
import org.fantasy.casino.unbrokenwallet.common.EntityNotFoundException;
import org.fantasy.casino.unbrokenwallet.payment.Payment;
import org.fantasy.casino.unbrokenwallet.payment.PaymentRepository;
import org.fantasy.casino.unbrokenwallet.payment.PaymentState;
import org.fantasy.casino.unbrokenwallet.payment.PaymentType;
import org.fantasy.casino.unbrokenwallet.payment.api.InitiatePaymentRequest;
import org.fantasy.casino.unbrokenwallet.player.PlayerRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PlayerRepository playerRepository;
    private final AccountRepository accountRepository;

    @Override
    public Payment initiatePayment(InitiatePaymentRequest request) {
        var player = playerRepository.findByUuid(request.playerUuid())
                .orElseThrow(() -> new EntityNotFoundException("Player not found with uuid: " + request.playerUuid()));

        if (!player.getKycVerified()) {
            throw new RuntimeException("Player is not verified");
        }
        var account = accountRepository.findByPlayerId(player.getId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found for player with uuid: " + request.playerUuid()));

        var payment = new Payment(
                request.amount(),
                Currency.valueOf(request.currency()),
                PaymentType.valueOf(request.paymentType()),
                player,
                account
        );
        return paymentRepository.saveAndFlush(payment);
    }

    @Override
    public Payment updatePaymentState(String paymentUid, PaymentState state) {
        var payment = getPaymentByUid(paymentUid);
        var account = payment.getAccount();
        var paymentType = payment.getPaymentType();
        var amount = payment.getAmount();

        if (PaymentState.APPROVED.equals(state)) {
            updateAccountBalance(account, paymentType, amount);
        }

        payment.setState(state);
        return paymentRepository.saveAndFlush(payment);
    }

    @Override
    public List<Payment> getPayments(String playerUuid) {
        return paymentRepository.findByPlayerUuid(playerUuid);
    }

    private void updateAccountBalance(Account account, PaymentType paymentType, BigDecimal amount) {
        switch (paymentType) {
            case DEPOSIT -> account.setBalance(account.getBalance() + amount.doubleValue());
            case WITHDRAWAL -> account.setBalance(account.getBalance() - amount.doubleValue());
        }
    }

    public Payment getPaymentByUid(String paymentUid){
        return paymentRepository.findByUuid(paymentUid)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Payment not found with uid: %s", paymentUid)));
    }


}
