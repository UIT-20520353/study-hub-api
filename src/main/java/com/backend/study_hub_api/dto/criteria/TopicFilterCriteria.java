package com.backend.study_hub_api.dto.criteria;

import com.backend.study_hub_api.helper.enumeration.TopicStatus;
import com.backend.study_hub_api.helper.enumeration.TopicVisibility;
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
public class TopicFilterCriteria extends BaseFilterCriteria {

    private String title;
    private String content;

    private Long authorId;
    private String authorName;

    private List<Long> categoryIds;
    private String categoryName;

    private Long universityId;
    private String universityName;

    private List<TopicStatus> statuses;
    private List<TopicVisibility> visibilities;

    private Boolean isLocked;
    private Boolean hasAttachments;

    private Integer minViewCount;
    private Integer maxViewCount;
    private Integer minCommentCount;
    private Integer maxCommentCount;
    private Integer minLikeCount;
    private Integer maxLikeCount;
    private Integer minDislikeCount;
    private Integer maxDislikeCount;

    private String lastActivityFrom;
    private String lastActivityTo;

    // Thêm: Các filter options cho categories
    private Boolean requireAllCategories = false; // false = OR logic, true = AND logic

    @Override
    public String getSortBy() {
        if (super.getSortBy() == null || "id".equals(super.getSortBy())) {
            return "lastActivityAt";
        }
        return super.getSortBy();
    }

    // Helper methods
    public boolean hasCategoryFilter() {
        return categoryIds != null && !categoryIds.isEmpty();
    }

    public boolean hasMultipleCategories() {
        return hasCategoryFilter() && categoryIds.size() > 1;
    }
}