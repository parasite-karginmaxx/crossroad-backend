package com.example.service;

import com.example.dto.request.VerificationRequest;
import com.example.model.EmailVerificationToken;
import com.example.model.User;
import com.example.enums.UserStatus;
import com.example.repository.EmailVerificationRepository;
import com.example.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;

    public void sendVerificationCode(User user) {
        String code = generate6DigitCode();
        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .token(code)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .confirmed(false)
                .build();

        tokenRepository.save(verificationToken);

        String text = "Ваш код подтверждения: " + code;
        emailSenderService.send(user.getEmail(), "Код подтверждения", text);
    }

    @Transactional
    public void confirmCode(VerificationRequest request) {
        EmailVerificationToken token = tokenRepository.findByToken(request.getCode())
                .orElseThrow(() -> new IllegalArgumentException("Неверный код"));

        if (!token.getUser().getEmail().equals(request.getEmail())) {
            throw new IllegalArgumentException("Неверный email");
        }

        if (token.isConfirmed()) {
            throw new IllegalStateException("Код уже был использован");
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Срок действия кода истек");
        }

        token.setConfirmed(true);
        User user = token.getUser();
        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);
    }

    private String generate6DigitCode() {
        return String.format("%06d", (int)(Math.random() * 1_000_000));
    }
}
