package com.backend.study_hub_api.specification;

import com.backend.study_hub_api.dto.criteria.ProductFilterCriteria;
import com.backend.study_hub_api.model.Product;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductSpecification extends BaseSpecificationBuilder<Product, ProductFilterCriteria> {

    @Override
    protected void addSpecificPredicates(List<Predicate> predicates, ProductFilterCriteria criteria,
                                         jakarta.persistence.criteria.Root<Product> root,
                                         jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder) {

        if (StringUtils.hasText(criteria.getTitle())) {
            predicates.add(likeIgnoreCase(criteriaBuilder, root.get("title"), criteria.getTitle()));
        }

        if (StringUtils.hasText(criteria.getDescription())) {
            predicates.add(likeIgnoreCase(criteriaBuilder, root.get("description"), criteria.getDescription()));
        }

        if (StringUtils.hasText(criteria.getAddress())) {
            predicates.add(likeIgnoreCase(criteriaBuilder, root.get("address"), criteria.getAddress()));
        }

        if (criteria.getSellerId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("seller").get("id"), criteria.getSellerId()));
        }

        if (StringUtils.hasText(criteria.getSellerName())) {
            Join<Object, Object> sellerJoin = root.join("seller", JoinType.LEFT);
            predicates.add(likeIgnoreCase(criteriaBuilder, sellerJoin.get("fullName"), criteria.getSellerName()));
        }

        if (!CollectionUtils.isEmpty(criteria.getCategoryIds())) {
            predicates.add(root.get("category").get("id").in(criteria.getCategoryIds()));
        }

        if (StringUtils.hasText(criteria.getCategoryName())) {
            Join<Object, Object> categoryJoin = root.join("category", JoinType.LEFT);
            predicates.add(likeIgnoreCase(criteriaBuilder, categoryJoin.get("name"), criteria.getCategoryName()));
        }

        if (criteria.getUniversityId() != null) {
            Join<Object, Object> sellerJoin = root.join("seller", JoinType.LEFT);
            predicates.add(criteriaBuilder.equal(sellerJoin.get("university").get("id"), criteria.getUniversityId()));
        }

        if (StringUtils.hasText(criteria.getUniversityName())) {
            Join<Object, Object> sellerJoin = root.join("seller", JoinType.LEFT);
            Join<Object, Object> universityJoin = sellerJoin.join("university", JoinType.LEFT);
            predicates.add(likeIgnoreCase(criteriaBuilder, universityJoin.get("name"), criteria.getUniversityName()));
        }

        if (!CollectionUtils.isEmpty(criteria.getStatuses())) {
            predicates.add(root.get("status").in(criteria.getStatuses()));
        }

        if (!CollectionUtils.isEmpty(criteria.getConditions())) {
            predicates.add(root.get("condition").in(criteria.getConditions()));
        }

        if (!CollectionUtils.isEmpty(criteria.getDeliveryMethods())) {
            predicates.add(root.get("deliveryMethod").in(criteria.getDeliveryMethods()));
        }

        addRangeFilter(predicates, criteriaBuilder, root, "price",
                       criteria.getMinPrice(), criteria.getMaxPrice());

        addRangeFilter(predicates, criteriaBuilder, root, "viewCount",
                       criteria.getMinViewCount(), criteria.getMaxViewCount());
    }

    @Override
    protected List<Predicate> buildSearchPredicates(String keyword,
                                                    jakarta.persistence.criteria.Root<Product> root,
                                                    jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder) {
        List<Predicate> searchPredicates = new ArrayList<>();

        searchPredicates.add(likeIgnoreCase(criteriaBuilder, root.get("title"), keyword));

        searchPredicates.add(likeIgnoreCase(criteriaBuilder, root.get("description"), keyword));

        Join<Object, Object> sellerJoin = root.join("seller", JoinType.LEFT);
        searchPredicates.add(likeIgnoreCase(criteriaBuilder, sellerJoin.get("fullName"), keyword));

        Join<Object, Object> categoryJoin = root.join("category", JoinType.LEFT);
        searchPredicates.add(likeIgnoreCase(criteriaBuilder, categoryJoin.get("name"), keyword));

        searchPredicates.add(likeIgnoreCase(criteriaBuilder, root.get("address"), keyword));

        return searchPredicates;
    }

    private void addRangeFilter(List<Predicate> predicates,
                                jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
                                jakarta.persistence.criteria.Root<Product> root,
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