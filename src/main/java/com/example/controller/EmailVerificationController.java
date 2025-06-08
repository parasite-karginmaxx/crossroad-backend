package com.example.controller;

import com.example.dto.request.VerificationRequest;
import com.example.service.EmailVerificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Верификация Email", description = "Подтверждение почты")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyEmail(@Valid @RequestBody VerificationRequest request) {
        emailVerificationService.confirmCode(request);
        return ResponseEntity.ok("Email успешно подтвержден");
    }
}
