package com.example.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileRequest {
    private String firstName;
    private String lastName;
    private String middleName;
    private String phone;
    private String gender;
    private LocalDate birthDate;
}

