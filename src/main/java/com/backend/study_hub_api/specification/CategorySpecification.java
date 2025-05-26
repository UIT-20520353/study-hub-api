package com.backend.study_hub_api.specification;

import com.backend.study_hub_api.dto.criteria.CategoryFilterCriteria;
import com.backend.study_hub_api.model.Category;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategorySpecification extends BaseSpecificationBuilder<Category, CategoryFilterCriteria> {

    @Override
    protected void addSpecificPredicates(List<Predicate> predicates, CategoryFilterCriteria criteria,
                                         Root<Category> root, CriteriaBuilder criteriaBuilder) {

        if (criteria.getName() != null && !criteria.getName().trim().isEmpty()) {
            predicates.add(likeIgnoreCase(criteriaBuilder, root.get("name"), criteria.getName()));
        }

        if (criteria.getTypes() != null && !criteria.getTypes().isEmpty()) {
            predicates.add(root.get("type").in(criteria.getTypes()));
        }
    }

    @Override
    protected List<Predicate> buildSearchPredicates(String keyword, Root<Category> root,
                                                    CriteriaBuilder criteriaBuilder) {
        List<Predicate> searchPredicates = new ArrayList<>();

        searchPredicates.add(likeIgnoreCase(criteriaBuilder, root.get("name"), keyword));

        return searchPredicates;
    }
}