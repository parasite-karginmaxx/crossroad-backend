package com.example.service;

import com.example.dto.request.UserProfileRequest;
import com.example.model.User;
import com.example.model.UserProfile;
import com.example.repository.UserProfileRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    public void updateCurrentUserProfile(UserProfileRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        UserProfile profile = user.getProfile();

        validateProfileFields(request);

        updateProfile(profile, request);

        userProfileRepository.save(profile);
    }

    private void validateProfileFields(UserProfileRequest request) {
        String passport = request.getPassportNumber();
        if (passport != null && !passport.matches("\\d{10}")) {
            throw new IllegalArgumentException("Паспорт должен содержать ровно 10 цифр");
        }

        String phone = request.getPhone();
        if (phone != null && !phone.matches("^((\\+7)|8)\\d{10}$")) {
            throw new IllegalArgumentException("Телефон должен начинаться с +7 или 8 и содержать 11 цифр");
        }
    }

    private void updateProfile(UserProfile profile, UserProfileRequest request) {
        if (request.getFirstName() != null) profile.setFirstName(request.getFirstName());
        if (request.getLastName() != null) profile.setLastName(request.getLastName());
        if (request.getMiddleName() != null) profile.setMiddleName(request.getMiddleName());
        if (request.getPhone() != null) profile.setPhone(request.getPhone());
        if (request.getGender() != null) profile.setGender(request.getGender());
        if (request.getBirthDate() != null) profile.setBirthDate(request.getBirthDate());
        if (request.getPassportNumber() != null) profile.setPassportNumber(request.getPassportNumber());
        if (request.getAddress() != null) profile.setAddress(request.getAddress());
        if (request.getCitizenship() != null) profile.setCitizenship(request.getCitizenship());
    }
}
