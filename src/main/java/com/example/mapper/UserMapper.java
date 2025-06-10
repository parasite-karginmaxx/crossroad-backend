package com.example.mapper;

import com.example.dto.response.UserResponse;
import com.example.model.User;
import com.example.model.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        UserProfile profile = user.getProfile();
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .status(user.getStatus().name())
                .role(user.getRole().getRole())
                .fullName(profile != null ? String.join(" ",
                        safe(profile.getLastName()),
                        safe(profile.getFirstName()),
                        safe(profile.getMiddleName())) : null)
                .phone(profile != null ? profile.getPhone() : null)
                .passport(profile != null ? profile.getPassportNumber() : null)
                .citizenship(profile != null ? profile.getCitizenship() : null)
                .build();
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
