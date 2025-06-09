package com.backend.study_hub_api.service.impl;

import com.backend.study_hub_api.config.jwt.SecurityUtils;
import com.backend.study_hub_api.dto.AuthDTO;
import com.backend.study_hub_api.dto.UniversityDTO;
import com.backend.study_hub_api.dto.UserDTO;
import com.backend.study_hub_api.dto.request.ChangePasswordRequest;
import com.backend.study_hub_api.helper.enumeration.UserRole;
import com.backend.study_hub_api.helper.enumeration.VerificationType;
import com.backend.study_hub_api.helper.exception.BadRequestException;
import com.backend.study_hub_api.model.University;
import com.backend.study_hub_api.model.User;
import com.backend.study_hub_api.repository.UserRepository;
import com.backend.study_hub_api.service.FileUploadService;
import com.backend.study_hub_api.service.UniversityService;
import com.backend.study_hub_api.service.UserService;
import com.backend.study_hub_api.service.VerificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.backend.study_hub_api.helper.constant.Message.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    UniversityService universityService;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    VerificationService verificationService;
    FileUploadService fileUploadService;

    private static final String[] ALLOWED_FILE_TYPES = {
            "image/jpeg",
            "image/jpg",
            "image/png",
    };

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public UserDTO registerUser(AuthDTO.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(USER_ALREADY_EXISTS);
        }

        if (request.getStudentId() != null &&
            userRepository.existsByStudentId(request.getStudentId())) {
            throw new BadRequestException(USER_STUDENT_ID_EXISTS_ERROR);
        }

        University university = universityService.getUniversityByIdOrThrow(request.getUniversityId());

        validateEmailDomain(request.getEmail(), university.getEmailDomain());

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .studentId(request.getStudentId())
                .university(university)
                .major(request.getMajor())
                .year(request.getYear())
                .phone(request.getPhone())
                .role(UserRole.USER)
                .isVerified(false)
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);

        return mapToDTO(savedUser);
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
        UniversityDTO.UniversityResponse universityResponse;
        if (user.getUniversity() != null) {
            universityResponse = universityService.mapToDTO(user.getUniversity());
        } else {
            universityResponse = null; // Handle case where university is null
        }

        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .studentId(user.getStudentId())
                .university(universityResponse)
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
    @Transactional
    public void verifyEmail(Long userId, String otpCode) {
        User user = getUserByIdOrThrow(userId);

        if (user.getIsVerified()) {
            throw new BadRequestException(EMAIL_ALREADY_VERIFIED_ERROR);
        }

        boolean isValid = verificationService.verifyCode(userId, otpCode, VerificationType.EMAIL_VERIFICATION);

        if (!isValid) {
            throw new BadRequestException(EMAIL_VERIFICATION_FAILED_ERROR);
        }
        user.setIsVerified(true);
        userRepository.save(user);
        log.info("User email verified successfully: {}", user.getEmail());
    }

    @Override
    public User getCurrentUser() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BadRequestException(USER_NOT_FOUND_ERROR);
        }
        return getUserByIdOrThrow(userId);
    }

    @Override
    @Transactional
    public UserDTO changeAvatar(Long userId, MultipartFile avatar) {
        User user = getUserByIdOrThrow(userId);
        if (avatar == null || avatar.isEmpty()) {
            throw new BadRequestException(AVATAR_FILE_EMPTY_ERROR);
        }
        String avatarUrl = fileUploadService
                .uploadFile(avatar, "avatars", ALLOWED_FILE_TYPES)
                .getFileUrl();
        if (avatarUrl == null) {
            throw new BadRequestException(AVATAR_UPLOAD_ERROR);
        }
        user.setAvatarUrl(avatarUrl);
        User updatedUser = userRepository.save(user);
        return mapToDTO(updatedUser);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = getUserByIdOrThrow(userId);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException(CURRENT_PASSWORD_INCORRECT_ERROR);
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new BadRequestException(NEW_PASSWORD_MUST_DIFFER_ERROR);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(java.time.Instant.now());

        userRepository.save(user);
    }

    @Override
    public User getUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(USER_NOT_FOUND_ERROR));
    }

    private void validateEmailDomain(String email, String universityEmailDomain) {
        String emailDomain = extractDomainFromEmail(email);

        if (!emailDomain.equalsIgnoreCase(universityEmailDomain)) {
            throw new BadRequestException(EMAIL_DOMAIN_MISMATCH_ERROR);
        }
    }

    private String extractDomainFromEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new BadRequestException(EMAIL_INVALID_ERROR);
        }

        return email.substring(email.lastIndexOf("@") + 1).toLowerCase();
    }
}