package com.backend.study_hub_api.specification;

import com.backend.study_hub_api.dto.criteria.TopicFilterCriteria;
import com.backend.study_hub_api.model.Topic;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Component
public class TopicSpecification extends BaseSpecificationBuilder<Topic, TopicFilterCriteria> {

    @Override
    protected void addSpecificPredicates(List<Predicate> predicates, TopicFilterCriteria criteria,
                                         Root<Topic> root, CriteriaBuilder criteriaBuilder) {

        // Filter by title
        if (criteria.getTitle() != null && !criteria.getTitle().trim().isEmpty()) {
            predicates.add(likeIgnoreCase(criteriaBuilder, root.get("title"), criteria.getTitle()));
        }

        // Filter by author ID
        if (criteria.getAuthorId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("author").get("id"), criteria.getAuthorId()));
        }

        // Filter by author name
        if (criteria.getAuthorName() != null && !criteria.getAuthorName().trim().isEmpty()) {
            Join<Object, Object> authorJoin = root.join("author", JoinType.LEFT);
            predicates.add(likeIgnoreCase(criteriaBuilder, authorJoin.get("fullName"), criteria.getAuthorName()));
        }

        // Filter by category ID
        if (criteria.getCategoryId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("category").get("id"), criteria.getCategoryId()));
        }

        // Filter by university ID
        if (criteria.getUniversityId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("university").get("id"), criteria.getUniversityId()));
        }

        // Filter by statuses
        if (criteria.getStatuses() != null && !criteria.getStatuses().isEmpty()) {
            predicates.add(root.get("status").in(criteria.getStatuses()));
        }

        // Filter by visibilities
        if (criteria.getVisibilities() != null && !criteria.getVisibilities().isEmpty()) {
            predicates.add(root.get("visibility").in(criteria.getVisibilities()));
        }

        // Filter by locked status
        if (criteria.getIsLocked() != null) {
            predicates.add(criteriaBuilder.equal(root.get("isLocked"), criteria.getIsLocked()));
        }

        // Filter by view count range
        if (criteria.getMinViewCount() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("viewCount"), criteria.getMinViewCount()));
        }
        if (criteria.getMaxViewCount() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("viewCount"), criteria.getMaxViewCount()));
        }

        // Filter by like count range
        if (criteria.getMinLikeCount() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("likeCount"), criteria.getMinLikeCount()));
        }
        if (criteria.getMaxLikeCount() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("likeCount"), criteria.getMaxLikeCount()));
        }

        // Filter by comment count range
        if (criteria.getMinCommentCount() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("commentCount"), criteria.getMinCommentCount()));
        }
        if (criteria.getMaxCommentCount() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("commentCount"), criteria.getMaxCommentCount()));
        }

        // Filter by has comments
        if (criteria.getHasComments() != null) {
            if (criteria.getHasComments()) {
                predicates.add(criteriaBuilder.greaterThan(root.get("commentCount"), 0));
            } else {
                predicates.add(criteriaBuilder.equal(root.get("commentCount"), 0));
            }
        }

        // Filter by last activity date range
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

        // Filter by has attachments
        if (criteria.getHasAttachments() != null) {
            Subquery<Long> attachmentSubquery = criteriaBuilder.createQuery().subquery(Long.class);
            Root<Topic> attachmentRoot = attachmentSubquery.from(Topic.class);
            attachmentSubquery.select(criteriaBuilder.count(attachmentRoot.get("attachments")))
                              .where(criteriaBuilder.equal(attachmentRoot.get("id"), root.get("id")));

            if (criteria.getHasAttachments()) {
                predicates.add(criteriaBuilder.greaterThan(attachmentSubquery, 0L));
            } else {
                predicates.add(criteriaBuilder.equal(attachmentSubquery, 0L));
            }
        }

        // Filter topics followed by current user
        if (criteria.getFollowedByCurrentUser() != null && criteria.getFollowedByCurrentUser() && criteria.getCurrentUserId() != null) {
            Subquery<Long> followSubquery = criteriaBuilder.createQuery().subquery(Long.class);
            Root<Topic> followRoot = followSubquery.from(Topic.class);
            Join<Object, Object> followersJoin = followRoot.join("followers", JoinType.INNER);

            followSubquery.select(followRoot.get("id"))
                          .where(criteriaBuilder.and(
                                  criteriaBuilder.equal(followRoot.get("id"), root.get("id")),
                                  criteriaBuilder.equal(followersJoin.get("user").get("id"), criteria.getCurrentUserId())
                          ));

            predicates.add(criteriaBuilder.exists(followSubquery));
        }

        // Filter topics liked by current user
        if (criteria.getLikedByCurrentUser() != null && criteria.getLikedByCurrentUser() && criteria.getCurrentUserId() != null) {
            Subquery<Long> likeSubquery = criteriaBuilder.createQuery().subquery(Long.class);
            Root<Topic> likeRoot = likeSubquery.from(Topic.class);
            Join<Object, Object> reactionsJoin = likeRoot.join("reactions", JoinType.INNER);

            likeSubquery.select(likeRoot.get("id"))
                        .where(criteriaBuilder.and(
                                criteriaBuilder.equal(likeRoot.get("id"), root.get("id")),
                                criteriaBuilder.equal(reactionsJoin.get("user").get("id"), criteria.getCurrentUserId()),
                                criteriaBuilder.equal(reactionsJoin.get("reactionType"), "LIKE")
                        ));

            predicates.add(criteriaBuilder.exists(likeSubquery));
        }
    }

    @Override
    protected List<Predicate> buildSearchPredicates(String keyword, Root<Topic> root,
                                                    CriteriaBuilder criteriaBuilder) {
        List<Predicate> searchPredicates = new ArrayList<>();

        // Search in title
        searchPredicates.add(likeIgnoreCase(criteriaBuilder, root.get("title"), keyword));

        // Search in content
        searchPredicates.add(likeIgnoreCase(criteriaBuilder, root.get("content"), keyword));

        // Search in author name
        Join<Object, Object> authorJoin = root.join("author", JoinType.LEFT);
        searchPredicates.add(likeIgnoreCase(criteriaBuilder, authorJoin.get("fullName"), keyword));

        // Search in category name
        Join<Object, Object> categoryJoin = root.join("category", JoinType.LEFT);
        searchPredicates.add(likeIgnoreCase(criteriaBuilder, categoryJoin.get("name"), keyword));

        return searchPredicates;
    }
}