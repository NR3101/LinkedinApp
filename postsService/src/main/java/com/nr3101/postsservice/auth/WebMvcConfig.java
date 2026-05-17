package com.nr3101.postsservice.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvcConfig is a Spring configuration class that implements the WebMvcConfigurer interface to register the RequestInterceptor as a Spring MVC interceptor.
 * By implementing WebMvcConfigurer, we can customize the configuration of Spring MVC, including adding interceptors that will be applied to incoming HTTP requests.
 * In the addInterceptors method, we add our custom RequestInterceptor to the InterceptorRegistry, which ensures that it will be invoked for every incoming request to extract the user ID from the headers and set it in the AuthContextHolder.
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private RequestInterceptor requestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor);
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
