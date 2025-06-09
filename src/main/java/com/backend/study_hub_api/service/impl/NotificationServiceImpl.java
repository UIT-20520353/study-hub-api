package com.backend.study_hub_api.service.impl;

import com.backend.study_hub_api.dto.NotificationDTO;
import com.backend.study_hub_api.mapper.OrderMapper;
import com.backend.study_hub_api.model.Notification;
import com.backend.study_hub_api.model.User;
import com.backend.study_hub_api.repository.NotificationRepository;
import com.backend.study_hub_api.service.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    SimpMessagingTemplate messagingTemplate;
    UserService userService;
    NotificationRepository notificationRepository;
    TopicService topicService;
    ProductService productService;
    OrderMapper orderMapper;

    @Override
    public void sendNotificationToUser(Long userId, Object notification) {

    }

    @Override
    public void sendNotificationToAll(Object notification) {
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }

    @Override
    public void sendNotificationToUniversity(Integer universityId, Object notification) {
        messagingTemplate.convertAndSend(
                "/topic/university/" + universityId + "/notifications",
                notification
        );
    }

    @Override
    public List<NotificationDTO> getUserNotifications() {
        User currentUser = userService.getCurrentUser();
        return notificationRepository.findByRecipientOrderByCreatedAtDesc(currentUser)
                                     .stream()
                                     .map(this::convertToDTO)
                                     .toList();
    }

    private NotificationDTO convertToDTO(Notification notification) {
        return NotificationDTO.builder()
                              .id(notification.getId())
                              .type(notification.getType())
                              .title(notification.getTitle())
                              .content(notification.getContent())
                              .isRead(notification.getIsRead())
                              .createdAt(notification.getCreatedAt())
                              .sender(userService.mapToDTO(notification.getSender()))
                              .topic(notification.getTopic() != null ? topicService.mapToDTO(notification.getTopic()) : null)
                              .product(notification.getProduct() != null ? productService.mapToProductResponse(notification.getProduct()) : null)
                              .order(notification.getOrder() != null ? orderMapper.toDTO(notification.getOrder()) : null)
                              .build();
    }

}
