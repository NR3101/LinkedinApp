package com.nr3101.apigateway.filter;

import com.nr3101.apigateway.service.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * AuthenticationFilter is a custom filter for Spring Cloud Gateway that checks for the presence of a JWT token in the Authorization header of incoming requests.
 * AbstractGatewayFilterFactory is a base class provided by Spring Cloud Gateway to create custom filters. It takes a configuration class as a type parameter, which can be used to pass any necessary configuration to the filter.
 * GatewayFilter is an interface that defines the contract for filters in Spring Cloud Gateway. It has a single method, filter, which takes a ServerWebExchange and a GatewayFilterChain as parameters and returns a Mono<Void>.
 * ServerWebExchange is an interface that represents the current HTTP request and response. It provides methods to access and modify the request and response, as well as to pass data between filters.
 */

@Component
@Slf4j
// Ye auth filter class h jo hr request me JWT token check krega when request comes to API Gateway.
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    // Injecting JwtService to validate the token and extract user information.
    private final JwtService jwtService;

    public AuthenticationFilter(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    // Yha actual filter logic chlti h.
    public @NonNull GatewayFilter apply(@NonNull Config config) {
        return (exchange, chain) -> {
            log.info("Auth request received for uri: {}", exchange.getRequest().getURI());

            // Checking for Authorization header in the incoming request and is in correct format (Bearer token).
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Missing or invalid Authorization header");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // Extracting the token from the Authorization header.
            final String token = authHeader.split("Bearer ")[1];

            try {
                // Extracting user ID from the token using JwtService.
                String userId = jwtService.getUserIdFromToken(token);
                // Adding user ID to the request header so that downstream services can access it.
                ServerWebExchange mutatedExchange = exchange.mutate()
                        .request(request -> request.header("X-User-Id", userId))
                        .build();

                // Passing the mutated exchange to the next filter in the chain.
                return chain.filter(mutatedExchange);
            } catch (JwtException e) {
                log.error("Invalid JWT token: {}", e.getMessage());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };
    }

    // Placeholder config class.
    public static class Config {
    }

}
