package com.backend.study_hub_api.specification;

import com.backend.study_hub_api.dto.criteria.BaseFilterCriteria;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseSpecificationBuilder<T, C extends BaseFilterCriteria> {

    protected static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Build specification from criteria
     */
    public Specification<T> build(C criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Add common predicates
            addCommonPredicates(predicates, criteria, root, criteriaBuilder);

            // Add entity-specific predicates
            addSpecificPredicates(predicates, criteria, root, criteriaBuilder);

            // Apply sorting (no need to handle it here as Pageable will handle it)

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Add common predicates that apply to all entities
     */
    protected void addCommonPredicates(List<Predicate> predicates, C criteria,
                                       jakarta.persistence.criteria.Root<T> root,
                                       jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder) {

        // Filter by isActive
        if (criteria.getIsActive() != null) {
            predicates.add(criteriaBuilder.equal(root.get("isActive"), criteria.getIsActive()));
        }

        // Filter by date range
        if (criteria.getCreatedFrom() != null) {
            try {
                Instant fromDate = LocalDateTime.parse(criteria.getCreatedFrom(), DATE_FORMATTER)
                                                .toInstant(ZoneOffset.UTC);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), fromDate));
            } catch (Exception e) {
                // Log error or handle invalid date format
            }
        }

        if (criteria.getCreatedTo() != null) {
            try {
                Instant toDate = LocalDateTime.parse(criteria.getCreatedTo(), DATE_FORMATTER)
                                              .toInstant(ZoneOffset.UTC);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), toDate));
            } catch (Exception e) {
                // Log error or handle invalid date format
            }
        }

        // General keyword search (if entity has searchable fields)
        if (criteria.getSearchKeyword() != null && !criteria.getSearchKeyword().trim().isEmpty()) {
            List<Predicate> searchPredicates = buildSearchPredicates(
                    criteria.getSearchKeyword(),
                    root,
                    criteriaBuilder
            );
            if (!searchPredicates.isEmpty()) {
                predicates.add(criteriaBuilder.or(searchPredicates.toArray(new Predicate[0])));
            }
        }
    }

    /**
     * Override this method to add entity-specific predicates
     */
    protected abstract void addSpecificPredicates(List<Predicate> predicates, C criteria,
                                                  jakarta.persistence.criteria.Root<T> root,
                                                  jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder);

    /**
     * Override this method to define searchable fields for keyword search
     */
    protected List<Predicate> buildSearchPredicates(String keyword,
                                                    jakarta.persistence.criteria.Root<T> root,
                                                    jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder) {
        return new ArrayList<>();
    }

    /**
     * Helper method for case-insensitive partial match
     */
    protected Predicate likeIgnoreCase(jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
                                       jakarta.persistence.criteria.Expression<String> expression,
                                       String value) {
        return criteriaBuilder.like(
                criteriaBuilder.lower(expression),
                "%" + value.toLowerCase() + "%"
        );
    }
}
