package com.nr3101.postsservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostCreatedEvent {

    private Long postId; // ID of the newly created post
    private Long authorId; // ID of the author of the post (the user who created the post)
    private Long connectionId;// ID of the connection to whom the event is relevant (the user who will receive the notification)
    private String content; // Content of the post
}
