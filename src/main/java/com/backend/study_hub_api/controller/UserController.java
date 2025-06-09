package com.backend.study_hub_api.controller;

import com.backend.study_hub_api.config.jwt.SecurityUtils;
import com.backend.study_hub_api.dto.UserDTO;
import com.backend.study_hub_api.dto.request.ChangePasswordRequest;
import com.backend.study_hub_api.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "Endpoints for user profile management")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getCurrentUserProfile() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        UserDTO userProfile = userService.getUserById(currentUserId);
        return ResponseEntity.ok(userProfile);
    }

    @GetMapping("/user-detail/{userId}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable Long userId) {
        UserDTO userProfile = userService.getUserById(userId);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/change-avatar")
    public ResponseEntity<UserDTO> changeAvatar(@RequestParam("avatar") MultipartFile avatar) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        UserDTO updatedUser = userService.changeAvatar(currentUserId, avatar);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        userService.changePassword(currentUserId, request);
        return ResponseEntity.noContent().build();
    }

}