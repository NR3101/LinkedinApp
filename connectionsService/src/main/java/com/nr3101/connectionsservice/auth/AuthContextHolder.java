package com.nr3101.connectionsservice.auth;

/**
 * AuthContextHolder is a utility class that uses ThreadLocal to store and retrieve the current authenticated user's ID.
 * This allows us to access the user ID throughout the request processing without passing it explicitly as a parameter.
 * ThreadLocal provides thread-local variables, which means that each thread that accesses the variable via its get or set method has its own, independently initialized copy of the variable. This is particularly useful in web applications where each request is handled by a separate thread.
 */

public class AuthContextHolder {

    private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();

    static void setCurrentUserId(Long userId) {
        userIdHolder.set(userId);
    }

    public static Long getCurrentUserId() {
        return userIdHolder.get();
    }

    static void clear() {
        userIdHolder.remove();
    }
}
