package com.example.controller.user;

import com.example.dto.request.UserProfileRequest;
import com.example.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Профиль пользователя", description = "Эндпоинты для работы с профилем")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
@PreAuthorize("hasRole('USER')")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Operation(summary = "Обновить данные профился")
    @PatchMapping("/update")
    public ResponseEntity<String> updateProfile(@Valid @RequestBody UserProfileRequest request) {
        userProfileService.updateCurrentUserProfile(request);
        return ResponseEntity.ok("Профиль обновлён");
    }
}
