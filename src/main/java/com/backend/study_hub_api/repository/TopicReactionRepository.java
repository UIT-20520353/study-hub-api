package com.backend.study_hub_api.repository;

import com.backend.study_hub_api.helper.enumeration.ReactionType;
import com.backend.study_hub_api.model.TopicReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicReactionRepository extends JpaRepository<TopicReaction, Long> {

    List<TopicReaction> findByTopicIdInAndUserId(List<Long> topicIds, Long userId);

    Optional<TopicReaction> findByTopicIdAndUserId(Long topicId, Long userId);

    boolean existsByTopicIdAndUserId(Long topicId, Long userId);

    boolean existsByTopicIdAndUserIdAndReactionType(Long topicId, Long userId, ReactionType reactionType);

    long countByTopicIdAndReactionType(Long topicId, ReactionType reactionType);

    List<TopicReaction> findByTopicIdOrderByCreatedAtDesc(Long topicId);

    List<TopicReaction> findByUserIdOrderByCreatedAtDesc(Long userId);

    void deleteByTopicIdAndUserId(Long topicId, Long userId);

    @Query("SELECT tr.topic.id, tr.reactionType, COUNT(tr) FROM TopicReaction tr " +
           "WHERE tr.topic.id IN :topicIds " +
           "GROUP BY tr.topic.id, tr.reactionType")
    List<Object[]> getReactionCountsByTopicIds(@Param("topicIds") List<Long> topicIds);

    @Query("SELECT tr FROM TopicReaction tr " +
           "WHERE tr.topic.id IN :topicIds AND tr.user.id = :userId")
    List<TopicReaction> getUserReactionsForTopics(@Param("topicIds") List<Long> topicIds,
                                                  @Param("userId") Long userId);
}