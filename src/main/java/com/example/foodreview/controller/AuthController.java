package com.example.foodreview.controller;

import com.example.foodreview.dto.LoginRequest;
import com.example.foodreview.dto.RegisterRequest;
import com.example.foodreview.dto.UserDTO;
import com.example.foodreview.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}