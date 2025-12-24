package com.example.foodreview.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private Long id;
    private Long userId;    // Frontend gửi userId
    private Long foodId;    // Frontend gửi foodId
    private Integer rating;
    private String comment;
    private String username; // Trả về cho Frontend hiển thị
    private LocalDateTime createdAt;
}