package com.backend.study_hub_api.helper.enumeration;

import lombok.Getter;

@Getter
public enum TopicStatus {

    ACTIVE("ACTIVE"),
    DELETED("DELETED"),
    CLOSED("CLOSED");

    private final String value;

    TopicStatus(String value) {
        this.value = value;
    }

}
