package com.backend.study_hub_api.service;

import com.backend.study_hub_api.dto.NotificationDTO;

import java.util.List;

public interface NotificationService {

    void sendNotificationToUser(Long userId, Object notification);
    void sendNotificationToAll(Object notification);
    void sendNotificationToUniversity(Integer universityId, Object notification);

    List<NotificationDTO> getUserNotifications();

}
