package com.nr3101.userservice.controller;

import com.nr3101.userservice.dto.request.LoginRequestDto;
import com.nr3101.userservice.dto.request.SignupRequestDto;
import com.nr3101.userservice.dto.response.UserDto;
import com.nr3101.userservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final AuthService authService;

    @PostMapping("/signup")
    private ResponseEntity<UserDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        log.info("Received signup request for email: {}", signupRequestDto.getEmail());
        UserDto userDto = authService.signup(signupRequestDto);
        log.info("User created with ID: {}", userDto.getId());
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    private ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        log.info("Received login request for email: {}", loginRequestDto.getEmail());
        String token = authService.login(loginRequestDto);
        log.info("Login successful for email: {}", loginRequestDto.getEmail());
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
