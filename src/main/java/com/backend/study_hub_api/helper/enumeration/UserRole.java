package com.backend.study_hub_api.helper.enumeration;

import lombok.Getter;

@Getter
public enum UserRole {

    SYSTEM_ADMIN("SYSTEM_ADMIN"),
    USER("USER");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

}
