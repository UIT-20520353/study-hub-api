package com.backend.study_hub_api.service;

import com.backend.study_hub_api.dto.criteria.BaseFilterCriteria;
import com.backend.study_hub_api.dto.common.PaginationDTO;
import com.backend.study_hub_api.helper.util.PageableBuilder;
import com.backend.study_hub_api.helper.util.PaginationUtils;
import com.backend.study_hub_api.specification.BaseSpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.function.Function;

public abstract class BaseFilterService<T, ID, C extends BaseFilterCriteria, DTO> {

    /**
     * Get paginated results with filter
     */
    public PaginationDTO<DTO> getWithFilter(C criteria) {
        // Build pageable from criteria
        Pageable pageable = PageableBuilder.build(criteria);

        // Build specification
        Specification<T> specification = getSpecificationBuilder().build(criteria);

        // Query data
        Page<T> page = getRepository().findAll(specification, pageable);

        // Convert to DTO
        Page<DTO> dtoPage = page.map(getEntityToDtoMapper());

        // Return pagination response
        return PaginationUtils.createPaginationResponse(dtoPage);
    }

    /**
     * Get all results with filter (no pagination)
     */
    public List<DTO> getAllWithFilter(C criteria) {
        // Build specification
        Specification<T> specification = getSpecificationBuilder().build(criteria);

        // Query data
        List<T> entities = getRepository().findAll(specification);

        // Convert to DTO
        return entities.stream()
                       .map(getEntityToDtoMapper())
                       .toList();
    }

    /**
     * Get repository
     */
    protected abstract JpaSpecificationExecutor<T> getRepository();

    /**
     * Get specification builder
     */
    protected abstract BaseSpecificationBuilder<T, C> getSpecificationBuilder();

    /**
     * Get entity to DTO mapper function
     */
    protected abstract Function<T, DTO> getEntityToDtoMapper();
}