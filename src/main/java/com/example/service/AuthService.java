package com.example.service;

import com.example.dto.request.AuthRequest;
import com.example.dto.request.RegisterRequest;
import com.example.dto.response.AuthResponse;
import com.example.model.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    public ResponseEntity<?> loginWithRoleCheck(AuthRequest request, String requiredRole) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            User user = userService.getUserByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            if (requiredRole != null && !requiredRole.equals(user.getRole().getRole())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Доступ разрешён только для " + requiredRole);
            }

            String accessToken = jwtService.generateAccessToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Ошибка авторизации: " + e.getMessage());
        }
    }

    public ResponseEntity<?> refresh(String refreshToken) {
        if (!jwtService.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Невалидный refresh токен");
        }

        String tokenType = jwtService.extractClaim(refreshToken, claims -> (String) claims.get("type"));
        if (!"refresh".equals(tokenType)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Ожидался refresh токен");
        }

        String username = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtService.generateAccessToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken));
    }

    public ResponseEntity<?> loginAsUser(AuthRequest request) {
        return loginWithRoleCheck(request, "ROLE_USER");
    }

    public ResponseEntity<?> registerUser(RegisterRequest request) {
        return ResponseEntity.ok(userService.saveUser(request, "ROLE_USER"));
    }

    public ResponseEntity<?> refreshFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Токен отсутствует");
        }

        String refreshToken = authHeader.substring(7);
        return refresh(refreshToken);
    }
}
