package com.nr3101.postsservice.service;

import com.nr3101.postsservice.dto.request.PostCreateDto;
import com.nr3101.postsservice.dto.response.PostDto;
import org.springframework.data.domain.Page;

public interface PostService {
    PostDto createPost(PostCreateDto postCreateDto, Long userId);

    PostDto getPostById(Long postId);

    Page<PostDto> getAllPostsOfUser(Long userId, int page, int size);
}
