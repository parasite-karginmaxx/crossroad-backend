package com.example.controller.user;

import com.example.dto.request.AuthRequest;
import com.example.dto.request.RegisterRequest;
import com.example.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Аутентификация (Пользователь)", description = "Эндпоинты для авторизации, регистрации и обновления токенов")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserAuthController {

    private final AuthService authService;

    @Operation(summary = "Войти как пользователь")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        return authService.loginAsUser(request);
    }

    @Operation(summary = "Зарегистрироваться как пользователь")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return authService.registerUser(request);
    }

    @Operation(summary = "Обновить токены")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        return authService.refreshFromRequest(request);
    }
}
