package com.backend.study_hub_api.controller;

import com.backend.study_hub_api.config.jwt.SecurityUtils;
import com.backend.study_hub_api.dto.request.CreateCommentRequest;
import com.backend.study_hub_api.dto.CommentDTO;
import com.backend.study_hub_api.service.TopicCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/comments")
@RequiredArgsConstructor
public class CommentController {

    private final TopicCommentService commentService;

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByTopic(@PathVariable Long topicId) {
        List<CommentDTO> comments = commentService.getCommentsByTopic(topicId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CreateCommentRequest request) {
        Long authorId = SecurityUtils.getCurrentUserId();
        CommentDTO comment = commentService.createComment(request, authorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        Long userId = SecurityUtils.getCurrentUserId();
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }
}