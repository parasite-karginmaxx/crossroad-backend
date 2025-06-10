package com.example.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String status;
    private String role;

    private String fullName;
    private String phone;
    private String passport;
    private String citizenship;
}
