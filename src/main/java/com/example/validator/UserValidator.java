package com.example.validator;

import com.example.dto.request.RegisterRequest;
import com.example.enums.UserStatus;
import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validateRegistration(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }

        if (isBlank(request.getPassword())) {
            throw new IllegalArgumentException("Пароль обязателен");
        }

        if (isBlank(request.getEmail())) {
            throw new IllegalArgumentException("Email обязателен");
        }

        if (!request.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Некорректный формат электронной почты");
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    public void validateUserCanBeBlocked(User user) {
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalStateException("Можно блокировать только пользователей со статусом ACTIVE");
        }
    }
}
