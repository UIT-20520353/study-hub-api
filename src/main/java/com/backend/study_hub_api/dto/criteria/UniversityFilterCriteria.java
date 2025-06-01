package com.backend.study_hub_api.dto.criteria;

import com.backend.study_hub_api.helper.enumeration.UniversityStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UniversityFilterCriteria extends BaseFilterCriteria {

    private String name;
    private String city;
    private String emailDomain;
    private List<UniversityStatus> statuses;

    @Override
    public String getSortBy() {
        String sortBy = super.getSortBy();
        return sortBy != null ? sortBy : "createdAt";
    }

    @Override
    public String getSortDirection() {
        String sortDirection = super.getSortDirection();
        return sortDirection != null ? sortDirection : "DESC";
    }
}