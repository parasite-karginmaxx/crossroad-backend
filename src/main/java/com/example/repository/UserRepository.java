package com.example.repository;

import com.example.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(@NotBlank(message = "Имя поля обязательно") String username);
    Optional<User> findByUsername(String username);
}

