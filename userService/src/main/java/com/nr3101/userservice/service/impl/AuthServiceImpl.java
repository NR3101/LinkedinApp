package com.nr3101.userservice.service.impl;

import com.nr3101.userservice.dto.request.LoginRequestDto;
import com.nr3101.userservice.dto.request.SignupRequestDto;
import com.nr3101.userservice.dto.response.UserDto;
import com.nr3101.userservice.entity.User;
import com.nr3101.userservice.event.UserCreatedEvent;
import com.nr3101.userservice.exception.ConflictException;
import com.nr3101.userservice.exception.UnauthorizedException;
import com.nr3101.userservice.repository.UserRepository;
import com.nr3101.userservice.service.AuthService;
import com.nr3101.userservice.utils.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.nr3101.userservice.utils.Bcrypt.checkPassword;
import static com.nr3101.userservice.utils.Bcrypt.hashPassword;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final KafkaTemplate<Long, UserCreatedEvent> userCreatedKafkaTemplate;

    @Override
    public UserDto signup(SignupRequestDto signupRequestDto) {
        log.info("Processing signup for email: {}", signupRequestDto.getEmail());

        if (userRepository.existsByEmail(signupRequestDto.getEmail())) {
            log.warn("Signup failed: Email {} already exists", signupRequestDto.getEmail());
            throw new ConflictException("Email already exists");
        }

        User user = modelMapper.map(signupRequestDto, User.class);
        user.setPassword(hashPassword(signupRequestDto.getPassword()));

        User savedUser = userRepository.save(user);
        log.info("User created with email: {}", savedUser.getEmail());

        // Publish user created event to Kafka
        UserCreatedEvent userCreatedEvent = UserCreatedEvent.builder()
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .build();
        userCreatedKafkaTemplate.send("user_created_topic", userCreatedEvent);
        log.info("Published UserCreatedEvent for userId: {}", savedUser.getId());

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public String login(LoginRequestDto loginRequestDto) {
        log.info("Processing login for email: {}", loginRequestDto.getEmail());

        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        boolean passwordMatches = checkPassword(loginRequestDto.getPassword(), user.getPassword());
        if (!passwordMatches) {
            log.warn("Login failed: Invalid password for email {}", loginRequestDto.getEmail());
            throw new UnauthorizedException("Invalid email or password");
        }

        String accessToken = jwtService.generateAccessToken(user);
        log.info("Login successful for email: {}", loginRequestDto.getEmail());
        return accessToken;
    }
}
