package com.backend.study_hub_api.service;

import com.backend.study_hub_api.dto.TopicDTO;
import com.backend.study_hub_api.dto.TopicReactionDTO;
import com.backend.study_hub_api.helper.enumeration.ReactionType;
import com.backend.study_hub_api.model.TopicReaction;

import java.util.List;
import java.util.Optional;

public interface TopicReactionService {

    /**
     * Add or update reaction to a topic
     * If user already has a reaction, it will be updated
     * If user has the same reaction, it will be removed (toggle behavior)
     */
    TopicDTO.TopicResponse toggleReaction(Long topicId, ReactionType reactionType);

    /**
     * Remove reaction from a topic
     */
    TopicReactionDTO.ReactionToggleResponse removeReaction(Long topicId);

    /**
     * Get user's reaction status for a topic
     */
    TopicReactionDTO.UserReactionStatus getUserReactionStatus(Long topicId, Long userId);

    /**
     * Get user's reaction status for current authenticated user
     */
    TopicReactionDTO.UserReactionStatus getCurrentUserReactionStatus(Long topicId);

    /**
     * Get reaction summary for a topic (counts + current user's reaction)
     */
    TopicReactionDTO.TopicReactionSummary getTopicReactionSummary(Long topicId);

    /**
     * Get reaction summaries for multiple topics
     */
    List<TopicReactionDTO.TopicReactionSummary> getTopicReactionSummaries(List<Long> topicIds);

    /**
     * Get all reactions for a topic
     */
    List<TopicReactionDTO.ReactionResponse> getTopicReactions(Long topicId);

    /**
     * Get user's reactions across all topics
     */
    List<TopicReactionDTO.ReactionResponse> getUserReactions(Long userId);

    /**
     * Get current user's reactions across all topics
     */
    List<TopicReactionDTO.ReactionResponse> getCurrentUserReactions();

    // ==================== HELPER METHODS ====================

    /**
     * Get reaction entity by topic and user
     */
    Optional<TopicReaction> getReactionByTopicAndUser(Long topicId, Long userId);

    /**
     * Check if user has reacted to topic
     */
    boolean hasUserReacted(Long topicId, Long userId);

    /**
     * Get reaction counts for a topic
     */
    TopicReactionDTO.UserReactionStatus getReactionCounts(Long topicId);

    /**
     * Map TopicReaction entity to DTO
     */
    TopicReactionDTO.ReactionResponse mapToDTO(TopicReaction reaction);
}