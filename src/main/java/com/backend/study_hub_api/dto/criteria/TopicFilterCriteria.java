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
    private Long categoryId;
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

    private String lastActivityFrom;
    private String lastActivityTo;

    @Override
    public String getSortBy() {
        String sortBy = super.getSortBy();
        return sortBy != null ? sortBy : "lastActivityAt";
    }

    @Override
    public String getSortDirection() {
        String sortDirection = super.getSortDirection();
        return sortDirection != null ? sortDirection : "DESC";
    }
}