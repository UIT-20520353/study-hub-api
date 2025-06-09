package com.backend.study_hub_api.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import static com.backend.study_hub_api.helper.constant.Message.ORDER_CANCELLATION_REASON_MAX_LENGTH_ERROR;
import static com.backend.study_hub_api.helper.constant.Message.ORDER_CANCELLATION_REASON_REQUIRED_ERROR;

@Data
public class CancelOrderRequest {

    @NotBlank(message = ORDER_CANCELLATION_REASON_REQUIRED_ERROR)
    @Max(value = 255, message = ORDER_CANCELLATION_REASON_MAX_LENGTH_ERROR)
    private String reason;
}