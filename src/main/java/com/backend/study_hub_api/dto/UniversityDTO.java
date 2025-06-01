package com.backend.study_hub_api.dto;

import com.backend.study_hub_api.helper.enumeration.UniversityStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

import static com.backend.study_hub_api.helper.constant.Message.*;

public class UniversityDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateUniversityRequest {
        @NotBlank(message = UNIVERSITY_NAME_REQUIRED_ERROR)
        @Size(max = 100, message = UNIVERSITY_NAME_MAX_LENGTH_ERROR)
        private String name;

        @Size(max = 20, message = UNIVERSITY_SHORT_NAME_MAX_LENGTH_ERROR)
        @NotBlank(message = UNIVERSITY_SHORT_NAME_REQUIRED_ERROR)
        private String shortName;

        @Size(max = 500, message = UNIVERSITY_ADDRESS_MAX_LENGTH_ERROR)
        private String address;

        @NotBlank(message = UNIVERSITY_EMAIL_DOMAIN_REQUIRED_ERROR)
        @Size(max = 100, message = UNIVERSITY_EMAIL_DOMAIN_MAX_LENGTH_ERROR)
        private String emailDomain;

        @Size(max = 50, message = UNIVERSITY_CITY_MAX_LENGTH_ERROR)
        private String city;

        @Size(max = 100, message = UNIVERSITY_WEBSITE_MAX_LENGTH_ERROR)
        private String website;

        @Size(max = 1000, message = UNIVERSITY_DESCRIPTION_MAX_LENGTH_ERROR)
        private String description;

        @NotNull(message = UNIVERSITY_STATUS_REQUIRED_ERROR)
        private UniversityStatus status = UniversityStatus.ACTIVE;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateUniversityRequest {
        @NotBlank(message = UNIVERSITY_NAME_REQUIRED_ERROR)
        @Size(max = 100, message = UNIVERSITY_NAME_MAX_LENGTH_ERROR)
        private String name;

        @Size(max = 20, message = UNIVERSITY_SHORT_NAME_MAX_LENGTH_ERROR)
        private String shortName;

        @Size(max = 500, message = UNIVERSITY_ADDRESS_MAX_LENGTH_ERROR)
        private String address;

        @NotBlank(message = UNIVERSITY_EMAIL_DOMAIN_REQUIRED_ERROR)
        @Size(max = 100, message = UNIVERSITY_EMAIL_DOMAIN_MAX_LENGTH_ERROR)
        private String emailDomain;

        @Size(max = 50, message = UNIVERSITY_CITY_MAX_LENGTH_ERROR)
        private String city;

        @Size(max = 100, message = UNIVERSITY_WEBSITE_MAX_LENGTH_ERROR)
        private String website;

        @Size(max = 1000, message = UNIVERSITY_DESCRIPTION_MAX_LENGTH_ERROR)
        private String description;

        private UniversityStatus status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateStatusRequest {
        @NotNull(message = UNIVERSITY_STATUS_REQUIRED_ERROR)
        private UniversityStatus status;
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
        private String emailDomain;
        private String city;
        private String website;
        private String logoUrl;
        private String description;
        private UniversityStatus status;
        private Boolean isActive;
        private Instant createdAt;
        private Instant updatedAt;
    }
}