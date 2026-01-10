package com.example.foodreview.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewDTO {
    // 👇 ID kiểu Long để khớp với MySQL
    private Long id; 
    
    // Info User
    private Long userId;
    private String username;
    private String userAvatar;

    // Info Food & Order
    private Long foodId;
    private String foodName;
    private Long orderId;

    // Review Content
    private int rating;
    
    // 👇 THÊM CÁC TRƯỜNG NÀY ĐỂ HẾT LỖI GẠCH ĐỎ Ở MAPPER
    private Double tasteRating;
    private Double hygieneRating;
    private Double serviceRating;
    private Double priceRating;

    private String comment;
    
    // 👇 Frontend cần List để hiển thị ảnh
    private List<String> images; 
    
    // Tương tác
    private int helpfulCount;
    private String reply;
    private LocalDateTime replyAt;
    
    private LocalDateTime createdAt;
}