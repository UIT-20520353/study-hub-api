package com.backend.study_hub_api.helper.enumeration;

import lombok.Getter;

@Getter
public enum VerificationType {
    EMAIL_VERIFICATION("EMAIL_VERIFICATION"),
    PASSWORD_RESET("PASSWORD_RESET");

    private final String value;

    VerificationType(String value) {
        this.value = value;
    }
}
