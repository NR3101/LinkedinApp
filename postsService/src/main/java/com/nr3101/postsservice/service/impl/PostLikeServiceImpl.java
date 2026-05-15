package com.nr3101.postsservice.service.impl;

import com.nr3101.postsservice.entity.PostLike;
import com.nr3101.postsservice.exception.ConflictException;
import com.nr3101.postsservice.exception.ResourceNotFoundException;
import com.nr3101.postsservice.repository.PostLikeRepository;
import com.nr3101.postsservice.repository.PostRepository;
import com.nr3101.postsservice.service.PostLikeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public void likePost(Long postId) {
        Long userId = 1L; // TODO: Get the actual user ID from the security context or session
        log.info("User {} is liking post {}", userId, postId);

        postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if (alreadyLiked) {
            log.warn("User {} has already liked post {}", userId, postId);
            throw new ConflictException("User has already liked this post");
        }

        PostLike postLike = new PostLike();
        postLike.setUserId(userId);
        postLike.setPostId(postId);
        postLikeRepository.save(postLike);
        log.info("User {} liked post {}", userId, postId);
    }

    @Override
    @Transactional
    public void unlikePost(Long postId) {
        Long userId = 1L; // TODO: Get the actual user ID from the security context or session
        log.info("User {} is unliking post {}", userId, postId);

        PostLike postLike = postLikeRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new ResourceNotFoundException("Like not found for user " + userId + " and post " + postId));

        postLikeRepository.delete(postLike);
        log.info("User {} unliked post {}", userId, postId);
    }
}
