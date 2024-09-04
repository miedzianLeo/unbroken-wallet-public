package org.fantasy.casino.unbrokenwallet.player.kyc;


import org.fantasy.casino.unbrokenwallet.player.Player;
import org.fantasy.casino.unbrokenwallet.player.kyc.KycVerificationEvent;

public interface KycVerificationService {
    Player handleVerificationResult(KycVerificationEvent event) throws Exception;
}
