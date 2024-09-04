package org.fantasy.casino.unbrokenwallet.payment.api;

import lombok.RequiredArgsConstructor;
import org.fantasy.casino.unbrokenwallet.common.EntityNotFoundException;
import org.fantasy.casino.unbrokenwallet.payment.Payment;
import org.fantasy.casino.unbrokenwallet.payment.service.PaymentService;
import org.fantasy.casino.unbrokenwallet.payment.PaymentState;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final ConversionService conversionService;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> initiatePayment(
            @RequestBody InitiatePaymentRequest initiatePaymentRequest) {
        Payment registeredPayment = paymentService.initiatePayment(initiatePaymentRequest);
        var paymentResponseDTO = conversionService.convert(registeredPayment, PaymentResponse.class);

        return ResponseEntity.status(CREATED).body(paymentResponseDTO);
    }

    @PatchMapping("/state/{uuid}")
    public ResponseEntity<PaymentResponse> updatePaymentState(
            @PathVariable String uuid, @RequestParam PaymentState state){
        var updatedPayment = paymentService.updatePaymentState(uuid, state);
        var paymentResponseDTO = conversionService.convert(updatedPayment, PaymentResponse.class);

        return ResponseEntity.ok(paymentResponseDTO);
    }

    @GetMapping
    public ResponseEntity<GetPaymentsResponse> getPayments(@RequestParam String playerUuid) {
        var payments = paymentService.getPayments(playerUuid).stream()
                .map(payment -> conversionService.convert(payment, PaymentResponse.class))
                .toList();

        return ResponseEntity.ok(new GetPaymentsResponse(payments));
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
