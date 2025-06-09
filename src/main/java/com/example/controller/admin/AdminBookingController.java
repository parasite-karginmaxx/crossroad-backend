package com.example.controller.admin;

import com.example.dto.response.BookingResponse;
import com.example.enums.BookingStatus;
import com.example.model.Booking;
import com.example.service.BookingService;
import com.example.service.BookingStatusService;
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
    private final BookingStatusService statusService;

    @GetMapping("/all")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookingResponses());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam BookingStatus status) {

        Booking booking = bookingService.getBookingById(id);
        statusService.updateStatusManually(booking, status);

        return ResponseEntity.ok("Бронирование #" + id + " обновлено до: " + status);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok("Бронирование #" + id + " удалено");
    }
}
