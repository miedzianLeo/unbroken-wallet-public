package org.fantasy.casino.unbrokenwallet.player.kyc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Profile("kafka")
@Component
@RequiredArgsConstructor
@Slf4j
class KycVerificationEventConsumer {

    private final KycVerificationService kycVerificationService;

    @KafkaListener(topics = "unbroken-wallet.pub.kyc-verification")
    public void handleKycVerificationEvent(KycVerificationEvent event) {
        try {
            kycVerificationService.handleVerificationResult(event);
        } catch (Exception e) {
            log.error("Error while handling kyc verification event", e);
        }
    }
}
