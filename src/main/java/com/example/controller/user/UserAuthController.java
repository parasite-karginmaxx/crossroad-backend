package com.example.controller.user;

import com.example.dto.request.AuthRequest;
import com.example.dto.request.RegisterRequest;
import com.example.service.AuthService;
import com.example.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Аутентификация (Пользователь)", description = "Эндпоинты для авторизации, регистрации и обновления токенов")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserAuthController {

    private final AuthService authService;
    private final UserService userService;

    @Operation(summary = "Войти как пользователь")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        return authService.loginWithRoleCheck(request, "ROLE_USER"); // без проверки роли
    }

    @Operation(summary = "Зарегистрироваться как пользователь")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.saveUser(request, "ROLE_USER"));
    }

    @Operation(summary = "Обновить токены")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Токен отсутствует");
        }

        String refreshToken = authHeader.substring(7);
        return authService.refresh(refreshToken);
    }
}
