package com.testplatform.controller;

import com.testplatform.dto.AuthDto;
import com.testplatform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthDto.RegisterRequest req) {
        try {
            if (req.getName() == null || req.getName().isBlank())
                return ResponseEntity.badRequest().body(Map.of("error", "Имя обязательно"));
            if (req.getEmail() == null || !req.getEmail().contains("@"))
                return ResponseEntity.badRequest().body(Map.of("error", "Некорректный email"));
            if (req.getPassword() == null || req.getPassword().length() < 6)
                return ResponseEntity.badRequest().body(Map.of("error", "Пароль минимум 6 символов"));

            return ResponseEntity.ok(authService.register(req));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDto.LoginRequest req) {
        try {
            return ResponseEntity.ok(authService.login(req));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleAuth(@RequestBody AuthDto.GoogleAuthRequest req) {
        try {
            return ResponseEntity.ok(authService.googleAuth(req));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        try {
            return ResponseEntity.ok(authService.getMe(auth.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
