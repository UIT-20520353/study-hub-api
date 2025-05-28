package com.backend.study_hub_api.service.impl;

import com.backend.study_hub_api.dto.AuthDTO;
import com.backend.study_hub_api.dto.UserDTO;
import com.backend.study_hub_api.helper.enumeration.UserRole;
import com.backend.study_hub_api.helper.exception.BadRequestException;
import com.backend.study_hub_api.model.User;
import com.backend.study_hub_api.repository.UserRepository;
import com.backend.study_hub_api.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.backend.study_hub_api.helper.constant.Message.USER_ALREADY_EXISTS;
import static com.backend.study_hub_api.helper.constant.Message.USER_NOT_FOUND_ERROR;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(AuthDTO.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(USER_ALREADY_EXISTS);
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .studentId(request.getStudentId())
                .university(request.getUniversity())
                .major(request.getMajor())
                .year(request.getYear())
                .phone(request.getPhone())
                .role(UserRole.USER)
                .isVerified(false)
                .isActive(true)
                .build();

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new BadRequestException(USER_NOT_FOUND_ERROR));
    }

    @Override
    public UserDTO mapToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .studentId(user.getStudentId())
                .university(user.getUniversity())
                .major(user.getMajor())
                .year(user.getYear())
                .avatarUrl(user.getAvatarUrl())
                .phone(user.getPhone())
                .bio(user.getBio())
                .role(user.getRole())
                .isVerified(user.getIsVerified())
                .build();
    }

    @Override
    public User getUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(USER_NOT_FOUND_ERROR));
    }
}