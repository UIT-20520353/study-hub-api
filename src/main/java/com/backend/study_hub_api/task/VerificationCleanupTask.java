package com.backend.study_hub_api.task;

import com.backend.study_hub_api.service.VerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VerificationCleanupTask {

    private final VerificationService verificationService;

    /**
     * Cleanup expired verification codes every hour
     */
    @Scheduled(fixedRate = 3600000)
    public void cleanupExpiredVerificationCodes() {
        try {
            log.info("Starting cleanup of expired verification codes");
            verificationService.cleanupExpiredCodes();
            log.info("Completed cleanup of expired verification codes");
        } catch (Exception e) {
            log.error("Error during verification codes cleanup", e);
        }
    }
}