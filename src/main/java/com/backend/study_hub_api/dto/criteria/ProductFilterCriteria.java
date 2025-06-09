package com.backend.study_hub_api.dto.criteria;

import com.backend.study_hub_api.helper.enumeration.DeliveryMethod;
import com.backend.study_hub_api.helper.enumeration.ProductCondition;
import com.backend.study_hub_api.helper.enumeration.ProductStatus;
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
public class ProductFilterCriteria extends BaseFilterCriteria {

    private String title;
    private String description;

    private Long sellerId;
    private String sellerName;

    private List<Long> categoryIds;
    private String categoryName;

    private Long universityId;
    private String universityName;

    private List<ProductStatus> statuses;
    private List<ProductCondition> conditions;
    private List<DeliveryMethod> deliveryMethods;

    private Integer minPrice;
    private Integer maxPrice;

    private Integer minViewCount;
    private Integer maxViewCount;

    private String address;

    @Override
    public String getSortBy() {
        if (super.getSortBy() == null || "id".equals(super.getSortBy())) {
            return "createdAt";
        }
        return super.getSortBy();
    }

    @Override
    public String getSortDirection() {
        if (super.getSortDirection() == null || super.getSortDirection().isEmpty()) {
            return "DESC";
        }
        return super.getSortDirection();
    }

    // Helper methods
    public boolean hasCategoryFilter() {
        return categoryIds != null && !categoryIds.isEmpty();
    }

    public boolean hasPriceFilter() {
        return minPrice != null || maxPrice != null;
    }
}