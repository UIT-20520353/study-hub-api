package com.backend.study_hub_api.helper.enumeration;

import lombok.Getter;

@Getter
public enum NotificationType {
    TOPIC_LIKED("TOPIC_LIKED"),
    TOPIC_FOLLOWED("TOPIC_FOLLOWED"),
    TOPIC_COMMENTED("TOPIC_COMMENTED"),
    COMMENT_LIKED("COMMENT_LIKED"),
    PRODUCT_ORDERED("PRODUCT_ORDERED"),
    PRODUCT_COMMENTED("PRODUCT_COMMENTED");

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }
}
