package com.example.foodreview.dto;

import lombok.Data;

@Data
public class ChatRequest {
    private String message;
    private String userId;
}