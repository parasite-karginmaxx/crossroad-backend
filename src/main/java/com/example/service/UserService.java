package com.example.service;

import com.example.dto.request.RegisterRequest;
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

    public User saveUser(RegisterRequest request, String roleName) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Role role = roleRepository.findByRole(roleName)
                .orElseGet(() -> roleRepository.save(
                        Role.builder().role(roleName).build()
                ));
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encodedPassword)
                .role(role)
                .registrationDate(LocalDate.now())
                .build();

        UserProfile profile = new UserProfile();
        profile.setUser(user);
        user.setProfile(profile);

        return userRepository.save(user);
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
}