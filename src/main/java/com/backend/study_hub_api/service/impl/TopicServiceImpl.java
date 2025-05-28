package com.backend.study_hub_api.service.impl;

import com.backend.study_hub_api.config.jwt.SecurityUtils;
import com.backend.study_hub_api.dto.FileUploadDTO;
import com.backend.study_hub_api.dto.TopicDTO;
import com.backend.study_hub_api.dto.common.PaginationDTO;
import com.backend.study_hub_api.dto.criteria.TopicFilterCriteria;
import com.backend.study_hub_api.helper.enumeration.TopicStatus;
import com.backend.study_hub_api.helper.enumeration.TopicVisibility;
import com.backend.study_hub_api.helper.exception.BadRequestException;
import com.backend.study_hub_api.helper.util.PaginationUtils;
import com.backend.study_hub_api.model.*;
import com.backend.study_hub_api.repository.TopicAttachmentRepository;
import com.backend.study_hub_api.repository.TopicRepository;
import com.backend.study_hub_api.service.*;
import com.backend.study_hub_api.specification.TopicSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.backend.study_hub_api.helper.constant.Message.*;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    UserService userService;
    CategoryService categoryService;
    UniversityService universityService;
    FileUploadService fileUploadService;
    TopicRepository topicRepository;
    TopicAttachmentRepository topicAttachmentRepository;
    TopicSpecification topicSpecification;

    private static final String[] ALLOWED_FILE_TYPES = {
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/pdf",
            "image/jpeg",
            "image/jpg",
            "image/png",
            "video/mp4"
    };

    private static final int MAX_FILE_SIZE_MB = 10;
    private static final int MAX_FILES_COUNT = 10;

    // ==================== CREATE TOPIC ====================
    @Override
    @Transactional
    public TopicDTO.TopicResponse createTopic(TopicDTO.CreateTopicWithFilesRequest request) {
        log.info("Creating new topic with title: {}", request.getTitle());

        User currentUser = getCurrentAuthenticatedUser();
        Category category = getCategoryForTopic(request.getCategoryId());
        University university = getUniversityForTopic(request);

        validateFileAttachments(request.getAttachments());

        List<FileUploadDTO.FileUploadResponse> uploadedFiles = uploadTopicAttachments(request.getAttachments());

        try {
            Topic topic = createAndSaveTopic(request, currentUser, category, university);
            saveTopicAttachments(topic, uploadedFiles);
            TopicDTO.TopicResponse response = mapToDTO(topic);

            log.info("Successfully created topic with ID: {}", topic.getId());
            return response;

        } catch (Exception e) {
            log.error("Error creating topic, cleaning up uploaded files", e);
            cleanupUploadedFiles(uploadedFiles);
            throw e;
        }
    }

    // ==================== GET TOPICS WITH FILTERS ====================
    @Override
    public PaginationDTO<TopicDTO.TopicResponse> getTopicsWithFilter(TopicFilterCriteria criteria) {
        // Build specification
        Specification<Topic> specification = topicSpecification.build(criteria);

        // Create pageable
        Sort.Direction direction = Sort.Direction.fromString(criteria.getSortDirection());
        Pageable pageable = PageRequest.of(
                criteria.getPage(),
                criteria.getSize(),
                Sort.by(direction, criteria.getSortBy())
        );

        // Execute query
        Page<Topic> topicPage = topicRepository.findAll(specification, pageable);

        // Map to DTOs
        Page<TopicDTO.TopicResponse> responsePage = topicPage.map(this::mapToDTO);

        return PaginationUtils.createPaginationResponse(responsePage);
    }

    @Override
    public PaginationDTO<TopicDTO.TopicResponse> getAllTopics(Pageable pageable) {
        return getTopicsWithFilter(TopicFilterCriteria.builder()
                                                      .statuses(List.of(TopicStatus.ACTIVE))
                                                      .page(pageable.getPageNumber())
                                                      .size(pageable.getPageSize())
                                                      .build());
    }

    @Override
    public TopicDTO.TopicResponse getTopicById(Long id) {
        Topic topic = getTopicByIdOrThrow(id);
        return mapToDTO(topic);
    }

    @Override
    public List<TopicDTO.TopicSummaryResponse> getPopularTopics(int limit) {
        TopicFilterCriteria criteria = TopicFilterCriteria.builder()
                                                          .statuses(List.of(TopicStatus.ACTIVE))
                                                          .sortBy("viewCount")
                                                          .sortDirection("DESC")
                                                          .size(limit)
                                                          .build();

        return getTopicsWithFilter(criteria).getItems()
                                            .stream()
                                            .map(this::mapToSummaryDTO)
                                            .collect(Collectors.toList());
    }

    @Override
    public List<TopicDTO.TopicSummaryResponse> getRecentTopics(int limit) {
        TopicFilterCriteria criteria = TopicFilterCriteria.builder()
                                                          .statuses(List.of(TopicStatus.ACTIVE))
                                                          .sortBy("createdAt")
                                                          .sortDirection("DESC")
                                                          .size(limit)
                                                          .build();

        return getTopicsWithFilter(criteria).getItems()
                                            .stream()
                                            .map(this::mapToSummaryDTO)
                                            .collect(Collectors.toList());
    }

    @Override
    public List<TopicDTO.TopicSummaryResponse> getTrendingTopics(int limit) {
        TopicFilterCriteria criteria = TopicFilterCriteria.builder()
                                                          .statuses(List.of(TopicStatus.ACTIVE))
                                                          .minLikeCount(1)
                                                          .sortBy("likeCount")
                                                          .sortDirection("DESC")
                                                          .size(limit)
                                                          .build();

        return getTopicsWithFilter(criteria).getItems()
                                            .stream()
                                            .map(this::mapToSummaryDTO)
                                            .collect(Collectors.toList());
    }

    // ==================== HELPER METHODS ====================
    @Override
    public TopicDTO.TopicResponse mapToDTO(Topic topic) {
        return TopicDTO.TopicResponse.builder()
                                     .id(topic.getId())
                                     .title(topic.getTitle())
                                     .content(topic.getContent())
                                     .viewCount(topic.getViewCount())
                                     .commentCount(topic.getCommentCount())
                                     .likeCount(topic.getLikeCount())
                                     .dislikeCount(topic.getDislikeCount())
                                     .status(topic.getStatus())
                                     .isLocked(topic.getIsLocked())
                                     .visibility(topic.getVisibility())
                                     .createdAt(topic.getCreatedAt())
                                     .updatedAt(topic.getUpdatedAt())
                                     .lastActivityAt(topic.getLastActivityAt())
                                     .author(mapToAuthorInfo(topic.getAuthor()))
                                     .category(mapToCategoryInfo(topic.getCategory()))
                                     .university(topic.getUniversity() != null ? mapToUniversityInfo(topic.getUniversity()) : null)
                                     .attachments(mapToAttachmentResponses(topic.getAttachments()))
                                     .build();
    }

    private TopicDTO.TopicSummaryResponse mapToSummaryDTO(TopicDTO.TopicResponse topic) {
        String contentPreview = topic.getContent().length() > 200
                ? topic.getContent().substring(0, 200) + "..."
                : topic.getContent();

        return TopicDTO.TopicSummaryResponse.builder()
                                            .id(topic.getId())
                                            .title(topic.getTitle())
                                            .contentPreview(contentPreview)
                                            .viewCount(topic.getViewCount())
                                            .commentCount(topic.getCommentCount())
                                            .likeCount(topic.getLikeCount())
                                            .dislikeCount(topic.getDislikeCount())
                                            .status(topic.getStatus())
                                            .visibility(topic.getVisibility())
                                            .createdAt(topic.getCreatedAt())
                                            .lastActivityAt(topic.getLastActivityAt())
                                            .author(topic.getAuthor())
                                            .category(topic.getCategory())
                                            .university(topic.getUniversity())
                                            .build();
    }

    @Override
    public Topic getTopicByIdOrThrow(Long id) {
        return topicRepository.findById(id)
                              .orElseThrow(() -> new BadRequestException("Topic not found"));
    }

    // ==================== MAPPING HELPERS ====================
    private TopicDTO.AuthorInfo mapToAuthorInfo(User author) {
        return TopicDTO.AuthorInfo.builder()
                                  .id(author.getId())
                                  .fullName(author.getFullName())
                                  .avatarUrl(author.getAvatarUrl())
                                  .university(author.getUniversity())
                                  .major(author.getMajor())
                                  .year(author.getYear())
                                  .build();
    }

    private TopicDTO.CategoryInfo mapToCategoryInfo(Category category) {
        return TopicDTO.CategoryInfo.builder()
                                    .id(category.getId())
                                    .name(category.getName())
                                    .build();
    }

    private TopicDTO.UniversityInfo mapToUniversityInfo(University university) {
        return TopicDTO.UniversityInfo.builder()
                                      .id(university.getId())
                                      .name(university.getName())
                                      .shortName(university.getShortName())
                                      .build();
    }

    private List<TopicDTO.AttachmentResponse> mapToAttachmentResponses(List<TopicAttachment> attachments) {
        if (CollectionUtils.isEmpty(attachments)) {
            return Collections.emptyList();
        }

        return attachments.stream()
                          .map(attachment -> TopicDTO.AttachmentResponse.builder()
                                                                        .id(attachment.getId())
                                                                        .fileUrl(attachment.getFileUrl())
                                                                        .fileName(attachment.getFileName())
                                                                        .fileType(attachment.getFileType())
                                                                        .fileSize(attachment.getFileSize() != null ? attachment.getFileSize().intValue() : null)
                                                                        .createdAt(attachment.getCreatedAt())
                                                                        .build())
                          .collect(Collectors.toList());
    }

    // ==================== CREATE TOPIC HELPER METHODS ====================
    private User getCurrentAuthenticatedUser() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BadRequestException(USER_NOT_AUTHENTICATED_ERROR);
        }
        return userService.getUserByIdOrThrow(userId);
    }

    private Category getCategoryForTopic(Long categoryId) {
        if (categoryId == null) {
            throw new BadRequestException("Category ID is required");
        }
        return categoryService.getCategoryByIdOrThrow(categoryId);
    }

    private University getUniversityForTopic(TopicDTO.CreateTopicWithFilesRequest request) {
        if (request.getVisibility() == TopicVisibility.UNIVERSITY_ONLY && request.getUniversityId() == null) {
            throw new BadRequestException(TOPIC_UNIVERSITY_ID_REQUIRED_ERROR);
        }

        return request.getUniversityId() != null
                ? universityService.getUniversityByIdOrThrow(request.getUniversityId())
                : null;
    }

    private void validateFileAttachments(List<MultipartFile> attachments) {
        if (CollectionUtils.isEmpty(attachments)) {
            return;
        }

        if (attachments.size() > MAX_FILES_COUNT) {
            throw new BadRequestException("Maximum " + MAX_FILES_COUNT + " files allowed");
        }

        for (MultipartFile file : attachments) {
            if (file.isEmpty()) {
                continue;
            }

            if (!isValidFileType(file)) {
                throw new BadRequestException(
                        String.format("File type '%s' is not supported for file '%s'",
                                      file.getContentType(), file.getOriginalFilename())
                );
            }

            if (file.getSize() > MAX_FILE_SIZE_MB * 1024L * 1024L) {
                throw new BadRequestException(
                        String.format("File '%s' exceeds maximum size of %dMB",
                                      file.getOriginalFilename(), MAX_FILE_SIZE_MB)
                );
            }
        }
    }

    private boolean isValidFileType(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null &&
               Arrays.stream(ALLOWED_FILE_TYPES)
                     .anyMatch(type -> type.equalsIgnoreCase(contentType));
    }

    private List<FileUploadDTO.FileUploadResponse> uploadTopicAttachments(List<MultipartFile> attachments) {
        if (CollectionUtils.isEmpty(attachments)) {
            return Collections.emptyList();
        }

        List<MultipartFile> validFiles = attachments.stream()
                                                    .filter(file -> !file.isEmpty())
                                                    .collect(Collectors.toList());

        if (validFiles.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            FileUploadDTO.MultipleFileUploadResponse uploadResponse =
                    fileUploadService.uploadMultipleFiles(validFiles, "topics", ALLOWED_FILE_TYPES);

            return uploadResponse.getFiles();

        } catch (Exception e) {
            log.error("Failed to upload topic attachments", e);
            throw new BadRequestException("Failed to upload attachments: " + e.getMessage());
        }
    }

    private Topic createAndSaveTopic(TopicDTO.CreateTopicWithFilesRequest request,
                                     User author, Category category, University university) {
        Topic topic = Topic.builder()
                           .title(request.getTitle().trim())
                           .content(request.getContent().trim())
                           .category(category)
                           .visibility(request.getVisibility())
                           .university(university)
                           .status(TopicStatus.ACTIVE)
                           .viewCount(0)
                           .commentCount(0)
                           .likeCount(0)
                           .dislikeCount(0)
                           .isLocked(false)
                           .author(author)
                           .build();

        return topicRepository.save(topic);
    }

    private void saveTopicAttachments(Topic topic, List<FileUploadDTO.FileUploadResponse> uploadedFiles) {
        if (CollectionUtils.isEmpty(uploadedFiles)) {
            return;
        }

        List<TopicAttachment> attachments = uploadedFiles.stream()
                                                         .map(file -> TopicAttachment.builder()
                                                                                     .topic(topic)
                                                                                     .fileUrl(file.getFileUrl())
                                                                                     .fileName(file.getOriginalFilename())
                                                                                     .fileType(file.getContentType())
                                                                                     .fileSize(file.getFileSize())
                                                                                     .build())
                                                         .collect(Collectors.toList());

        topicAttachmentRepository.saveAll(attachments);

        log.info("Saved {} attachments for topic ID: {}", attachments.size(), topic.getId());
    }

    private void cleanupUploadedFiles(List<FileUploadDTO.FileUploadResponse> uploadedFiles) {
        if (CollectionUtils.isEmpty(uploadedFiles)) {
            return;
        }

        uploadedFiles.forEach(file -> {
            try {
                boolean deleted = fileUploadService.deleteFile(file.getFileUrl());
                if (deleted) {
                    log.info("Cleaned up file: {}", file.getFileUrl());
                } else {
                    log.warn("Failed to cleanup file: {}", file.getFileUrl());
                }
            } catch (Exception e) {
                log.error("Error cleaning up file: {}", file.getFileUrl(), e);
            }
        });
    }
}