package com.backend.study_hub_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

public class FileUploadDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "File upload response")
    public static class FileUploadResponse {
        @Schema(description = "File URL", example = "https://study-hub-storage.s3.amazonaws.com/uploads/uuid-filename.jpg")
        private String fileUrl;

        @Schema(description = "Original filename", example = "my-book-image.jpg")
        private String originalFilename;

        @Schema(description = "File size in bytes", example = "2048576")
        private Long fileSize;

        @Schema(description = "Content type", example = "image/jpeg")
        private String contentType;

        @Schema(description = "Upload timestamp")
        private Instant uploadedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Multiple files upload response")
    public static class MultipleFileUploadResponse {
        @Schema(description = "List of uploaded files")
        private java.util.List<FileUploadResponse> files;

        @Schema(description = "Total files uploaded")
        private Integer totalFiles;

        @Schema(description = "Upload timestamp")
        private Instant uploadedAt;
    }
}