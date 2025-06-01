package com.backend.study_hub_api.controller;

import com.backend.study_hub_api.dto.UniversityDTO;
import com.backend.study_hub_api.dto.common.PaginationDTO;
import com.backend.study_hub_api.dto.criteria.UniversityFilterCriteria;
import com.backend.study_hub_api.service.UniversityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/universities")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "University Management", description = "Endpoints for university management (Admin only)")
public class UniversityAdminController {

    UniversityService universityService;

    @GetMapping
    @Operation(summary = "Filter universities", description = "Filter universities by various criteria")
    public ResponseEntity<PaginationDTO<UniversityDTO.UniversityResponse>> filterUniversities(
            @ModelAttribute UniversityFilterCriteria criteria) {

        PaginationDTO<UniversityDTO.UniversityResponse> result = universityService.getUniversitiesWithFilter(criteria);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/active")
    @Operation(summary = "Get active universities", description = "Get all active universities")
    public ResponseEntity<List<UniversityDTO.UniversityResponse>> getActiveUniversities() {
        List<UniversityDTO.UniversityResponse> result = universityService.getActiveUniversities();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get university by ID", description = "Get university details by ID")
    public ResponseEntity<UniversityDTO.UniversityResponse> getUniversityById(@PathVariable Long id) {
        UniversityDTO.UniversityResponse result = universityService.getUniversityById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create university", description = "Create new university with optional logo upload")
    public ResponseEntity<UniversityDTO.UniversityResponse> createUniversity(
            @Valid @RequestPart("university") UniversityDTO.CreateUniversityRequest request,
            @RequestPart(value = "logo", required = false) MultipartFile logoFile) {

        UniversityDTO.UniversityResponse result = universityService.createUniversity(request, logoFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update university", description = "Update university with optional logo upload")
    public ResponseEntity<UniversityDTO.UniversityResponse> updateUniversity(
            @PathVariable Long id,
            @Valid @RequestPart("university") UniversityDTO.UpdateUniversityRequest request,
            @RequestPart(value = "logo", required = false) MultipartFile logoFile) {

        UniversityDTO.UniversityResponse result = universityService.updateUniversity(id, request, logoFile);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update university status", description = "Update only the status of a university")
    public ResponseEntity<UniversityDTO.UniversityResponse> updateUniversityStatus(
            @PathVariable Long id,
            @Valid @RequestBody UniversityDTO.UpdateStatusRequest request) {

        UniversityDTO.UniversityResponse result = universityService.updateUniversityStatus(id, request);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete university", description = "Soft delete university (set status to DELETED)")
    public ResponseEntity<Void> deleteUniversity(@PathVariable Long id) {
        universityService.deleteUniversity(id);
        return ResponseEntity.noContent().build();
    }

}
