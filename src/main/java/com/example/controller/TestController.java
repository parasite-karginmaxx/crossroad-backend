package com.example.controller;

import com.example.service.BookingService;
import com.example.service.EmailSenderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Тестирование", description = "POST /api/test/send-email?to=example@mail.ru&message=Привет")
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final EmailSenderService emailSenderService;
    private final BookingService bookingService;

    @PostMapping("/send-email")
    public ResponseEntity<String> sendTestEmail(@RequestParam @Email String to,
                                                @RequestParam @NotBlank String message) {
        try {
            emailSenderService.send(to, "Тестовое письмо", message);
            return ResponseEntity.ok("Письмо отправлено на: " + to);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Ошибка при отправке письма: " + e.getMessage());
        }
    }

    @GetMapping("/booking/activate")
    public ResponseEntity<String> activateOngoingBookings() {
        bookingService.activateOngoingBookings();
        return ResponseEntity.ok("Активация бронирований выполнена вручную");
    }
}
