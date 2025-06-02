package com.backend.study_hub_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.email.from:noreply@studyhub.com}")
    private String fromEmail;

    @Value("${app.name:StudyHub}")
    private String appName;

    public void sendVerificationEmail(String toEmail, String fullName, String otpCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Xác nhận tài khoản " + appName);

            String emailContent = buildVerificationEmailContent(fullName, otpCode);
            message.setText(emailContent);

            mailSender.send(message);
            log.info("Verification email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send verification email to: {}", toEmail, e);
            throw new RuntimeException("Không thể gửi email xác nhận. Vui lòng thử lại sau.");
        }
    }

    public void sendPasswordResetEmail(String toEmail, String fullName, String otpCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Đặt lại mật khẩu " + appName);

            String emailContent = buildPasswordResetEmailContent(fullName, otpCode);
            message.setText(emailContent);

            mailSender.send(message);
            log.info("Password reset email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", toEmail, e);
            throw new RuntimeException("Không thể gửi email đặt lại mật khẩu. Vui lòng thử lại sau.");
        }
    }

    private String buildVerificationEmailContent(String fullName, String otpCode) {
        return String.format(
                "Xin chào %s,\n\n" +
                "Cảm ơn bạn đã đăng ký tài khoản tại %s!\n\n" +
                "Mã xác nhận của bạn là: %s\n\n" +
                "Mã này có hiệu lực trong vòng 1 phút.\n\n" +
                "Nếu bạn không thực hiện đăng ký này, vui lòng bỏ qua email này.\n\n" +
                "Trân trọng,\n" +
                "Đội ngũ %s",
                fullName, appName, otpCode, appName
        );
    }

    private String buildPasswordResetEmailContent(String fullName, String otpCode) {
        return String.format(
                "Xin chào %s,\n\n" +
                "Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản %s.\n\n" +
                "Mã xác nhận của bạn là: %s\n\n" +
                "Mã này có hiệu lực trong vòng 10 phút.\n\n" +
                "Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.\n\n" +
                "Trân trọng,\n" +
                "Đội ngũ %s",
                fullName, appName, otpCode, appName
        );
    }
}