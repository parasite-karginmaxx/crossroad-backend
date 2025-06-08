package com.example.util;

import com.example.model.User;
import com.example.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;

public class ControllerUtils {

    public static User resolveUser(UserDetails userDetails, UserService userService) {
        if (userDetails == null) {
            throw new AccessDeniedException("Вы не авторизованы");
        }

        return userService.getUserByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }
}
