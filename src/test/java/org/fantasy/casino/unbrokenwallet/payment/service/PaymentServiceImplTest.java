package org.fantasy.casino.unbrokenwallet.payment.service;

import org.fantasy.casino.unbrokenwallet.account.Account;
import org.fantasy.casino.unbrokenwallet.account.AccountRepository;
import org.fantasy.casino.unbrokenwallet.payment.Payment;
import org.fantasy.casino.unbrokenwallet.payment.PaymentRepository;
import org.fantasy.casino.unbrokenwallet.payment.PaymentState;
import org.fantasy.casino.unbrokenwallet.payment.PaymentType;
import org.fantasy.casino.unbrokenwallet.payment.api.InitiatePaymentRequest;
import org.fantasy.casino.unbrokenwallet.player.Player;
import org.fantasy.casino.unbrokenwallet.player.PlayerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepositoryMock;

    @Mock
    private PlayerRepository playerRepositoryMock;

    @Mock
    private AccountRepository accountRepositoryMock;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Captor
    private ArgumentCaptor<Payment> paymentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldInitiatePayment() {
        var givenAccount = new Account();
        var givenPlayer = new Player();
        givenPlayer.setKycVerified(true);

        var givenPaymentType = "DEPOSIT";
        var givenCurrency = "USD";
        var givenAmount = BigDecimal.valueOf(100);
        var initiatePaymentRequest = new InitiatePaymentRequest(
                givenAmount,
                givenCurrency,
                givenPaymentType,
                givenPlayer.getUuid()
        );


        when(playerRepositoryMock.findByUuid(eq(givenPlayer.getUuid())))
                .thenReturn(Optional.of(givenPlayer));

        when(accountRepositoryMock.findByPlayerId(eq(givenPlayer.getId())))
                .thenReturn(Optional.of(givenAccount));

        paymentService.initiatePayment(initiatePaymentRequest);

        verify(paymentRepositoryMock, times(1)).saveAndFlush(paymentCaptor.capture());
        var capturedPayment = paymentCaptor.getValue();
        assertEquals(capturedPayment.getPaymentType().name(), givenPaymentType);
        assertEquals(capturedPayment.getCurrency().name(), givenCurrency);
        assertEquals(capturedPayment.getAmount(), givenAmount);

    }

    @Test
    void shouldThrowExceptionWhenPlayerNotVerified() {
        var player = new Player();
        player.setKycVerified(false);

        var initiatePaymentRequest = new InitiatePaymentRequest(
                BigDecimal.valueOf(100),
                "USD",
                "DEPOSIT",
                "1234"
        );

        when(playerRepositoryMock.findByUuid(anyString())).thenReturn(Optional.of(player));

        var exception = Assertions.assertThrows(RuntimeException.class, () -> paymentService.initiatePayment(initiatePaymentRequest));
        assertEquals("Player is not verified", exception.getMessage());
    }

    @Test
    void shouldUpdatePaymentStateToApproved() {
        var payment = new Payment();
        var account = new Account();
        account.setBalance(200.00);
        var player = new Player();
        player.setKycVerified(true);
        account.setPlayer(player);
        payment.setAccount(account);
        payment.setPaymentType(PaymentType.DEPOSIT);
        payment.setAmount(BigDecimal.valueOf(100));

        when(paymentRepositoryMock.findByUuid(eq(payment.getUuid()))).thenReturn(Optional.of(payment));

        paymentService.updatePaymentState(payment.getUuid(), PaymentState.APPROVED);

        verify(paymentRepositoryMock, times(1)).saveAndFlush(paymentCaptor.capture());
        var updatedPayment = paymentCaptor.getValue();
        assertEquals(BigDecimal.valueOf(300).doubleValue(), updatedPayment.getAccount().getBalance());
    }
}
