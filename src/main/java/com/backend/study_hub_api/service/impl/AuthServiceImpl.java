package com.backend.study_hub_api.service.impl;

import com.backend.study_hub_api.config.jwt.GenerateJwtResult;
import com.backend.study_hub_api.config.jwt.JwtProvider;
import com.backend.study_hub_api.dto.AuthDTO;
import com.backend.study_hub_api.helper.exception.AuthenticationException;
import com.backend.study_hub_api.model.User;
import com.backend.study_hub_api.model.UserSession;
import com.backend.study_hub_api.repository.UserSessionRepository;
import com.backend.study_hub_api.service.AuthService;
import com.backend.study_hub_api.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static com.backend.study_hub_api.helper.constant.Message.INVALID_CREDENTIAL_ERR;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    UserService userService;
    PasswordEncoder passwordEncoder;
    JwtProvider jwtProvider;

    @Override
    @Transactional
    public AuthDTO.AuthResponse login(AuthDTO.LoginRequest request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthenticationException(INVALID_CREDENTIAL_ERR));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthenticationException(INVALID_CREDENTIAL_ERR);
        }

        GenerateJwtResult jwtTokens = jwtProvider.generateToken(user);

        user.setNewSession(new UserSession(jwtTokens.tokenId(), jwtTokens.expiredDate()));

        return AuthDTO.AuthResponse.builder()
                .token(jwtTokens.accessToken())
                .tokenType("Bearer")
                .user(userService.mapToDTO(user))
                .build();
    }

//    @Override
//    @Transactional
//    public void register(AuthDTO.RegisterRequest request) {
//        userService.registerUser(request);
//    }
}
