package com.nr3101.connectionsservice.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * RequestInterceptor is a Spring MVC interceptor that intercepts incoming HTTP requests to extract the user ID from the "X-User-Id" header and store it in the AuthContextHolder for later use in the request processing.
 * It implements the HandlerInterceptor interface, which provides methods to intercept requests before they reach the controller (preHandle) and after the request has been processed (afterCompletion).
 * In the preHandle method, it checks for the presence of the "X-User-Id" header, parses it as a Long, and sets it in the AuthContextHolder.
 * In the afterCompletion method, it clears the AuthContextHolder to ensure that user information does not leak between requests.
 */

@Component
@Slf4j
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader != null) {
            try {
                Long userId = Long.parseLong(userIdHeader);
                AuthContextHolder.setCurrentUserId(userId);
            } catch (NumberFormatException e) {
                // Invalid user ID format, ignore and do not set in context
                log.warn("Invalid user ID format in header: {}", userIdHeader);
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        AuthContextHolder.clear();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
