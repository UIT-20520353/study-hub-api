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

    // Basic topic filters
    private String title;

    // Related entity filters
    private Long authorId;
    private String authorName;
    private Long categoryId;
    private Long universityId;

    // Status and visibility filters
    private List<TopicStatus> statuses;
    private List<TopicVisibility> visibilities;
    private Boolean isLocked;

    // Interaction filters
    private Boolean hasComments;
    private Integer minViewCount;
    private Integer maxViewCount;
    private Integer minLikeCount;
    private Integer maxLikeCount;
    private Integer minCommentCount;
    private Integer maxCommentCount;

    // Date filters
    private String lastActivityFrom;
    private String lastActivityTo;

    // User-specific filters (for authenticated users)
    private Long currentUserId; // To filter user's own topics
    private Boolean followedByCurrentUser; // Topics followed by current user
    private Boolean likedByCurrentUser; // Topics liked by current user

    // Special filters
    private Boolean hasAttachments;
    private String tag; // If you implement tagging system later

    @Override
    public String getSortBy() {
        String sortBy = super.getSortBy();
        return sortBy != null ? sortBy : "lastActivityAt";
    }

    @Override
    public String getSortDirection() {
        String sortDirection = super.getSortDirection();
        // Default to DESC for topics to show most recent activity first
        return sortDirection != null ? sortDirection : "DESC";
    }
}