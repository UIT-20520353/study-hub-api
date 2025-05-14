package com.backend.study_hub_api.service;

import com.backend.study_hub_api.dto.request.LoginRequest;
import com.backend.study_hub_api.dto.request.RegisterRequest;
import com.backend.study_hub_api.dto.response.JwtResponse;
import com.backend.study_hub_api.dto.response.MessageResponse;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);

    MessageResponse registerUser(RegisterRequest registerRequest);
}
