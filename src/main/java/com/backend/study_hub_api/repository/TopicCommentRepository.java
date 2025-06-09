package com.backend.study_hub_api.repository;

import com.backend.study_hub_api.model.TopicComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicCommentRepository extends JpaRepository<TopicComment, Long>, JpaSpecificationExecutor<TopicComment> {

    @Query("SELECT c FROM TopicComment c WHERE c.id = :id AND c.isDeleted = false")
    Optional<TopicComment> findByIdAndNotDeleted(@Param("id") Long id);

    @Query("SELECT c FROM TopicComment c " +
           "LEFT JOIN FETCH c.author " +
           "WHERE c.topic.id = :topicId AND c.isDeleted = false " +
           "ORDER BY c.createdAt ASC")
    List<TopicComment> findByTopicIdAndNotDeletedOrderByCreatedAt(@Param("topicId") Long topicId);
}