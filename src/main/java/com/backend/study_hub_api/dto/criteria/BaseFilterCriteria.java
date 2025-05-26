package com.backend.study_hub_api.dto.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseFilterCriteria {

    // Common filter fields
    private String searchKeyword; // For general text search
    private Boolean isActive;

    // Pagination fields
    private Integer page = 0;
    private Integer size = 10;

    // Sorting fields
    private String sortBy = "id";
    private String sortDirection = "ASC";

    // Date range filters (commonly used)
    private String createdFrom;
    private String createdTo;

    // Method to validate sort direction
    public String getSortDirection() {
        if (("ASC".equalsIgnoreCase(sortDirection) || "DESC".equalsIgnoreCase(sortDirection))) {
            return sortDirection.toUpperCase();
        }
        return "ASC";
    }
}
