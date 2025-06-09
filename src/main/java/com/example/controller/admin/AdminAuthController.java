package com.example.controller.admin;

import com.example.dto.request.AuthRequest;
import com.example.dto.request.RegisterRequest;
import com.example.service.AuthService;
import com.example.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Аутентификация (Админ)", description = "Эндпоинты для авторизации и регистрации администратора")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/admin")
public class AdminAuthController {

    private final AuthService authService;
    private final UserService userService;

    @Operation(summary = "Войти как администратор")
    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody AuthRequest request) {
        return authService.loginWithRoleCheck(request, "ROLE_ADMIN"); // проверка роли
    }

    @Operation(summary = "Зарегистрировать нового администратора")
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.saveUser(request, "ROLE_ADMIN"));
    }
}
