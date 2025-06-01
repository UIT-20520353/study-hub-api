package com.backend.study_hub_api.helper.enumeration;

import lombok.Getter;

@Getter
public enum UniversityStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    DELETED("DELETED");

    private final String value;

    UniversityStatus(String value) {
        this.value = value;
    }
}
