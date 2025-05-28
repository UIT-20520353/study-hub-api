package com.backend.study_hub_api.dto;

import com.backend.study_hub_api.helper.enumeration.TopicStatus;
import com.backend.study_hub_api.helper.enumeration.TopicVisibility;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

public class TopicDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateTopicRequest {

        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must not exceed 255 characters")
        private String title;

        @NotBlank(message = "Content is required")
        private String content;

        private Long categoryId;

        @NotNull(message = "Visibility is required")
        private TopicVisibility visibility;

        private Long universityId;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateTopicWithFilesRequest {

        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must not exceed 255 characters")
        private String title;

        @NotBlank(message = "Content is required")
        private String content;

        private Long categoryId;

        @NotNull(message = "Visibility is required")
        private TopicVisibility visibility;

        private Long universityId;

        private List<MultipartFile> attachments;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateTopicRequest {

        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must not exceed 255 characters")
        private String title;

        @NotBlank(message = "Content is required")
        private String content;

        private Long categoryId;

        private TopicVisibility visibility;

        private Long universityId;

        private TopicStatus status;

        private Boolean isLocked;

        private List<AttachmentRequest> attachments;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttachmentRequest {

        @NotBlank(message = "File URL is required")
        private String fileUrl;

        @NotBlank(message = "File name is required")
        private String fileName;

        private String fileType;

        private Integer fileSize;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopicResponse {

        private Long id;
        private String title;
        private String content;
        private Integer viewCount;
        private Integer commentCount;
        private Integer likeCount;
        private Integer dislikeCount;
        private TopicStatus status;
        private Boolean isLocked;
        private TopicVisibility visibility;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant createdAt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant updatedAt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant lastActivityAt;

        // Author information
        private AuthorInfo author;

        // Category information
        private CategoryInfo category;

        // University information
        private UniversityInfo university;

        // Attachments
        private List<AttachmentResponse> attachments;

        // User interaction info (if user is authenticated)
        private UserInteractionInfo userInteraction;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopicSummaryResponse {

        private Long id;
        private String title;
        private String contentPreview; // First 200 characters
        private Integer viewCount;
        private Integer commentCount;
        private Integer likeCount;
        private Integer dislikeCount;
        private TopicStatus status;
        private TopicVisibility visibility;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant createdAt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant lastActivityAt;

        private AuthorInfo author;
        private CategoryInfo category;
        private UniversityInfo university;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorInfo {
        private Long id;
        private String fullName;
        private String avatarUrl;
        private String university;
        private String major;
        private Integer year;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryInfo {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UniversityInfo {
        private Long id;
        private String name;
        private String shortName;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttachmentResponse {
        private Long id;
        private String fileUrl;
        private String fileName;
        private String fileType;
        private Integer fileSize;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant createdAt;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInteractionInfo {
        private Boolean isFollowing;
        private String userReaction; // LIKE, DISLIKE, or null
        private Boolean isAuthor;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReactTopicRequest {

        @NotNull(message = "Reaction type is required")
        private String reactionType; // LIKE or DISLIKE
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileInfo {
        private String fileName;
        private String fileUrl;
        private String fileType;
        private Long fileSize;
    }
}