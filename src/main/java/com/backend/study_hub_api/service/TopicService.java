package com.backend.study_hub_api.service;

import com.backend.study_hub_api.dto.TopicDTO;
import com.backend.study_hub_api.dto.common.PaginationDTO;
import com.backend.study_hub_api.dto.criteria.TopicFilterCriteria;
import com.backend.study_hub_api.model.Topic;
import com.backend.study_hub_api.model.TopicReaction;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TopicService {

    // ==================== CREATE OPERATIONS ====================

    TopicDTO.TopicResponse createTopic(TopicDTO.CreateTopicWithFilesRequest request);

    // ==================== READ OPERATIONS WITH FILTERS ====================

    PaginationDTO<TopicDTO.TopicResponse> getTopicsWithFilter(TopicFilterCriteria criteria);
    PaginationDTO<TopicDTO.TopicResponse> getAllTopics(Pageable pageable);
    TopicDTO.TopicResponse getTopicById(Long id);
    List<TopicDTO.TopicResponse> getTop10Topics();

    // ==================== SPECIAL QUERIES ====================

    List<TopicDTO.TopicSummaryResponse> getPopularTopics(int limit);
    List<TopicDTO.TopicSummaryResponse> getRecentTopics(int limit);
    List<TopicDTO.TopicSummaryResponse> getTrendingTopics(int limit);

    // ==================== HELPER METHODS ====================

    Topic getTopicByIdOrThrow(Long id);
    TopicDTO.TopicResponse mapToDTO(Topic topic);
    TopicDTO.TopicResponse mapToDTO(Topic topic, TopicReaction userReaction);

    // ==================== UPDATE OPERATIONS ====================
    void deleteTopic(Long id);
}