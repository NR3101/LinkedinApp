package com.nr3101.userservice.service;

import com.nr3101.userservice.dto.request.LoginRequestDto;
import com.nr3101.userservice.dto.request.SignupRequestDto;
import com.nr3101.userservice.dto.response.UserDto;
import jakarta.validation.Valid;

public interface AuthService {
    
    UserDto signup(@Valid SignupRequestDto signupRequestDto);

    String login(@Valid LoginRequestDto loginRequestDto);
}
