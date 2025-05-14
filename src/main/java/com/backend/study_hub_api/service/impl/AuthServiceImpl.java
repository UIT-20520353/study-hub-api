package com.backend.study_hub_api.service.impl;

import com.backend.study_hub_api.dto.request.LoginRequest;
import com.backend.study_hub_api.dto.request.RegisterRequest;
import com.backend.study_hub_api.dto.response.JwtResponse;
import com.backend.study_hub_api.dto.response.MessageResponse;
import com.backend.study_hub_api.model.User;
import com.backend.study_hub_api.repository.UserRepository;
import com.backend.study_hub_api.security.jwt.JwtUtils;
import com.backend.study_hub_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                                  .orElseThrow(() -> new RuntimeException("User not found"));

        return new JwtResponse(
                jwt,
                user.getId(),
                user.getEmail(),
                user.getFullName()
        );
    }

    @Override
    public MessageResponse registerUser(RegisterRequest registerRequest) {
        // Check if email exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new MessageResponse("Error: Email is already in use!");
        }

        // Check if student ID exists (if provided)
        if (registerRequest.getStudentId() != null && !registerRequest.getStudentId().isEmpty() &&
            userRepository.existsByStudentId(registerRequest.getStudentId())) {
            return new MessageResponse("Error: Student ID is already in use!");
        }

        // Create new user
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFullName(registerRequest.getFullName());
        user.setStudentId(registerRequest.getStudentId());
        user.setUniversity(registerRequest.getUniversity());
        user.setMajor(registerRequest.getMajor());
        user.setYear(registerRequest.getYear());
        user.setPhone(registerRequest.getPhone());
        user.setBio(registerRequest.getBio());

        userRepository.save(user);

        return new MessageResponse("User registered successfully!");
    }
}
