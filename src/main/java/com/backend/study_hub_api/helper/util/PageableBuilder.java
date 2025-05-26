package com.backend.study_hub_api.helper.util;

import com.backend.study_hub_api.dto.criteria.BaseFilterCriteria;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PageableBuilder {

    // Map để validate các field có thể sort
    private static final Map<Class<?>, List<String>> SORTABLE_FIELDS = Map.of(
            // Có thể config các field cho phép sort cho từng entity
    );

    /**
     * Build Pageable from BaseFilterCriteria
     */
    public static Pageable build(BaseFilterCriteria criteria) {
        return build(criteria.getPage(),
                     criteria.getSize(),
                     criteria.getSortBy(),
                     criteria.getSortDirection());
    }

    /**
     * Build Pageable with custom parameters
     */
    public static Pageable build(Integer page, Integer size, String sortBy, String sortDirection) {
        // Validate and set defaults
        int pageNumber = (page != null && page >= 0) ? page : 0;
        int pageSize = (size != null && size > 0) ? Math.min(size, 100) : 10; // Max 100 items per page

        // Build sort
        Sort sort = buildSort(sortBy, sortDirection);

        return PageRequest.of(pageNumber, pageSize, sort);
    }

    /**
     * Build Sort object
     */
    private static Sort buildSort(String sortBy, String sortDirection) {
        if (sortBy == null || sortBy.trim().isEmpty()) {
            return Sort.unsorted();
        }

        Sort.Direction direction = "DESC".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        // Support multiple sort fields separated by comma
        if (sortBy.contains(",")) {
            List<Sort.Order> orders = new ArrayList<>();
            String[] fields = sortBy.split(",");

            for (String field : fields) {
                String trimmedField = field.trim();
                if (!trimmedField.isEmpty()) {
                    orders.add(new Sort.Order(direction, trimmedField));
                }
            }

            return Sort.by(orders);
        }

        return Sort.by(direction, sortBy);
    }

    /**
     * Validate if field is sortable for given entity
     */
    public static boolean isSortableField(Class<?> entityClass, String field) {
        List<String> sortableFields = SORTABLE_FIELDS.get(entityClass);
        return sortableFields == null || sortableFields.contains(field);
    }
}