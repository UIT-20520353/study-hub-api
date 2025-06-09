package com.backend.study_hub_api.dto;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;

@Data
@Builder
public class CommentDTO {
    private Long id;
    private Long topicId;
    private String content;
    private Integer likeCount;
    private Integer dislikeCount;
    private Instant createdAt;
    private AuthorInfo author;

    @Data
    @Builder
    public static class AuthorInfo {
        private Long id;
        private String fullName;
        private String avatarUrl;
    }
}