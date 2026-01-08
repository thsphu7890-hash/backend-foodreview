package com.example.foodreview.dto;

import lombok.Data;

@Data
public class LoginRequest {
    // Sửa thành username
    private String username;
    private String password;
}