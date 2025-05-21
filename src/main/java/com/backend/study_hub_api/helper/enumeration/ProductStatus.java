package com.backend.study_hub_api.helper.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
    AVAILABLE("AVAILABLE"),
    PENDING("PENDING"),
    SOLD("SOLD"),
    INACTIVE("INACTIVE");

    private final String description;
}
