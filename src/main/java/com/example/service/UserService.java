package com.example.service;

import com.example.dto.request.RegisterRequest;
import com.example.enums.UserStatus;
import com.example.model.Role;
import com.example.model.User;
import com.example.model.UserProfile;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;

    public User saveUser(RegisterRequest request, String roleName) {
        validateRegistrationData(request);

        Role role = roleRepository.findByRole(roleName)
                .orElseGet(() -> roleRepository.save(new Role(null, roleName)));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .registrationDate(LocalDate.now())
                .status(UserStatus.PENDING)
                .build();

        UserProfile profile = new UserProfile();
        profile.setUser(user);
        user.setProfile(profile);

        User savedUser = userRepository.save(user);

        emailVerificationService.sendVerificationCode(savedUser);

        return userRepository.save(user);
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Пользователь с ID " + id + " не найден");
        }
        userRepository.deleteById(id);
    }

    private void validateRegistrationData(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }

        if (isBlank(request.getPassword())) {
            throw new IllegalArgumentException("Пароль обязателен");
        }

        if (isBlank(request.getEmail())) {
            throw new IllegalArgumentException("Email обязателен");
        }

        if (!request.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Некорректный формат электронной почты");
        }
    }
}