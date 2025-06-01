package com.backend.study_hub_api.controller;

import com.backend.study_hub_api.dto.UniversityDTO;
import com.backend.study_hub_api.service.UniversityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/common/universities")
@RequiredArgsConstructor
@Tag(name = "Public Universities", description = "Public endpoints for universities")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UniversityPublicController {

    UniversityService universityService;

    @GetMapping
    public ResponseEntity<List<UniversityDTO.UniversityResponse>> getAllUniversities() {
        return ResponseEntity.ok(universityService.getAllActiveUniversities());
    }

}
