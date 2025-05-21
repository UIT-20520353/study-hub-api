package com.backend.study_hub_api.helper.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductCondition {
    NEW("NEW"),
    LIKE_NEW("LIKE_NEW"),
    GOOD("GOOD"),
    FAIR("FAIR"),
    POOR("POOR");

    private final String description;
}
