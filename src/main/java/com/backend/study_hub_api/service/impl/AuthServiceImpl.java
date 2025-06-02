package com.backend.study_hub_api.service.impl;

import com.backend.study_hub_api.config.jwt.GenerateJwtResult;
import com.backend.study_hub_api.config.jwt.JwtProvider;
import com.backend.study_hub_api.dto.AuthDTO;
import com.backend.study_hub_api.dto.UserDTO;
import com.backend.study_hub_api.helper.enumeration.VerificationType;
import com.backend.study_hub_api.helper.exception.AuthenticationException;
import com.backend.study_hub_api.helper.exception.BadRequestException;
import com.backend.study_hub_api.model.User;
import com.backend.study_hub_api.model.UserSession;
import com.backend.study_hub_api.service.AuthService;
import com.backend.study_hub_api.service.UserService;
import com.backend.study_hub_api.service.VerificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.backend.study_hub_api.helper.constant.Message.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    UserService userService;
    PasswordEncoder passwordEncoder;
    JwtProvider jwtProvider;
    VerificationService verificationService;

    @Override
    @Transactional
    public AuthDTO.AuthResponse login(AuthDTO.LoginRequest request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthenticationException(INVALID_CREDENTIAL_ERR));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthenticationException(INVALID_CREDENTIAL_ERR);
        }

        if (!user.getIsActive()) {
            throw new BadRequestException(ACCOUNT_BLOCKED_ERROR);
        }

        if (!user.getIsVerified()) {
            return AuthDTO.AuthResponse.builder()
                    .user(userService.mapToDTO(user))
                    .message(ACCOUNT_NOT_VERIFIED_ERROR)
                    .token("")
                    .tokenType("")
                    .build();
        }



        GenerateJwtResult jwtTokens = jwtProvider.generateToken(user);

        user.setNewSession(new UserSession(jwtTokens.tokenId(), jwtTokens.expiredDate()));

        return AuthDTO.AuthResponse.builder()
                .token(jwtTokens.accessToken())
                .tokenType("Bearer")
                .user(userService.mapToDTO(user))
                .message("Login successful")
                .build();
    }

    @Override
    @Transactional
    public UserDTO register(AuthDTO.RegisterRequest request) {
        return userService.registerUser(request);
    }

    @Override
    @Transactional
    public void verifyEmail(Long userId, String code) {
        userService.verifyEmail(userId, code);
    }

    @Override
    @Transactional
    public void resendVerificationEmail(Long userId) {
        User user = userService.getUserByIdOrThrow(userId);

        if (user.getIsVerified()) {
            throw new BadRequestException(ACCOUNT_ALREADY_VERIFIED_ERROR);
        }

        verificationService.generateAndSendVerificationCode(user, VerificationType.EMAIL_VERIFICATION);
    }

    @Override
    public UserDTO sendVerificationEmail(Long userId) {
        User user = userService.getUserByIdOrThrow(userId);
        verificationService.generateAndSendVerificationCode(user, VerificationType.EMAIL_VERIFICATION);
        return userService.mapToDTO(user);
    }
}
