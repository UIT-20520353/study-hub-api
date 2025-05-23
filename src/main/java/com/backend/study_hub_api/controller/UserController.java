package com.backend.study_hub_api.controller;

import com.backend.study_hub_api.config.jwt.SecurityUtils;
import com.backend.study_hub_api.dto.UserDTO;
import com.backend.study_hub_api.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}