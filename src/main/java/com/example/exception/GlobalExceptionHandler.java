package com.example.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException e) {
        return ResponseEntity.status(403).body("Ошибка доступа: " + e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArg(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body("Неверные данные: " + e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntime(RuntimeException e) {
        return ResponseEntity.status(404).body("Ошибка: " + e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElement(NoSuchElementException e) {
        return ResponseEntity.status(404).body("Ошибка: " + e.getMessage());
    }

    @ExceptionHandler(org.springframework.security.authentication.DisabledException.class)
    public ResponseEntity<String> handleDisabled(org.springframework.security.authentication.DisabledException e) {
        return ResponseEntity.status(403).body("Пользователь заблокирован");
    }

    @ExceptionHandler(com.example.exception.IncompleteProfileException.class)
    public ResponseEntity<String> handleIncompleteProfile(com.example.exception.IncompleteProfileException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
