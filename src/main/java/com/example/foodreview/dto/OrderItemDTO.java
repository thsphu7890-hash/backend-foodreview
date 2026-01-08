package com.example.foodreview.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long foodId;
    private String foodName;
    private String image;
    private double price;
    private int quantity;
    
    // Quan trọng cho tính năng Review
    private Boolean isReviewed; 
}