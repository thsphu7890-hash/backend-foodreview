package com.example.foodreview.dto;

import lombok.Data;

@Data
public class ReviewRequest {
    private Long foodId;    // Đánh giá món nào
    
    // 👇 THÊM TRƯỜNG NÀY (Quan trọng để check lịch sử đơn hàng)
    private Long orderId;   
    
    private int rating;     // 1 - 5 sao
    private String comment; // Nội dung
    
    // 👇 String image là chuẩn rồi (Khớp với Entity MySQL mới)
    private String image;   
}