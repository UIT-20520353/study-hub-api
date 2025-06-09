package com.backend.study_hub_api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class TopicCommentDTO {
    private Long id;
    private Long topicId;
    private AuthorDTO author;
    private String content;
    private Integer likeCount;
    private Integer dislikeCount;
    private Boolean isDeleted;
    private Instant createdAt;
    private Instant updatedAt;
    private String userReaction; // LIKE, DISLIKE, or null

    @Data
    @Builder
    public static class AuthorDTO {
        private Long id;
        private String fullName;
        private String avatarUrl;
        private String major;
        private Integer year;
    }
}