package com.backend.study_hub_api.helper.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryMethod {
    SHIPPER("SHIPPER"),
    HAND_DELIVERY("HAND_DELIVERY"),
    BOTH("BOTH");

    private final String description;
}