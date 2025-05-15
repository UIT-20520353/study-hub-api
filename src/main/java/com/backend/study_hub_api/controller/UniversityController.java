package com.backend.study_hub_api.controller;

import com.backend.study_hub_api.dto.UniversityDTO;
import com.backend.study_hub_api.service.UniversityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/universities")
@RequiredArgsConstructor
@Tag(name = "University Management", description = "Endpoints for university management (Admin only)")
public class UniversityController {

    private final UniversityService universityService;

    @PostMapping
    public ResponseEntity<UniversityDTO.UniversityResponse> createUniversity(
            @Valid @RequestBody UniversityDTO.CreateUniversityRequest request) {
        return ResponseEntity.ok(universityService.createUniversity(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UniversityDTO.UniversityResponse> getUniversityById(@PathVariable Long id) {
        return ResponseEntity.ok(universityService.getUniversityById(id));
    }

    @GetMapping
    public ResponseEntity<List<UniversityDTO.UniversityResponse>> getAllUniversities() {
        return ResponseEntity.ok(universityService.getAllUniversities());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UniversityDTO.UniversityResponse> updateUniversity(
            @PathVariable Long id,
            @Valid @RequestBody UniversityDTO.UpdateUniversityRequest request) {
        return ResponseEntity.ok(universityService.updateUniversity(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniversity(@PathVariable Long id) {
        universityService.deleteUniversity(id);
        return ResponseEntity.noContent().build();
    }
}