package com.backend.study_hub_api.service;

import com.backend.study_hub_api.dto.CommentDTO;
import com.backend.study_hub_api.dto.request.CreateCommentRequest;
import com.backend.study_hub_api.model.TopicComment;

import java.util.List;

public interface TopicCommentService {

    List<CommentDTO> getCommentsByTopic(Long topicId);
    CommentDTO createComment(CreateCommentRequest request, Long authorId);
    void deleteComment(Long commentId, Long userId);
    CommentDTO mapToDTO(TopicComment comment);
    TopicComment getCommentByIdOrThrow(Long commentId);

}