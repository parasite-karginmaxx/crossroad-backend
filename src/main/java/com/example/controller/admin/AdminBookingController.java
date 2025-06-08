package com.example.controller.admin;

import com.example.dto.response.BookingResponse;
import com.example.enums.BookingStatus;
import com.example.service.BookingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Бронирования (Админ)", description = "Управление всеми бронированиями")
@RestController
@RequestMapping("/api/admin/bookings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminBookingController {

    private final BookingService bookingService;

    @GetMapping("/all")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookingResponses());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam BookingStatus status) {
        bookingService.updateBookingStatusByAdmin(id, status);
        return ResponseEntity.ok("Бронирование #" + id + " обновлено до: " + status);
    }

    @PutMapping("/{id}/approve-extension")
    public ResponseEntity<String> approveExtension(@PathVariable Long id) {
        bookingService.approveBookingExtension(id);
        return ResponseEntity.ok("Продление подтверждено");
    }

    @PutMapping("/{id}/reject-extension")
    public ResponseEntity<String> rejectExtension(@PathVariable Long id) {
        bookingService.rejectBookingExtension(id);
        return ResponseEntity.ok("Продление отклонено");
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok("Бронирование #" + id + " удалено");
    }
}
