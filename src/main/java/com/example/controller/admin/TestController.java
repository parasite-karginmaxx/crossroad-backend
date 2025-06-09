package com.example.controller.admin;

import com.example.service.BookingStatusService;
import com.example.service.EmailSenderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Тестирование", description = "POST /api/test/send-email?to=example@mail.ru&message=Привет")
@RestController
@RequestMapping("/api/admin/test")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class TestController {

    private final EmailSenderService emailSenderService;
    private final BookingStatusService bookingService;

    @Operation (
            summary = "Отправка тестового письма",
            description = "Отправляет письмо по электронной почте"
    )
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

    @Operation (
            summary = "Обновление статусов бронирований",
            description = "Активирует и завершает бронирования по текущей дате"
    )
    @GetMapping("/booking/update_status")
    public ResponseEntity<String> updateBookingStatus() {
        bookingService.updateStatusAuto();
        return ResponseEntity.ok("Обновление статусов бронирований выполнено");
    }
}
