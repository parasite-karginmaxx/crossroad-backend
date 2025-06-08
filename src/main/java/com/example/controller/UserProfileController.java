package com.example.controller;

import com.example.dto.request.UserProfileRequest;
import com.example.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserProfileController {
    private final UserProfileService userProfileService;

    @PatchMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateProfile(@RequestBody UserProfileRequest request) {
        userProfileService.updateCurrentUserProfile(request);
        return ResponseEntity.ok("Профиль обновлён");
    }

}
