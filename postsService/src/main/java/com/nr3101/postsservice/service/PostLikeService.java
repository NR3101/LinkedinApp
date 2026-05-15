package com.nr3101.postsservice.service;

public interface PostLikeService {

    void likePost(Long postId);

    void unlikePost(Long postId);
}
