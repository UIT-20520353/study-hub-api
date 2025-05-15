package com.backend.study_hub_api.service.impl;

import com.backend.study_hub_api.dto.AuthDTO;
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
    UserSessionRepository userSessionRepository;

    @Override
    @Transactional
    public AuthDTO.AuthResponse login(AuthDTO.LoginRequest request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException(INVALID_CREDENTIAL_ERR));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException(INVALID_CREDENTIAL_ERR);
        }

        String tokenId = UUID.randomUUID().toString();
        Instant expiredDate = Instant.now().plus(30, ChronoUnit.DAYS);

        UserSession userSession = new UserSession(tokenId, expiredDate);
        userSession.setUser(user);
        userSessionRepository.save(userSession);

        return AuthDTO.AuthResponse.builder()
                .token(tokenId)
                .tokenType("Bearer")
                .user(userService.mapToDTO(user))
                .build();
    }

    @Override
    @Transactional
    public void register(AuthDTO.RegisterRequest request) {
        userService.registerUser(request);
    }
}
