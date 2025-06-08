package com.example.controller;

import com.example.dto.request.AuthRequest;
import com.example.dto.request.RegisterRequest;
import com.example.service.AuthService;
import com.example.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Аутентификация", description = "Эндпоинты для авторизации, регистрации и т.д.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        return authService.loginWithRoleCheck(request, "ROLE_USER"); // без проверки роли
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody AuthRequest request) {
        return authService.loginWithRoleCheck(request, "ROLE_ADMIN"); // проверка роли
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.saveUser(request, "ROLE_USER"));
    }

    @PostMapping("/admin/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.saveUser(request, "ROLE_ADMIN"));
    }

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
