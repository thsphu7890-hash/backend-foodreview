package com.example.foodreview.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReviewRequest {
    private Long foodId;
    private Long orderId; // Để check đã mua hàng chưa
    
    private String comment;
    
    // Điểm thành phần
    private int tasteRating;
    private int hygieneRating;
    private int serviceRating;
    private int priceRating;
    
    // Danh sách ảnh upload
    private List<String> images;
}