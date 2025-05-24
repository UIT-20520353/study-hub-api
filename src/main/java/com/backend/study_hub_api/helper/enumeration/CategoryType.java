package com.backend.study_hub_api.helper.enumeration;

import lombok.Getter;

@Getter
public enum CategoryType {

    TOPIC("TOPIC"),
    PRODUCT("PRODUCT");

    private final String value;

    CategoryType(String value) {
        this.value = value;
    }

}
