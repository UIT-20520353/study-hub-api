package com.backend.study_hub_api.specification;

import com.backend.study_hub_api.dto.criteria.TopicFilterCriteria;
import com.backend.study_hub_api.model.Topic;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class TopicSpecification {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public Specification<Topic> build(TopicFilterCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Add Topic-specific predicates
            addTopicPredicates(predicates, criteria, root, criteriaBuilder);

            // Add common date range predicates
            addCommonDatePredicates(predicates, criteria, root, criteriaBuilder);

            // Add search predicates
            if (StringUtils.hasText(criteria.getSearchKeyword())) {
                List<Predicate> searchPredicates = buildSearchPredicates(
                        criteria.getSearchKeyword(), root, criteriaBuilder);
                if (!searchPredicates.isEmpty()) {
                    predicates.add(criteriaBuilder.or(searchPredicates.toArray(new Predicate[0])));
                }
            }

            // Add category filters (many-to-many relationship)
            addCategoryPredicates(predicates, criteria, root, query, criteriaBuilder);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void addTopicPredicates(List<Predicate> predicates, TopicFilterCriteria criteria,
                                    Root<Topic> root, CriteriaBuilder criteriaBuilder) {

        // Text filters
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

        // University filters
        if (criteria.getUniversityId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("university").get("id"), criteria.getUniversityId()));
        }

        if (StringUtils.hasText(criteria.getUniversityName())) {
            Join<Object, Object> universityJoin = root.join("university", JoinType.LEFT);
            predicates.add(likeIgnoreCase(criteriaBuilder, universityJoin.get("name"), criteria.getUniversityName()));
        }

        // Enum filters
        if (criteria.getStatuses() != null && !criteria.getStatuses().isEmpty()) {
            predicates.add(root.get("status").in(criteria.getStatuses()));
        }

        if (criteria.getVisibilities() != null && !criteria.getVisibilities().isEmpty()) {
            predicates.add(root.get("visibility").in(criteria.getVisibilities()));
        }

        // Boolean filters
        if (criteria.getIsLocked() != null) {
            predicates.add(criteriaBuilder.equal(root.get("isLocked"), criteria.getIsLocked()));
        }

        if (criteria.getHasAttachments() != null) {
            if (criteria.getHasAttachments()) {
                // Has attachments
                predicates.add(criteriaBuilder.isNotEmpty(root.get("attachments")));
            } else {
                // No attachments
                predicates.add(criteriaBuilder.isEmpty(root.get("attachments")));
            }
        }

        // Numeric range filters
        if (criteria.getMinViewCount() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("viewCount"), criteria.getMinViewCount()));
        }

        if (criteria.getMaxViewCount() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("viewCount"), criteria.getMaxViewCount()));
        }

        if (criteria.getMinCommentCount() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("commentCount"), criteria.getMinCommentCount()));
        }

        if (criteria.getMaxCommentCount() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("commentCount"), criteria.getMaxCommentCount()));
        }

        if (criteria.getMinLikeCount() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("likeCount"), criteria.getMinLikeCount()));
        }

        if (criteria.getMaxLikeCount() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("likeCount"), criteria.getMaxLikeCount()));
        }

        // Last activity date range
        if (StringUtils.hasText(criteria.getLastActivityFrom())) {
            try {
                Instant fromDate = LocalDateTime.parse(criteria.getLastActivityFrom(), DATE_FORMATTER)
                        .toInstant(ZoneOffset.UTC);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("lastActivityAt"), fromDate));
            } catch (Exception e) {
                // Log error or handle invalid date format
            }
        }

        if (StringUtils.hasText(criteria.getLastActivityTo())) {
            try {
                Instant toDate = LocalDateTime.parse(criteria.getLastActivityTo(), DATE_FORMATTER)
                        .toInstant(ZoneOffset.UTC);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("lastActivityAt"), toDate));
            } catch (Exception e) {
                // Log error or handle invalid date format
            }
        }
    }

    // Mới: Handle many-to-many category filtering
    private void addCategoryPredicates(List<Predicate> predicates, TopicFilterCriteria criteria,
                                       Root<Topic> root, jakarta.persistence.criteria.CriteriaQuery<?> query,
                                       CriteriaBuilder criteriaBuilder) {

        // Filter by category IDs (many-to-many)
        if (criteria.hasCategoryFilter()) {
            if (criteria.getRequireAllCategories() && criteria.hasMultipleCategories()) {
                // AND logic: Topic must have ALL specified categories
                for (Long categoryId : criteria.getCategoryIds()) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Topic> subRoot = subquery.from(Topic.class);
                    Join<Object, Object> categoryJoin = subRoot.join("categories");

                    subquery.select(subRoot.get("id"))
                            .where(criteriaBuilder.and(
                                    criteriaBuilder.equal(subRoot.get("id"), root.get("id")),
                                    criteriaBuilder.equal(categoryJoin.get("id"), categoryId)
                            ));

                    predicates.add(criteriaBuilder.exists(subquery));
                }
            } else {
                // OR logic: Topic must have AT LEAST ONE of specified categories
                Join<Object, Object> categoryJoin = root.join("categories", JoinType.INNER);
                predicates.add(categoryJoin.get("id").in(criteria.getCategoryIds()));
            }
        }

        // Filter by category name
        if (StringUtils.hasText(criteria.getCategoryName())) {
            Join<Object, Object> categoryJoin = root.join("categories", JoinType.LEFT);
            predicates.add(likeIgnoreCase(criteriaBuilder, categoryJoin.get("name"), criteria.getCategoryName()));
        }
    }

    private void addCommonDatePredicates(List<Predicate> predicates, TopicFilterCriteria criteria,
                                         Root<Topic> root, CriteriaBuilder criteriaBuilder) {
        // Filter by date range
        if (StringUtils.hasText(criteria.getCreatedFrom())) {
            try {
                Instant fromDate = LocalDateTime.parse(criteria.getCreatedFrom(), DATE_FORMATTER)
                        .toInstant(ZoneOffset.UTC);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), fromDate));
            } catch (Exception e) {
                // Log error or handle invalid date format
            }
        }

        if (StringUtils.hasText(criteria.getCreatedTo())) {
            try {
                Instant toDate = LocalDateTime.parse(criteria.getCreatedTo(), DATE_FORMATTER)
                        .toInstant(ZoneOffset.UTC);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), toDate));
            } catch (Exception e) {
                // Log error or handle invalid date format
            }
        }
    }

    private List<Predicate> buildSearchPredicates(String keyword, Root<Topic> root,
                                                  CriteriaBuilder criteriaBuilder) {
        List<Predicate> searchPredicates = new ArrayList<>();

        // Search in title
        searchPredicates.add(likeIgnoreCase(criteriaBuilder, root.get("title"), keyword));

        // Search in content
        searchPredicates.add(likeIgnoreCase(criteriaBuilder, root.get("content"), keyword));

        // Search in author name
        Join<Object, Object> authorJoin = root.join("author", JoinType.LEFT);
        searchPredicates.add(likeIgnoreCase(criteriaBuilder, authorJoin.get("fullName"), keyword));

        // Search in category names (many-to-many)
        Join<Object, Object> categoryJoin = root.join("categories", JoinType.LEFT);
        searchPredicates.add(likeIgnoreCase(criteriaBuilder, categoryJoin.get("name"), keyword));

        return searchPredicates;
    }

    /**
     * Helper method for case-insensitive partial match
     */
    private Predicate likeIgnoreCase(CriteriaBuilder criteriaBuilder,
                                     jakarta.persistence.criteria.Expression<String> expression,
                                     String value) {
        return criteriaBuilder.like(
                criteriaBuilder.lower(expression),
                "%" + value.toLowerCase() + "%"
        );
    }
}