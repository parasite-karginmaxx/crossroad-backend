package com.example.controller;

import com.example.dto.AuthRequest;
import com.example.dto.RegisterRequest;
import com.example.service.AuthService;
import com.example.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        return authService.loginWithRoleCheck(request, null); // без проверки роли
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
}
