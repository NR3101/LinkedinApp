package com.nr3101.postsservice.service.impl;

import com.nr3101.postsservice.auth.AuthContextHolder;
import com.nr3101.postsservice.client.ConnectionsServiceClient;
import com.nr3101.postsservice.client.UploaderServiceClient;
import com.nr3101.postsservice.dto.request.PostCreateDto;
import com.nr3101.postsservice.dto.response.PersonDto;
import com.nr3101.postsservice.dto.response.PostDto;
import com.nr3101.postsservice.entity.Post;
import com.nr3101.postsservice.event.PostCreatedEvent;
import com.nr3101.postsservice.exception.ResourceNotFoundException;
import com.nr3101.postsservice.repository.PostRepository;
import com.nr3101.postsservice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ConnectionsServiceClient connectionsServiceClient;
    private final UploaderServiceClient uploaderServiceClient;
    private final KafkaTemplate<Long, PostCreatedEvent> postCreatedKafkaTemplate;

    @Override
    public PostDto createPost(PostCreateDto postCreateDto, MultipartFile file) {
        Long userId = AuthContextHolder.getCurrentUserId();
        log.info("Creating post with content: {} for user ID: {}", postCreateDto.getContent(), userId);

        String fileUrl = null;
        // Handle file upload if a file is provided
        if (file != null && !file.isEmpty()) {
            fileUrl = uploaderServiceClient.uploadFile(file);
        }

        Post post = modelMapper.map(postCreateDto, Post.class);
        post.setUserId(userId);
        post.setImageUrl(fileUrl);
        Post savedPost = postRepository.save(post);

        // Publish post created event to the connections of the user via Kafka
        List<PersonDto> connections = connectionsServiceClient.getFirstDegreeConnections(userId);
        connections.forEach(connection -> {
            PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
                    .postId(savedPost.getId())
                    .authorId(userId)
                    .connectionId(connection.getUserId())
                    .content(savedPost.getContent())
                    .build();

            postCreatedKafkaTemplate.send("post_created_topic", postCreatedEvent);
            log.info("Published PostCreatedEvent for post ID: {} to connection ID: {}", savedPost.getId(), connection.getUserId());
        });


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
