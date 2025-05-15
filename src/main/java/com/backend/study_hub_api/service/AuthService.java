package com.backend.study_hub_api.service;

import com.backend.study_hub_api.dto.AuthDTO;

public interface AuthService {
    AuthDTO.AuthResponse login(AuthDTO.LoginRequest request);
    AuthDTO.AuthResponse register(AuthDTO.RegisterRequest request);
}
