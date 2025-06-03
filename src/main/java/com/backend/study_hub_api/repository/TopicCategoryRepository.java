package com.backend.study_hub_api.repository;

import com.backend.study_hub_api.model.TopicCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicCategoryRepository extends JpaRepository<TopicCategory, Long> {

    @Query("SELECT tc FROM TopicCategory tc WHERE tc.topic.id = :topicId")
    List<TopicCategory> findByTopicId(@Param("topicId") Long topicId);

    @Query("SELECT tc FROM TopicCategory tc WHERE tc.category.id = :categoryId")
    List<TopicCategory> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT tc FROM TopicCategory tc WHERE tc.topic.id = :topicId AND tc.category.id = :categoryId")
    Optional<TopicCategory> findByTopicIdAndCategoryId(@Param("topicId") Long topicId,
                                                       @Param("categoryId") Long categoryId);

    boolean existsByTopicIdAndCategoryId(Long topicId, Long categoryId);

    @Modifying
    @Query("DELETE FROM TopicCategory tc WHERE tc.topic.id = :topicId AND tc.category.id = :categoryId")
    void deleteByTopicIdAndCategoryId(@Param("topicId") Long topicId, @Param("categoryId") Long categoryId);

    @Modifying
    @Query("DELETE FROM TopicCategory tc WHERE tc.topic.id = :topicId")
    void deleteByTopicId(@Param("topicId") Long topicId);

    @Modifying
    @Query("DELETE FROM TopicCategory tc WHERE tc.category.id = :categoryId")
    void deleteByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT COUNT(tc) FROM TopicCategory tc WHERE tc.category.id = :categoryId")
    long countTopicsByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT COUNT(tc) FROM TopicCategory tc WHERE tc.topic.id = :topicId")
    long countCategoriesByTopicId(@Param("topicId") Long topicId);
}