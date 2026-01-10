package com.example.foodreview.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long id;
    
    // Thông tin món ăn
    private Long foodId;
    private String foodName;
    private String foodImage; 
    
    private Double price;    // Giá lúc đặt
    private Integer quantity; // Số lượng

    // Trạng thái đánh giá (Để hiện nút "Đánh giá" hoặc "Đã đánh giá")
    private Boolean isReviewed = false; 
}