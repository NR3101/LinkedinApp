package com.nr3101.notificationsservice.service;

import com.nr3101.notificationsservice.entity.Notification;
import com.nr3101.notificationsservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void addNotification(Notification notification) {
        log.info("Adding notification: {}", notification);
        notificationRepository.save(notification);
        log.info("Notification saved: {}", notification);
    }
}
