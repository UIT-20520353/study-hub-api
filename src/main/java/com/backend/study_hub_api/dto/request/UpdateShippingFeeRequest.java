package com.backend.study_hub_api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import static com.backend.study_hub_api.helper.constant.Message.ORDER_SHIPPING_FEE_MIN_ERROR;
import static com.backend.study_hub_api.helper.constant.Message.ORDER_SHIPPING_FEE_REQUIRED_ERROR;

@Data
public class UpdateShippingFeeRequest {

    @NotNull(message = ORDER_SHIPPING_FEE_REQUIRED_ERROR)
    @Min(value = 0, message = ORDER_SHIPPING_FEE_MIN_ERROR)
    private Integer shippingFee;
}