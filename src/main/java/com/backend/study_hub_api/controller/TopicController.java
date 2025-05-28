package com.backend.study_hub_api.controller;

import com.backend.study_hub_api.dto.TopicDTO;
import com.backend.study_hub_api.dto.common.PaginationDTO;
import com.backend.study_hub_api.dto.criteria.TopicFilterCriteria;
import com.backend.study_hub_api.helper.enumeration.TopicStatus;
import com.backend.study_hub_api.helper.enumeration.TopicVisibility;
import com.backend.study_hub_api.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user/topics")
@RequiredArgsConstructor
@Tag(name = "Public Topics", description = "Public endpoints for topics")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TopicController {

    TopicService topicService;

    @PostMapping
    public ResponseEntity<TopicDTO.TopicResponse> createTopic(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "categoryId") Long categoryId,
            @RequestParam("visibility") TopicVisibility visibility,
            @RequestParam(value = "universityId", required = false) Long universityId,
            @RequestParam(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        TopicDTO.CreateTopicWithFilesRequest request = TopicDTO.CreateTopicWithFilesRequest.builder()
                .title(title)
                .content(content)
                .categoryId(categoryId)
                .visibility(visibility)
                .universityId(universityId)
                .attachments(attachments)
                .build();

        ;
        return ResponseEntity.ok(topicService.createTopic(request));
    }

    // ==================== GET TOPICS WITH ADVANCED FILTERS ====================
    @GetMapping("/filter")
    @Operation(summary = "Get topics with advanced filters", description = "Retrieve topics with comprehensive filtering options")
    public ResponseEntity<PaginationDTO<TopicDTO.TopicResponse>> getTopicsWithFilter(
            @Parameter(description = "Search keyword (searches in title, content, author name, category)")
            @RequestParam(required = false) String searchKeyword,

            @Parameter(description = "Filter by title")
            @RequestParam(required = false) String title,

            @Parameter(description = "Filter by content")
            @RequestParam(required = false) String content,

            @Parameter(description = "Filter by author ID")
            @RequestParam(required = false) Long authorId,

            @Parameter(description = "Filter by author name")
            @RequestParam(required = false) String authorName,

            @Parameter(description = "Filter by category ID")
            @RequestParam(required = false) Long categoryId,

            @Parameter(description = "Filter by category name")
            @RequestParam(required = false) String categoryName,

            @Parameter(description = "Filter by university ID")
            @RequestParam(required = false) Long universityId,

            @Parameter(description = "Filter by university name")
            @RequestParam(required = false) String universityName,

            @Parameter(description = "Filter by topic statuses")
            @RequestParam(required = false) List<TopicStatus> statuses,

            @Parameter(description = "Filter by topic visibilities")
            @RequestParam(required = false) List<TopicVisibility> visibilities,

            @Parameter(description = "Filter by locked status")
            @RequestParam(required = false) Boolean isLocked,

            @Parameter(description = "Filter topics with/without attachments")
            @RequestParam(required = false) Boolean hasAttachments,

            @Parameter(description = "Minimum view count")
            @RequestParam(required = false) Integer minViewCount,

            @Parameter(description = "Maximum view count")
            @RequestParam(required = false) Integer maxViewCount,

            @Parameter(description = "Minimum comment count")
            @RequestParam(required = false) Integer minCommentCount,

            @Parameter(description = "Maximum comment count")
            @RequestParam(required = false) Integer maxCommentCount,

            @Parameter(description = "Minimum like count")
            @RequestParam(required = false) Integer minLikeCount,

            @Parameter(description = "Maximum like count")
            @RequestParam(required = false) Integer maxLikeCount,

            @Parameter(description = "Created from date (ISO format: 2024-01-01T00:00:00)")
            @RequestParam(required = false) String createdFrom,

            @Parameter(description = "Created to date (ISO format: 2024-12-31T23:59:59)")
            @RequestParam(required = false) String createdTo,

            @Parameter(description = "Last activity from date")
            @RequestParam(required = false) String lastActivityFrom,

            @Parameter(description = "Last activity to date")
            @RequestParam(required = false) String lastActivityTo,

            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") Integer page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") Integer size,

            @Parameter(description = "Sort field (id, title, createdAt, lastActivityAt, viewCount, likeCount, commentCount)")
            @RequestParam(defaultValue = "lastActivityAt") String sortBy,

            @Parameter(description = "Sort direction (ASC, DESC)")
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        TopicFilterCriteria criteria = TopicFilterCriteria.builder()
                                                          .searchKeyword(searchKeyword)
                                                          .title(title)
                                                          .content(content)
                                                          .authorId(authorId)
                                                          .authorName(authorName)
                                                          .categoryId(categoryId)
                                                          .categoryName(categoryName)
                                                          .universityId(universityId)
                                                          .universityName(universityName)
                                                          .statuses(statuses)
                                                          .visibilities(visibilities)
                                                          .isLocked(isLocked)
                                                          .hasAttachments(hasAttachments)
                                                          .minViewCount(minViewCount)
                                                          .maxViewCount(maxViewCount)
                                                          .minCommentCount(minCommentCount)
                                                          .maxCommentCount(maxCommentCount)
                                                          .minLikeCount(minLikeCount)
                                                          .maxLikeCount(maxLikeCount)
                                                          .createdFrom(createdFrom)
                                                          .createdTo(createdTo)
                                                          .lastActivityFrom(lastActivityFrom)
                                                          .lastActivityTo(lastActivityTo)
                                                          .page(page)
                                                          .size(size)
                                                          .sortBy(sortBy)
                                                          .sortDirection(sortDirection)
                                                          .build();

        PaginationDTO<TopicDTO.TopicResponse> response = topicService.getTopicsWithFilter(criteria);
        return ResponseEntity.ok(response);
    }

    // ==================== SIMPLE GET ENDPOINTS ====================
    @GetMapping
    @Operation(summary = "Get all topics", description = "Get all active topics with basic pagination")
    public ResponseEntity<PaginationDTO<TopicDTO.TopicResponse>> getAllTopics(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "lastActivityAt") String sortBy,

            @Parameter(description = "Sort direction")
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        PaginationDTO<TopicDTO.TopicResponse> response = topicService.getAllTopics(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get topic by ID", description = "Retrieve a specific topic by its ID")
    public ResponseEntity<TopicDTO.TopicResponse> getTopicById(
            @Parameter(description = "Topic ID")
            @PathVariable Long id) {
        TopicDTO.TopicResponse response = topicService.getTopicById(id);
        return ResponseEntity.ok(response);
    }

    // ==================== SPECIAL QUERIES ====================
    @GetMapping("/popular")
    @Operation(summary = "Get popular topics", description = "Get topics sorted by view count")
    public ResponseEntity<List<TopicDTO.TopicSummaryResponse>> getPopularTopics(
            @Parameter(description = "Number of topics to return")
            @RequestParam(defaultValue = "10") int limit) {
        List<TopicDTO.TopicSummaryResponse> response = topicService.getPopularTopics(limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recent")
    @Operation(summary = "Get recent topics", description = "Get recently created topics")
    public ResponseEntity<List<TopicDTO.TopicSummaryResponse>> getRecentTopics(
            @Parameter(description = "Number of topics to return")
            @RequestParam(defaultValue = "10") int limit) {
        List<TopicDTO.TopicSummaryResponse> response = topicService.getRecentTopics(limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/trending")
    @Operation(summary = "Get trending topics", description = "Get topics with high engagement (likes + comments)")
    public ResponseEntity<List<TopicDTO.TopicSummaryResponse>> getTrendingTopics(
            @Parameter(description = "Number of topics to return")
            @RequestParam(defaultValue = "10") int limit) {
        List<TopicDTO.TopicSummaryResponse> response = topicService.getTrendingTopics(limit);
        return ResponseEntity.ok(response);
    }

    // ==================== CATEGORY & UNIVERSITY SPECIFIC ====================
    @GetMapping("/by-category/{categoryId}")
    @Operation(summary = "Get topics by category", description = "Get topics filtered by category")
    public ResponseEntity<PaginationDTO<TopicDTO.TopicResponse>> getTopicsByCategory(
            @Parameter(description = "Category ID")
            @PathVariable Long categoryId,

            @Parameter(description = "Page number")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size) {

        TopicFilterCriteria criteria = TopicFilterCriteria.builder()
                                                          .categoryId(categoryId)
                                                          .statuses(List.of(TopicStatus.ACTIVE))
                                                          .page(page)
                                                          .size(size)
                                                          .sortBy("lastActivityAt")
                                                          .sortDirection("DESC")
                                                          .build();

        PaginationDTO<TopicDTO.TopicResponse> response = topicService.getTopicsWithFilter(criteria);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-university/{universityId}")
    @Operation(summary = "Get topics by university", description = "Get topics filtered by university")
    public ResponseEntity<PaginationDTO<TopicDTO.TopicResponse>> getTopicsByUniversity(
            @Parameter(description = "University ID")
            @PathVariable Long universityId,

            @Parameter(description = "Page number")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size) {

        TopicFilterCriteria criteria = TopicFilterCriteria.builder()
                                                          .universityId(universityId)
                                                          .statuses(List.of(TopicStatus.ACTIVE))
                                                          .page(page)
                                                          .size(size)
                                                          .sortBy("lastActivityAt")
                                                          .sortDirection("DESC")
                                                          .build();

        PaginationDTO<TopicDTO.TopicResponse> response = topicService.getTopicsWithFilter(criteria);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-author/{authorId}")
    @Operation(summary = "Get topics by author", description = "Get topics created by specific author")
    public ResponseEntity<PaginationDTO<TopicDTO.TopicResponse>> getTopicsByAuthor(
            @Parameter(description = "Author ID")
            @PathVariable Long authorId,

            @Parameter(description = "Page number")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size) {

        TopicFilterCriteria criteria = TopicFilterCriteria.builder()
                                                          .authorId(authorId)
                                                          .page(page)
                                                          .size(size)
                                                          .sortBy("createdAt")
                                                          .sortDirection("DESC")
                                                          .build();

        PaginationDTO<TopicDTO.TopicResponse> response = topicService.getTopicsWithFilter(criteria);
        return ResponseEntity.ok(response);
    }

    // ==================== SEARCH ENDPOINTS ====================
    @GetMapping("/search")
    @Operation(summary = "Search topics", description = "Search topics by keyword in title, content, and author name")
    public ResponseEntity<PaginationDTO<TopicDTO.TopicResponse>> searchTopics(
            @Parameter(description = "Search keyword", required = true)
            @RequestParam String q,

            @Parameter(description = "Page number")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "lastActivityAt") String sortBy,

            @Parameter(description = "Sort direction")
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        TopicFilterCriteria criteria = TopicFilterCriteria.builder()
                                                          .searchKeyword(q)
                                                          .statuses(List.of(TopicStatus.ACTIVE))
                                                          .page(page)
                                                          .size(size)
                                                          .sortBy(sortBy)
                                                          .sortDirection(sortDirection)
                                                          .build();

        PaginationDTO<TopicDTO.TopicResponse> response = topicService.getTopicsWithFilter(criteria);
        return ResponseEntity.ok(response);
    }

}
