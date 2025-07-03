package org.fantasy.casino.unbrokenwallet.player.kyc;

import lombok.NonNull;

public record KycVerificationEvent(@NonNull String playerUuid, boolean verificationResult) {
}
