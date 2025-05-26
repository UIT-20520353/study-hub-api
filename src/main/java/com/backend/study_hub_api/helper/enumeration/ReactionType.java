package com.backend.study_hub_api.helper.enumeration;

import lombok.Getter;

@Getter
public enum ReactionType {

    LIKE("LIKE"),
    DISLIKE("DISLIKE");

    private final String value;

    ReactionType(String value) {
        this.value = value;
    }

}
