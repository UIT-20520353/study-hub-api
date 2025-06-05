package com.backend.study_hub_api.specification;

import com.backend.study_hub_api.dto.criteria.TopicFilterCriteria;
import com.backend.study_hub_api.model.Topic;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Component
public class TopicSpecification extends BaseSpecificationBuilder<Topic, TopicFilterCriteria> {

    @Override
    protected void addSpecificPredicates(List<Predicate> predicates, TopicFilterCriteria criteria,
                                         jakarta.persistence.criteria.Root<Topic> root,
                                         jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder) {

        // Content filters
        if (StringUtils.hasText(criteria.getTitle())) {
            predicates.add(likeIgnoreCase(criteriaBuilder, root.get("title"), criteria.getTitle()));
        }

        if (StringUtils.hasText(criteria.getContent())) {
            predicates.add(likeIgnoreCase(criteriaBuilder, root.get("content"), criteria.getContent()));
        }

        // Author filters
        if (criteria.getAuthorId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("author").get("id"), criteria.getAuthorId()));
        }

        if (StringUtils.hasText(criteria.getAuthorName())) {
            Join<Object, Object> authorJoin = root.join("author", JoinType.LEFT);
            predicates.add(likeIgnoreCase(criteriaBuilder, authorJoin.get("fullName"), criteria.getAuthorName()));
        }

        // Category filters
        if (!CollectionUtils.isEmpty(criteria.getCategoryIds())) {
            Join<Object, Object> categoryJoin = root.join("categories", JoinType.LEFT);
            predicates.add(categoryJoin.get("id").in(criteria.getCategoryIds()));
        }

        if (StringUtils.hasText(criteria.getCategoryName())) {
            Join<Object, Object> categoryJoin = root.join("categories", JoinType.LEFT);
            predicates.add(likeIgnoreCase(criteriaBuilder, categoryJoin.get("name"), criteria.getCategoryName()));
        }

        // University filters
        if (criteria.getUniversityId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("university").get("id"), criteria.getUniversityId()));
        }

        if (StringUtils.hasText(criteria.getUniversityName())) {
            Join<Object, Object> universityJoin = root.join("university", JoinType.LEFT);
            predicates.add(likeIgnoreCase(criteriaBuilder, universityJoin.get("name"), criteria.getUniversityName()));
        }

        // Status filters
        if (!CollectionUtils.isEmpty(criteria.getStatuses())) {
            predicates.add(root.get("status").in(criteria.getStatuses()));
        }

        if (!CollectionUtils.isEmpty(criteria.getVisibilities())) {
            predicates.add(root.get("visibility").in(criteria.getVisibilities()));
        }

        if (criteria.getIsLocked() != null) {
            predicates.add(criteriaBuilder.equal(root.get("isLocked"), criteria.getIsLocked()));
        }

        // Attachment filters
        if (criteria.getHasAttachments() != null) {
            Join<Object, Object> attachmentJoin = root.join("attachments", JoinType.LEFT);
            if (criteria.getHasAttachments()) {
                predicates.add(criteriaBuilder.isNotNull(attachmentJoin.get("id")));
            } else {
                predicates.add(criteriaBuilder.isNull(attachmentJoin.get("id")));
            }
        }

        // Engagement filters
        addRangeFilter(predicates, criteriaBuilder, root, "viewCount",
                       criteria.getMinViewCount(), criteria.getMaxViewCount());
        addRangeFilter(predicates, criteriaBuilder, root, "commentCount",
                       criteria.getMinCommentCount(), criteria.getMaxCommentCount());
        addRangeFilter(predicates, criteriaBuilder, root, "likeCount",
                       criteria.getMinLikeCount(), criteria.getMaxLikeCount());
        addRangeFilter(predicates, criteriaBuilder, root, "dislikeCount",
                       criteria.getMinDislikeCount(), criteria.getMaxDislikeCount());

        // Last activity date filters
        if (criteria.getLastActivityFrom() != null) {
            try {
                Instant fromDate = LocalDateTime.parse(criteria.getLastActivityFrom(), DATE_FORMATTER)
                                                .toInstant(ZoneOffset.UTC);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("lastActivityAt"), fromDate));
            } catch (Exception e) {
                // Log error or handle invalid date format
            }
        }

        if (criteria.getLastActivityTo() != null) {
            try {
                Instant toDate = LocalDateTime.parse(criteria.getLastActivityTo(), DATE_FORMATTER)
                                              .toInstant(ZoneOffset.UTC);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("lastActivityAt"), toDate));
            } catch (Exception e) {
                // Log error or handle invalid date format
            }
        }
    }

    @Override
    protected List<Predicate> buildSearchPredicates(String keyword,
                                                    jakarta.persistence.criteria.Root<Topic> root,
                                                    jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder) {
        List<Predicate> searchPredicates = new ArrayList<>();

        // Search in title
        searchPredicates.add(likeIgnoreCase(criteriaBuilder, root.get("title"), keyword));

        // Search in content
        searchPredicates.add(likeIgnoreCase(criteriaBuilder, root.get("content"), keyword));

        // Search in author name
        Join<Object, Object> authorJoin = root.join("author", JoinType.LEFT);
        searchPredicates.add(likeIgnoreCase(criteriaBuilder, authorJoin.get("fullName"), keyword));

        // Search in category name
        Join<Object, Object> categoryJoin = root.join("categories", JoinType.LEFT);
        searchPredicates.add(likeIgnoreCase(criteriaBuilder, categoryJoin.get("name"), keyword));

        return searchPredicates;
    }

    /**
     * Helper method to add range filters (min/max)
     */
    private void addRangeFilter(List<Predicate> predicates,
                                jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
                                jakarta.persistence.criteria.Root<Topic> root,
                                String fieldName,
                                Integer minValue,
                                Integer maxValue) {
        if (minValue != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), minValue));
        }
        if (maxValue != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), maxValue));
        }
    }
}