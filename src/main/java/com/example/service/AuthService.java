package com.example.service;

import com.example.config.JwtConfig;
import com.example.dto.AuthRequest;
import com.example.dto.AuthResponse;
import com.example.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtConfig jwtConfig;
    private final UserService userService;

    public ResponseEntity<?> loginWithRoleCheck(AuthRequest request, String requiredRole) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        User user = userService.getUserByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (requiredRole != null && !requiredRole.equals(user.getRole().getRole())) {
            return ResponseEntity.status(403)
                    .body("Доступ разрешён только для " + requiredRole);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String jwt = jwtConfig.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}

