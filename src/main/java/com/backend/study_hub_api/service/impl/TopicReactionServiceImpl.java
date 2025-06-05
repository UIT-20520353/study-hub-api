package com.backend.study_hub_api.service.impl;

import com.backend.study_hub_api.config.jwt.SecurityUtils;
import com.backend.study_hub_api.dto.TopicDTO;
import com.backend.study_hub_api.dto.TopicReactionDTO;
import com.backend.study_hub_api.helper.enumeration.ReactionType;
import com.backend.study_hub_api.helper.exception.BadRequestException;
import com.backend.study_hub_api.model.Topic;
import com.backend.study_hub_api.model.TopicReaction;
import com.backend.study_hub_api.model.User;
import com.backend.study_hub_api.repository.TopicReactionRepository;
import com.backend.study_hub_api.repository.TopicRepository;
import com.backend.study_hub_api.service.TopicReactionService;
import com.backend.study_hub_api.service.TopicService;
import com.backend.study_hub_api.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.backend.study_hub_api.helper.constant.Message.USER_NOT_AUTHENTICATED_ERROR;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TopicReactionServiceImpl implements TopicReactionService {

    TopicReactionRepository topicReactionRepository;
    TopicRepository topicRepository;
    UserService userService;
    TopicService topicService;

    @Override
    @Transactional
    public TopicDTO.TopicResponse toggleReaction(Long topicId, ReactionType reactionType) {
        Long userId = getCurrentUserId();
        User user = userService.getUserByIdOrThrow(userId);
        Topic topic = getTopicByIdOrThrow(topicId);

        Optional<TopicReaction> existingReaction = topicReactionRepository.findByTopicIdAndUserId(topicId, userId);

        ReactionType previousReaction = null;
        TopicReaction userReaction = null;

        if (existingReaction.isPresent()) {
            TopicReaction reaction = existingReaction.get();
            previousReaction = reaction.getReactionType();

            if (previousReaction == reactionType) {
                topicReactionRepository.delete(reaction);
                updateTopicReactionCount(topic, previousReaction, false);
            } else {
                updateTopicReactionCount(topic, previousReaction, false);
                updateTopicReactionCount(topic, reactionType, true);

                reaction.setReactionType(reactionType);
                userReaction = topicReactionRepository.save(reaction);
            }
        } else {
            TopicReaction newReactionEntity = TopicReaction.builder()
                                                           .topic(topic)
                                                           .user(user)
                                                           .reactionType(reactionType)
                                                           .build();

            userReaction = topicReactionRepository.save(newReactionEntity);
            updateTopicReactionCount(topic, reactionType, true);
        }

        topicRepository.save(topic);

        return topicService.mapToDTO(topic, userReaction);
    }

    @Override
    @Transactional
    public TopicReactionDTO.ReactionToggleResponse removeReaction(Long topicId) {
        Long userId = getCurrentUserId();
        Topic topic = getTopicByIdOrThrow(topicId);

        Optional<TopicReaction> existingReaction = topicReactionRepository.findByTopicIdAndUserId(topicId, userId);

        if (existingReaction.isEmpty()) {
            throw new BadRequestException("No reaction found to remove");
        }

        TopicReaction reaction = existingReaction.get();
        ReactionType previousReaction = reaction.getReactionType();

        topicReactionRepository.delete(reaction);
        updateTopicReactionCount(topic, previousReaction, false);
        topicRepository.save(topic);

        return TopicReactionDTO.ReactionToggleResponse.builder()
                                                      .topicId(topicId)
                                                      .newReaction(null)
                                                      .previousReaction(previousReaction)
                                                      .likeCount(topic.getLikeCount())
                                                      .dislikeCount(topic.getDislikeCount())
                                                      .message("Reaction removed")
                                                      .build();
    }

    @Override
    public TopicReactionDTO.UserReactionStatus getUserReactionStatus(Long topicId, Long userId) {
        Topic topic = getTopicByIdOrThrow(topicId);
        Optional<TopicReaction> reaction = topicReactionRepository.findByTopicIdAndUserId(topicId, userId);

        return TopicReactionDTO.UserReactionStatus.builder()
                                                  .topicId(topicId)
                                                  .userReaction(reaction.map(TopicReaction::getReactionType).orElse(null))
                                                  .likeCount(topic.getLikeCount())
                                                  .dislikeCount(topic.getDislikeCount())
                                                  .build();
    }

    @Override
    public TopicReactionDTO.UserReactionStatus getCurrentUserReactionStatus(Long topicId) {
        Long userId = getCurrentUserId();
        return getUserReactionStatus(topicId, userId);
    }

    @Override
    public TopicReactionDTO.TopicReactionSummary getTopicReactionSummary(Long topicId) {
        Long userId = getCurrentUserIdOrNull();
        Topic topic = getTopicByIdOrThrow(topicId);

        ReactionType userReaction = null;
        if (userId != null) {
            Optional<TopicReaction> reaction = topicReactionRepository.findByTopicIdAndUserId(topicId, userId);
            userReaction = reaction.map(TopicReaction::getReactionType).orElse(null);
        }

        return TopicReactionDTO.TopicReactionSummary.builder()
                                                    .topicId(topicId)
                                                    .likeCount(topic.getLikeCount())
                                                    .dislikeCount(topic.getDislikeCount())
                                                    .userReaction(userReaction)
                                                    .build();
    }

    @Override
    public List<TopicReactionDTO.TopicReactionSummary> getTopicReactionSummaries(List<Long> topicIds) {
        if (topicIds == null || topicIds.isEmpty()) {
            return Collections.emptyList();
        }

        Long userId = getCurrentUserIdOrNull();

        // Get topics
        List<Topic> topics = topicRepository.findAllById(topicIds);
        Map<Long, Topic> topicMap = topics.stream()
                                          .collect(Collectors.toMap(Topic::getId, topic -> topic));

        // Get user's reactions if authenticated
        Map<Long, ReactionType> userReactionsMap = new HashMap<>();
        if (userId != null) {
            List<TopicReaction> userReactions = topicReactionRepository.getUserReactionsForTopics(topicIds, userId);
            userReactionsMap = userReactions.stream()
                                            .collect(Collectors.toMap(
                                                    reaction -> reaction.getTopic().getId(),
                                                    TopicReaction::getReactionType
                                            ));
        }

        // Build response
        List<TopicReactionDTO.TopicReactionSummary> summaries = new ArrayList<>();
        final Map<Long, ReactionType> finalUserReactionsMap = userReactionsMap;

        for (Long topicId : topicIds) {
            Topic topic = topicMap.get(topicId);
            if (topic != null) {
                summaries.add(TopicReactionDTO.TopicReactionSummary.builder()
                                                                   .topicId(topicId)
                                                                   .likeCount(topic.getLikeCount())
                                                                   .dislikeCount(topic.getDislikeCount())
                                                                   .userReaction(finalUserReactionsMap.get(topicId))
                                                                   .build());
            }
        }

        return summaries;
    }

    @Override
    public List<TopicReactionDTO.ReactionResponse> getTopicReactions(Long topicId) {
        List<TopicReaction> reactions = topicReactionRepository.findByTopicIdOrderByCreatedAtDesc(topicId);
        return reactions.stream()
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
    }

    @Override
    public List<TopicReactionDTO.ReactionResponse> getUserReactions(Long userId) {
        List<TopicReaction> reactions = topicReactionRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return reactions.stream()
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
    }

    @Override
    public List<TopicReactionDTO.ReactionResponse> getCurrentUserReactions() {
        Long userId = getCurrentUserId();
        return getUserReactions(userId);
    }

    // ==================== HELPER METHODS ====================

    @Override
    public Optional<TopicReaction> getReactionByTopicAndUser(Long topicId, Long userId) {
        return topicReactionRepository.findByTopicIdAndUserId(topicId, userId);
    }

    @Override
    public boolean hasUserReacted(Long topicId, Long userId) {
        return topicReactionRepository.existsByTopicIdAndUserId(topicId, userId);
    }

    @Override
    public TopicReactionDTO.UserReactionStatus getReactionCounts(Long topicId) {
        Topic topic = getTopicByIdOrThrow(topicId);
        return TopicReactionDTO.UserReactionStatus.builder()
                                                  .topicId(topicId)
                                                  .userReaction(null)
                                                  .likeCount(topic.getLikeCount())
                                                  .dislikeCount(topic.getDislikeCount())
                                                  .build();
    }

    @Override
    public TopicReactionDTO.ReactionResponse mapToDTO(TopicReaction reaction) {
        return TopicReactionDTO.ReactionResponse.builder()
                                                .id(reaction.getId())
                                                .topicId(reaction.getTopic().getId())
                                                .userId(reaction.getUser().getId())
                                                .reactionType(reaction.getReactionType())
                                                .createdAt(reaction.getCreatedAt())
                                                .build();
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private Long getCurrentUserId() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BadRequestException(USER_NOT_AUTHENTICATED_ERROR);
        }
        return userId;
    }

    private Long getCurrentUserIdOrNull() {
        return SecurityUtils.getCurrentUserId();
    }

    private Topic getTopicByIdOrThrow(Long topicId) {
        return topicRepository.findById(topicId)
                              .orElseThrow(() -> new BadRequestException("Topic not found with ID: " + topicId));
    }

    private void updateTopicReactionCount(Topic topic, ReactionType reactionType, boolean increment) {
        switch (reactionType) {
            case LIKE:
                if (increment) {
                    topic.incrementLikeCount();
                } else {
                    topic.decrementLikeCount();
                }
                break;
            case DISLIKE:
                if (increment) {
                    topic.incrementDislikeCount();
                } else {
                    topic.decrementDislikeCount();
                }
                break;
        }
    }
}