package com.example.enums;

public enum BookingStatus {
    PENDING,        // Ожидает подтверждения
    CONFIRMED,      // Подтверждено
    REJECTED,       // Админ отказал
    CANCELLED,      // Клиент отменил
    ACTIVE,         // Посетитель проживает сейчас
    COMPLETED,       // Завершено
    EXTENSION_REQUESTED // Запрос на продление от пользователя
    }

