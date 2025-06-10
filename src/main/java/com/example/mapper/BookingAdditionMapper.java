package com.example.mapper;

import com.example.model.Addition;
import com.example.model.Booking;
import com.example.model.BookingAddition;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingAdditionMapper {

    public List<BookingAddition> mapFromAdditions(List<Addition> additions, Booking booking) {
        return additions.stream()
                .map(addition -> BookingAddition.builder()
                        .name(addition.getName())
                        .price(addition.getPrice())
                        .booking(booking)
                        .build())
                .toList();
    }
}
