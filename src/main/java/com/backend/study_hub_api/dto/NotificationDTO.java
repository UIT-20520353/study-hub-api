package com.backend.study_hub_api.dto;

import com.backend.study_hub_api.helper.enumeration.NotificationType;
import lombok.Builder;
import lombok.Data;
import java.time.Instant;

@Data
@Builder
public class NotificationDTO {
    private Long id;
    private NotificationType type;
    private String title;
    private String content;
    private boolean isRead;
    private Instant createdAt;

    private TopicDTO.TopicResponse topic;
    private ProductDTO.ProductResponse product;
    private OrderDTO order;

    private UserDTO sender;
}