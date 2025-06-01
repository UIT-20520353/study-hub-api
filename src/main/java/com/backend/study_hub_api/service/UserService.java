package com.backend.study_hub_api.service;

import com.backend.study_hub_api.dto.AuthDTO;
import com.backend.study_hub_api.dto.UserDTO;
import com.backend.study_hub_api.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    UserDTO registerUser(AuthDTO.RegisterRequest request);

    Optional<User> findByEmail(String email);

    UserDTO getUserById(Long id);

    UserDTO mapToDTO(User user);

    User getUserByIdOrThrow(Long id);

    void verifyEmail(Long userId, String otpCode);

}
