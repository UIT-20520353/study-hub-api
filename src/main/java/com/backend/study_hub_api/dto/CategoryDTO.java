package com.backend.study_hub_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

import static com.backend.study_hub_api.helper.constant.Message.CATEGORY_NAME_LENGTH_ERROR;
import static com.backend.study_hub_api.helper.constant.Message.CATEGORY_NAME_REQUIRED_ERROR;

public class CategoryDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Category response")
    public static class CategoryResponse {
        @Schema(description = "Category ID", example = "1")
        private Long id;

        @Schema(description = "Category name", example = "Sách Giáo Khoa")
        private String name;

        @Schema(description = "Category status", example = "true")
        private Boolean isActive;

        @Schema(description = "Creation time")
        private Instant createdAt;

        @Schema(description = "Last update time")
        private Instant updatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Create category request")
    public static class CreateCategoryRequest {
        @NotBlank(message = CATEGORY_NAME_REQUIRED_ERROR)
        @Size(max = 100, message = CATEGORY_NAME_LENGTH_ERROR)
        @Schema(description = "Category name", example = "Sách Giáo Khoa", required = true)
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Update category request")
    public static class UpdateCategoryRequest {
        @NotBlank(message = CATEGORY_NAME_REQUIRED_ERROR)
        @Size(max = 100, message = CATEGORY_NAME_LENGTH_ERROR)
        @Schema(description = "Category name", example = "Sách Tham Khảo", required = true)
        private String name;

        @Schema(description = "Category status", example = "true")
        private Boolean isActive;
    }
}