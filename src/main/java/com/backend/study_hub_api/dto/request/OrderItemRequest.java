package com.backend.study_hub_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import static com.backend.study_hub_api.helper.constant.Message.*;

@Data
public class OrderItemRequest {
    @NotNull(message = ORDER_PRODUCT_ID_REQUIRED_ERROR)
    private Long productId;
} 