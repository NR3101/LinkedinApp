package com.nr3101.notificationsservice.consumer;

import com.nr3101.notificationsservice.entity.Notification;
import com.nr3101.notificationsservice.service.NotificationService;
import com.nr3101.postsservice.event.PostCreatedEvent;
import com.nr3101.postsservice.event.PostLikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostsConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "post_created_topic")
    public void handlePostCreatedEvent(PostCreatedEvent event) {
        log.info("Received PostCreatedEvent: {}", event);

        // Create a notification based on the event data and save it to the database
        String message = String.format("Your connection with ID: %d has created a new post: %s", event.getAuthorId(), event.getContent());
        Notification notification = Notification.builder()
                .message(message)
                .userId(event.getConnectionId()) // The connection will receive the notification
                .build();
        notificationService.addNotification(notification);
    }

    @KafkaListener(topics = "post_liked_topic")
    public void handlePostLikedEvent(PostLikedEvent event) {
        log.info("Received PostLikedEvent: {}", event);

        // Create a notification based on the event data and save it to the database
        String message = String.format("Your post with ID: %d was liked by user with ID: %d", event.getPostId(), event.getLikedByUserId());
        Notification notification = Notification.builder()
                .message(message)
                .userId(event.getAuthorId()) // The author of the post will receive the notification
                .build();
        notificationService.addNotification(notification);
    }

}
