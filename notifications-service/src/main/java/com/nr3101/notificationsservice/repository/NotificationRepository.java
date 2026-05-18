package com.nr3101.notificationsservice.repository;

import com.nr3101.notificationsservice.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}