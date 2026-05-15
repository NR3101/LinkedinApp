package com.nr3101.postsservice.service.impl;

import com.nr3101.postsservice.dto.request.PostCreateDto;
import com.nr3101.postsservice.dto.response.PostDto;
import com.nr3101.postsservice.entity.Post;
import com.nr3101.postsservice.exception.ResourceNotFoundException;
import com.nr3101.postsservice.repository.PostRepository;
import com.nr3101.postsservice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Override
    public PostDto createPost(PostCreateDto postCreateDto, Long userId) {
        log.info("Creating post with data: {}", postCreateDto);

        Post post = modelMapper.map(postCreateDto, Post.class);
        post.setUserId(userId);
        Post savedPost = postRepository.save(post);


        log.info("Post saved successfully with ID: {}", savedPost.getId());
        return modelMapper.map(savedPost, PostDto.class);
    }

    @Override
    public PostDto getPostById(Long postId) {
        log.info("Fetching post with ID: {}", postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + postId));

        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public Page<PostDto> getAllPostsOfUser(Long userId, int page, int size) {
        log.info("Fetching posts for user ID: {}, page: {}, size: {}", userId, page, size);

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest request = PageRequest.of(page, size, sort);
        Page<Post> postPage = postRepository.findByUserId(userId, request);

        log.info("Posts fetched successfully for user ID: {}, total elements: {}", userId, postPage.getTotalElements());
        return postPage.map(post -> modelMapper.map(post, PostDto.class));
    }
}
