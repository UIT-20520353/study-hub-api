package com.backend.study_hub_api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public class TopicCategoryDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateTopicCategoriesRequest {
        @NotNull(message = "Topic ID is required")
        private Long topicId;

        @NotEmpty(message = "At least one category is required")
        private Set<Long> categoryIds;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddCategoryToTopicRequest {
        @NotNull(message = "Topic ID is required")
        private Long topicId;

        @NotNull(message = "Category ID is required")
        private Long categoryId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RemoveCategoryFromTopicRequest {
        @NotNull(message = "Topic ID is required")
        private Long topicId;

        @NotNull(message = "Category ID is required")
        private Long categoryId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopicCategoryResponse {
        private Long id;
        private Long topicId;
        private String topicTitle;
        private Long categoryId;
        private String categoryName;
        private Instant createdAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryWithTopicCountResponse {
        private Long id;
        private String name;
        private String type;
        private Boolean isActive;
        private Long topicCount;
        private Instant createdAt;
        private Instant updatedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopicWithCategoriesResponse {
        private Long id;
        private String title;
        private String content;
        private List<CategoryResponse> categories;
        private Integer viewCount;
        private Integer commentCount;
        private Integer likeCount;
        private Instant createdAt;
        private Instant updatedAt;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class CategoryResponse {
            private Long id;
            private String name;
            private String type;
        }
    }
}