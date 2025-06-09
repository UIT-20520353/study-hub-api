package com.backend.study_hub_api.service.impl;

import com.backend.study_hub_api.dto.CommentDTO;
import com.backend.study_hub_api.dto.request.CreateCommentRequest;
import com.backend.study_hub_api.helper.exception.BadRequestException;
import com.backend.study_hub_api.model.Topic;
import com.backend.study_hub_api.model.TopicComment;
import com.backend.study_hub_api.model.User;
import com.backend.study_hub_api.repository.TopicCommentRepository;
import com.backend.study_hub_api.repository.TopicRepository;
import com.backend.study_hub_api.service.TopicCommentService;
import com.backend.study_hub_api.service.TopicService;
import com.backend.study_hub_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.backend.study_hub_api.helper.constant.Message.COMMENT_NOT_FOUND_ERROR;
import static com.backend.study_hub_api.helper.constant.Message.ONLY_COMMENT_AUTHOR_CAN_DELETE;

@Service
@RequiredArgsConstructor
public class TopicCommentServiceImpl implements TopicCommentService {

    private final TopicCommentRepository commentRepository;
    private final TopicService topicService;
    private final UserService userService;
    private final TopicRepository topicRepository;

    @Override
    public List<CommentDTO> getCommentsByTopic(Long topicId) {
        List<TopicComment> comments = commentRepository.findByTopicIdAndNotDeletedOrderByCreatedAt(topicId);
        return comments.stream()
                       .map(this::mapToDTO)
                       .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDTO createComment(CreateCommentRequest request, Long authorId) {
        Topic topic = topicService.getTopicByIdOrThrow(request.getTopicId());
        User author = userService.getUserByIdOrThrow(authorId);

        TopicComment comment = TopicComment.builder()
                                           .topic(topic)
                                           .author(author)
                                           .content(request.getContent())
                                           .likeCount(0)
                                           .dislikeCount(0)
                                           .isDeleted(false)
                                           .build();

        comment = commentRepository.save(comment);

        topic.setCommentCount(topic.getCommentCount() + 1);
        topicRepository.save(topic);

        return mapToDTO(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        TopicComment comment = getCommentByIdOrThrow(commentId);

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new BadRequestException(ONLY_COMMENT_AUTHOR_CAN_DELETE);
        }

        comment.setIsDeleted(true);
        commentRepository.save(comment);

        Topic topic = comment.getTopic();
        if (topic.getCommentCount() > 0) {
            topic.setCommentCount(topic.getCommentCount() - 1);
            topicRepository.save(topic);
        }
    }

    @Override
    public CommentDTO mapToDTO(TopicComment comment) {
        return CommentDTO.builder()
                         .id(comment.getId())
                         .topicId(comment.getTopic().getId())
                         .content(comment.getContent())
                         .likeCount(comment.getLikeCount())
                         .dislikeCount(comment.getDislikeCount())
                         .createdAt(comment.getCreatedAt())
                         .author(CommentDTO.AuthorInfo.builder()
                                                      .id(comment.getAuthor().getId())
                                                      .fullName(comment.getAuthor().getFullName())
                                                      .avatarUrl(comment.getAuthor().getAvatarUrl())
                                                      .build())
                         .build();
    }

    @Override
    public TopicComment getCommentByIdOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                                .orElseThrow(() -> new BadRequestException(COMMENT_NOT_FOUND_ERROR));
    }
}