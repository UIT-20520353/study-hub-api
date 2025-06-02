package com.backend.study_hub_api.service;

import com.backend.study_hub_api.dto.AuthDTO;
import com.backend.study_hub_api.dto.UserDTO;

public interface AuthService {
    AuthDTO.AuthResponse login(AuthDTO.LoginRequest request);
    UserDTO register(AuthDTO.RegisterRequest request);
    void verifyEmail(Long userId, String code);
    void resendVerificationEmail(Long userId);
    UserDTO sendVerificationEmail(Long userId);
}
