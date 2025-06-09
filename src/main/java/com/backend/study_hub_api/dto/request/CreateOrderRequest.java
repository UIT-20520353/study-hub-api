package com.backend.study_hub_api.dto.request;

import com.backend.study_hub_api.helper.enumeration.DeliveryMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

import static com.backend.study_hub_api.helper.constant.Message.*;

@Data
public class CreateOrderRequest {
    @NotEmpty(message = ORDER_ITEMS_EMPTY_ERROR)
    private List<OrderItemRequest> orderItems;

    @NotNull(message = ORDER_DELIVERY_METHOD_REQUIRED_ERROR)
    private DeliveryMethod deliveryMethod;

    @NotBlank(message = ORDER_DELIVERY_ADDRESS_REQUIRED_ERROR)
    private String deliveryAddress;

    @NotBlank(message = ORDER_DELIVERY_PHONE_REQUIRED_ERROR)
    private String deliveryPhone;

    private String deliveryNotes;
} 