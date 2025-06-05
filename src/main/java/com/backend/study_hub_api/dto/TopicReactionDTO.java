package com.backend.study_hub_api.dto;

import com.backend.study_hub_api.helper.enumeration.ReactionType;
import lombok.*;

import java.time.Instant;

public class TopicReactionDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReactionRequest {
        private Long topicId;
        private ReactionType reactionType;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReactionResponse {
        private Long id;
        private Long topicId;
        private Long userId;
        private ReactionType reactionType;
        private Instant createdAt;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserReactionStatus {
        private Long topicId;
        private ReactionType userReaction; // null if user hasn't reacted
        private Integer likeCount;
        private Integer dislikeCount;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReactionToggleResponse {
        private Long topicId;
        private ReactionType newReaction; // null if reaction was removed
        private ReactionType previousReaction; // null if user hadn't reacted before
        private Integer likeCount;
        private Integer dislikeCount;
        private String message;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopicReactionSummary {
        private Long topicId;
        private Integer likeCount;
        private Integer dislikeCount;
        private ReactionType userReaction; // Current user's reaction, null if not reacted
    }
}