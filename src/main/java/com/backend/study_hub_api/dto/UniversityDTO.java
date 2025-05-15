package com.backend.study_hub_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

import static com.backend.study_hub_api.helper.constant.Message.*;

public class UniversityDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateUniversityRequest {
        @NotBlank(message = UNIVERSITY_NAME_REQUIRED_ERROR)
        private String name;

        private String shortName;
        private String address;
        private String city;
        private String website;
        private String logoUrl;
        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateUniversityRequest {
        @NotBlank(message = UNIVERSITY_NAME_REQUIRED_ERROR)
        private String name;

        private String shortName;
        private String address;
        private String city;
        private String website;
        private String logoUrl;
        private String description;
        private Boolean isActive;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UniversityResponse {
        private Long id;
        private String name;
        private String shortName;
        private String address;
        private String city;
        private String website;
        private String logoUrl;
        private String description;
        private Boolean isActive;
        private Instant createdAt;
        private Instant updatedAt;
    }
}