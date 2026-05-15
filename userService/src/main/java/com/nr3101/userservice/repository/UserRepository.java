package com.nr3101.userservice.repository;

import com.nr3101.userservice.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(@Email(message = "Invalid email format") @NotBlank(message = "Email is required") String email);
}