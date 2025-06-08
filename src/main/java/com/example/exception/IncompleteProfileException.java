package com.example.exception;

public class IncompleteProfileException extends RuntimeException {
    public IncompleteProfileException(String message) {
        super("Профиль пользователя не заполнен: " + message);
    }
}
