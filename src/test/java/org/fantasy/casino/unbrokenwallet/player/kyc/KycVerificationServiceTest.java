package org.fantasy.casino.unbrokenwallet.player.kyc;

import org.fantasy.casino.unbrokenwallet.player.Player;
import org.fantasy.casino.unbrokenwallet.player.kyc.KycVerificationEvent;
import org.fantasy.casino.unbrokenwallet.player.PlayerRepository;
import org.fantasy.casino.unbrokenwallet.player.kyc.KycVerificationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class KycVerificationServiceTest {

    @Autowired
    private KycVerificationService kycVerificationService;

    @Autowired
    private PlayerRepository playerRepository;

    @BeforeEach
    void setUp() {
        var player = new Player();
        player.setUUID("1234:1234");
        player.setKycVerified(false);
        player.setFirstName("John");
        player.setLastName("Paul");
        player.setAccount(null);
        playerRepository.save(player);
    }

    @AfterEach
    void tearDown() {
        playerRepository.deleteAll();
    }

    @Test
    void shouldChangeVerificationToTrue() throws Exception {
        KycVerificationEvent event = new KycVerificationEvent(
                "1234:1234",
                true
        );

        Player updatedPlayer = kycVerificationService.handleVerificationResult(event);

        assertTrue(updatedPlayer.getKycVerified());
    }


    @Test
    void shouldFailOnHandleKycVerificationEvent() {
        KycVerificationEvent event = new KycVerificationEvent(
                "1234:9999",
                true
        );

        Assertions.assertThrows(Exception.class, () -> kycVerificationService.handleVerificationResult(event));
    }

}
