package com.example.foodreview.dto;
import lombok.Data;

@Data
public class FoodDTO {
    private Long id;
    private String name;
    private Double price;
    private String description; // <--- Đã có
    private String image;
    private Long restaurantId;
    private String restaurantName;
    
    // 👇👇👇 BẮT BUỘC PHẢI THÊM DÒNG NÀY 👇👇👇
    private Long categoryId; 
}