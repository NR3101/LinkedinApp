package com.nr3101.notificationsservice.consumer;

import com.nr3101.connectionsservice.event.ConnectionRequestAcceptedEvent;
import com.nr3101.connectionsservice.event.ConnectionRequestSentEvent;
import com.nr3101.notificationsservice.entity.Notification;
import com.nr3101.notificationsservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "connection_request_sent_topic")
    public void handleConnectionRequestSentEvent(ConnectionRequestSentEvent event) {
        log.info("Received ConnectionRequestSentEvent: {}", event);

        // Create a notification based on the event data and save it to the database
        String message = String.format("You have received a connection request from user with ID: %d", event.getSenderId());
        Notification notification = Notification.builder()
                .message(message)
                .userId(event.getReceiverId()) // The receiver of the connection request will receive the notification
                .build();
        notificationService.addNotification(notification);
    }

    @KafkaListener(topics = "connection_request_accepted_topic")
    public void handleConnectionRequestAcceptedEvent(ConnectionRequestAcceptedEvent event) {
        log.info("Received ConnectionRequestAcceptedEvent: {}", event);

        // Create a notification based on the event data and save it to the database
        String message = String.format("Your connection request to user with ID: %d has been accepted", event.getReceiverId());
        Notification notification = Notification.builder()
                .message(message)
                .userId(event.getSenderId()) // The sender of the connection request will receive the notification
                .build();
        notificationService.addNotification(notification);
    }
}
