package com.backend.study_hub_api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderNotificationDTO {
    private String type;
    private String title;
    private String content;

    private Long orderId;
    private String orderCode;

    private String buyerName;
    private String buyerAvatar;
}