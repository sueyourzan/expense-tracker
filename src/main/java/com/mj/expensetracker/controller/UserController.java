package com.mj.expensetracker.controller;

import com.mj.expensetracker.dto.UserDTO;
import com.mj.expensetracker.entity.User;
import com.mj.expensetracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal User user) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", user.getId());
        result.put("email", user.getEmail());
        result.put("username", user.getUsername());
        result.put("phone", user.getPhone());
        result.put("role", user.getRole().name());
        result.put("createdAt", user.getCreatedAt());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/me")
    public ResponseEntity<Map<String, Object>> updateCurrentUser(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UserDTO userDTO) {
        User updated = userService.updateUser(user.getId(), userDTO);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", updated.getId());
        result.put("email", updated.getEmail());
        result.put("username", updated.getUsername());
        result.put("phone", updated.getPhone());
        result.put("role", updated.getRole().name());
        result.put("createdAt", updated.getCreatedAt());
        return ResponseEntity.ok(result);
    }
}
