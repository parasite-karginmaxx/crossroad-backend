package com.example.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomValidator {

    public String validateRoomNumber(String number) {
        if (number == null || number.trim().isEmpty()) {
            return "Номер комнаты не может быть пустым";
        }

        if (number.length() > 4) {
            return "Номер комнаты не может быть длиннее 4 символов";
        }

        return null;
    }
}
