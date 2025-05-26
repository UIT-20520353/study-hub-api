package com.backend.study_hub_api.dto.criteria;

import com.backend.study_hub_api.helper.enumeration.CategoryType;
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
public class CategoryFilterCriteria extends BaseFilterCriteria {

    private String name;
    private List<CategoryType> types;

    @Override
    public String getSortBy() {
        String sortBy = super.getSortBy();
        return sortBy != null ? sortBy : "name";
    }

}
