package com.nr3101.postsservice.controller;

import com.nr3101.postsservice.dto.request.PostCreateDto;
import com.nr3101.postsservice.dto.response.PostDto;
import com.nr3101.postsservice.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody PostCreateDto postCreateDto
    ) {
        log.info("Received request to create post: {}", postCreateDto);
        // TODO: Replace hardcoded user ID with actual authenticated user ID
        PostDto createdPost = postService.createPost(postCreateDto, 1L);
        log.info("Post created successfully: {}", createdPost);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId) {
        log.info("Received request to get post with ID: {}", postId);
        PostDto postDto = postService.getPostById(postId);
        log.info("Post retrieved successfully: {}", postDto);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/user/{userId}/allPosts")
    public ResponseEntity<Page<PostDto>> getAllPostsOfUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Received request to get posts for user ID: {}", userId);
        Page<PostDto> posts = postService.getAllPostsOfUser(userId, page, size);
        log.info("Posts retrieved successfully for user ID {}: {}", userId, posts.getContent());
        return ResponseEntity.ok(posts);
    }
}
