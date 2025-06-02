package com.backend.study_hub_api.service;

import com.backend.study_hub_api.helper.enumeration.VerificationType;
import com.backend.study_hub_api.helper.exception.BadRequestException;
import com.backend.study_hub_api.model.User;
import com.backend.study_hub_api.model.VerificationCode;
import com.backend.study_hub_api.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.backend.study_hub_api.helper.constant.Message.VERIFICATION_CODE_EXISTS_ERROR;
import static com.backend.study_hub_api.helper.constant.Message.VERIFICATION_CODE_SENT_FAILED_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;

    @Value("${app.verification.otp-length:6}")
    private int otpLength;

    @Value("${app.verification.otp-expiry-minutes:1}")
    private int otpExpiryMinutes;

    @Value("${app.verification.max-attempts:5}")
    private int maxAttempts;

    private static final SecureRandom random = new SecureRandom();

    /**
     * Tạo và gửi OTP verification code
     */
    @Transactional
    public void generateAndSendVerificationCode(User user, VerificationType type) {
        if (hasValidCode(user.getId(), type)) {
            throw new BadRequestException(VERIFICATION_CODE_EXISTS_ERROR);
        }

        verificationCodeRepository.markAllAsUsedForUser(user.getId(), type);

        String code = generateOTP();
        Instant expiredAt = Instant.now().plus(otpExpiryMinutes, ChronoUnit.MINUTES);

        VerificationCode verificationCode = VerificationCode.builder()
                                                            .user(user)
                                                            .code(code)
                                                            .type(type)
                                                            .isUsed(false)
                                                            .expiredAt(expiredAt)
                                                            .build();

        verificationCodeRepository.save(verificationCode);

        try {
            switch (type) {
                case EMAIL_VERIFICATION:
                    emailService.sendVerificationEmail(user.getEmail(), user.getFullName(), code);
                    break;
                case PASSWORD_RESET:
                    emailService.sendPasswordResetEmail(user.getEmail(), user.getFullName(), code);
                    break;
            }
            log.info("Verification code sent to user: {} for type: {}", user.getEmail(), type);
        } catch (Exception e) {
            log.error("Failed to send verification code to user: {}", user.getEmail(), e);
            throw new BadRequestException(VERIFICATION_CODE_SENT_FAILED_ERROR);
        }
    }

    /**
     * Xác minh OTP code
     */
    @Transactional
    public boolean verifyCode(Long userId, String code, VerificationType type) {
        Optional<VerificationCode> verificationCodeOpt = verificationCodeRepository
                .findValidCode(userId, code, type, Instant.now());

        if (verificationCodeOpt.isPresent()) {
            VerificationCode verificationCode = verificationCodeOpt.get();

            verificationCode.setIsUsed(true);
            verificationCodeRepository.save(verificationCode);

            log.info("Verification code verified successfully for user: {} type: {}", userId, type);
            return true;
        }

        log.warn("Invalid verification code attempt for user: {} type: {}", userId, type);
        return false;
    }

    /**
     * Kiểm tra xem user có code hợp lệ không
     */
    public boolean hasValidCode(Long userId, VerificationType type) {
        return verificationCodeRepository.hasValidCode(userId, type, Instant.now());
    }

    /**
     * Tạo OTP ngẫu nhiên
     */
    private String generateOTP() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    /**
     * Cleanup expired codes (có thể chạy bằng scheduled task)
     */
    @Transactional
    public void cleanupExpiredCodes() {
        verificationCodeRepository.deleteExpiredCodes(Instant.now());
        log.info("Cleaned up expired verification codes");
    }
}