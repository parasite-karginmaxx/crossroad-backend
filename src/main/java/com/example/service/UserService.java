package com.example.service;

import com.example.dto.request.RegisterRequest;
import com.example.enums.UserStatus;
import com.example.model.Role;
import com.example.model.User;
import com.example.model.UserProfile;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final UserValidator userValidator;

    public User saveUser(RegisterRequest request, String roleName) {
        userValidator.validateRegistration(request);

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

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));
    }

    public void blockUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        userValidator.validateUserCanBeBlocked(user);

        user.setStatus(UserStatus.BLOCKED);
        userRepository.save(user);
    }

    public User getUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));
        userRepository.delete(user);
    }
}