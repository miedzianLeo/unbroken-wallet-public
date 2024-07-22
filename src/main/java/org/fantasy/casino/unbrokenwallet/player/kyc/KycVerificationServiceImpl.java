package org.fantasy.casino.unbrokenwallet.player.kyc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fantasy.casino.unbrokenwallet.player.Player;
import org.fantasy.casino.unbrokenwallet.player.PlayerRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class KycVerificationServiceImpl implements KycVerificationService {
    private final PlayerRepository playerRepository;

    @Override
    public Player handleVerificationResult(KycVerificationEvent event) throws Exception {
        var player = playerRepository.findByUuid(event.playerUid())
                .orElseThrow(() -> new Exception("Player not found with id " + event.playerUid()));

        player.setKycVerified(event.verificationResult());

        return playerRepository.saveAndFlush(player);
    }
}
