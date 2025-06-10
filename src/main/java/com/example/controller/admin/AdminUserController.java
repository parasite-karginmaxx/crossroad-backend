package com.example.controller.admin;

import com.example.dto.response.UserResponse;
import com.example.mapper.UserMapper;
import com.example.model.User;
import com.example.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Пользователи (Админ)", description = "Эндпоинты пользователей для администраторов")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Получение пользователя по id")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserByIdOrThrow(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Блокирование пользователя")
    @PostMapping("/{id}/block")
    public ResponseEntity<String> blockUser(@PathVariable Long id) {
        userService.blockUserById(id);
        return ResponseEntity.ok("Пользователь #" + id + "заблокирован");
    }

    @Operation(summary = "Удаление пользователя")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("Пользователь #" + id + "удалён");
    }
}
