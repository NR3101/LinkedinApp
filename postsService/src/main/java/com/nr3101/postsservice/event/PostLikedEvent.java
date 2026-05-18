package com.nr3101.postsservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostLikedEvent {

    private Long postId; // ID of the post that was liked
    private Long authorId; // ID of the author of the post (the user who created the post)
    private Long likedByUserId; // ID of the user who liked the post(current user)
}
