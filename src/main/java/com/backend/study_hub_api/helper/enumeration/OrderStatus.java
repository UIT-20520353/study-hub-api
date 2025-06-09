package com.backend.study_hub_api.helper.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    PENDING("PENDING"),
    CONFIRMED("CONFIRMED"),
    SHIPPING("SHIPPING"),
    SHIPPING_FEE_UPDATED("SHIPPING_FEE_UPDATED"),
    DELIVERED("DELIVERED"),
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED");

    private final String description;
}
