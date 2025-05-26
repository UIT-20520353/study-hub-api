package com.backend.study_hub_api.helper.enumeration;

import lombok.Getter;

@Getter
public enum TopicVisibility {

    PUBLIC("PUBLIC"),
    UNIVERSITY_ONLY("UNIVERSITY_ONLY");

    private final String value;

    TopicVisibility(String value) {
        this.value = value;
    }

}
