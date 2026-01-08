package com.example.foodreview.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewDTO {
    private Long id;
    
    // --- THÔNG TIN CƠ BẢN ---
    private String comment;
    private double rating; // Đổi sang double để hiển thị 4.5
    private LocalDateTime createdAt;

    // --- LIÊN KẾT ---
    private Long userId;
    private String username;
    private String userAvatar; // (Mới) Để hiện avatar người review
    private Long foodId;
    private String foodName;   // (Mới) Để biết review món gì

    // --- TÍNH NĂNG NÂNG CẤP (MỚI) ---
    private List<String> images; // Danh sách ảnh review
    private Long orderId;        // Mã đơn hàng (Verified Purchase)
    
    // Điểm chi tiết
    private int tasteRating;
    private int hygieneRating;
    private int serviceRating;
    private int priceRating;

    // Phản hồi từ chủ quán
    private String reply;
    private LocalDateTime replyAt;
    private int helpfulCount;
}