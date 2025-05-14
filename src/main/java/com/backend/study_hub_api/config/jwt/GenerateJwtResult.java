package com.backend.study_hub_api.config.jwt;

import java.time.Instant;

public record GenerateJwtResult(
        String tokenId,
        String accessToken,
        String refreshToken,
        Instant expiredDate
) {
}
