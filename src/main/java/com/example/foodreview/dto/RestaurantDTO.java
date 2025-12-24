package com.example.foodreview.dto;

import lombok.Data;

@Data
public class RestaurantDTO {
    private Long id;
    private String name;
    private String address;
    private String image;
    
    // --- THÊM DÒNG NÀY ---
    private String description;
    
    private String category; // Tên danh mục (để hiển thị)
    private Long categoryId; // ID danh mục (để update)
}