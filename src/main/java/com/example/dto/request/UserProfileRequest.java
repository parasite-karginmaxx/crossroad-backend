package com.example.dto.request;

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
    private String passportNumber;
    private String address;
    private String citizenship;
}

