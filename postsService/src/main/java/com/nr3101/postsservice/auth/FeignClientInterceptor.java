package com.nr3101.postsservice.auth;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * FeignClientInterceptor is a Feign RequestInterceptor that adds the current user's ID to the headers of outgoing Feign requests.
 * It retrieves the user ID from the AuthContextHolder and adds it as a header named "X-User-Id".
 * This allows downstream services to identify the user making the request and perform any necessary authorization or personalization.
 */

@Component
@Slf4j
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        Long userId = AuthContextHolder.getCurrentUserId();
        if (userId != null) {
            log.info("Adding user ID {} to Feign request headers", userId);
            template.header("X-User-Id", String.valueOf(userId));
        } else {
            log.warn("No user ID found in AuthContextHolder, not adding header to Feign request");
        }
    }
}
