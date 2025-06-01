package com.backend.study_hub_api.specification;

import com.backend.study_hub_api.dto.criteria.UniversityFilterCriteria;
import com.backend.study_hub_api.model.University;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UniversitySpecification extends BaseSpecificationBuilder<University, UniversityFilterCriteria> {

    @Override
    protected void addSpecificPredicates(List<Predicate> predicates, UniversityFilterCriteria criteria,
                                         Root<University> root, CriteriaBuilder criteriaBuilder) {

        if (criteria.getName() != null && !criteria.getName().trim().isEmpty()) {
            predicates.add(likeIgnoreCase(criteriaBuilder, root.get("name"), criteria.getName()));
        }

        if (criteria.getCity() != null && !criteria.getCity().trim().isEmpty()) {
            predicates.add(likeIgnoreCase(criteriaBuilder, root.get("city"), criteria.getCity()));
        }

        if (criteria.getEmailDomain() != null && !criteria.getEmailDomain().trim().isEmpty()) {
            predicates.add(likeIgnoreCase(criteriaBuilder, root.get("emailDomain"), criteria.getEmailDomain()));
        }

        if (criteria.getStatuses() != null && !criteria.getStatuses().isEmpty()) {
            predicates.add(root.get("status").in(criteria.getStatuses()));
        }
    }

    @Override
    protected List<Predicate> buildSearchPredicates(String keyword, Root<University> root,
                                                    CriteriaBuilder criteriaBuilder) {
        List<Predicate> searchPredicates = new ArrayList<>();

        searchPredicates.add(likeIgnoreCase(criteriaBuilder, root.get("name"), keyword));
        searchPredicates.add(likeIgnoreCase(criteriaBuilder, root.get("shortName"), keyword));
        searchPredicates.add(likeIgnoreCase(criteriaBuilder, root.get("city"), keyword));
        searchPredicates.add(likeIgnoreCase(criteriaBuilder, root.get("emailDomain"), keyword));

        return searchPredicates;
    }
}