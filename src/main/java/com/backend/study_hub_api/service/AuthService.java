package com.backend.study_hub_api.service;

import com.backend.study_hub_api.dto.AuthDTO;

public interface AuthService {
    AuthDTO.AuthResponse login(AuthDTO.LoginRequest request);
    void register(AuthDTO.RegisterRequest request);
}
