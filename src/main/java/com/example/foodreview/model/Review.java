package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- 1. LIÊN KẾT DỮ LIỆU ---
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Người đánh giá

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food; // Món ăn được đánh giá

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant; // (Mới) Đánh giá thuộc về nhà hàng nào

    @Column(name = "order_id")
    private Long orderId; // (Mới) Review này thuộc đơn hàng nào? (Để hiện badge "Đã mua hàng")

    // --- 2. HỆ THỐNG ĐIỂM SỐ CHI TIẾT (Thang 1-5) ---
    private double rating; // Điểm trung bình tổng (VD: 4.5)

    // (Mới) Các tiêu chí phụ - Giúp chủ quán biết cần sửa gì
    private int tasteRating;    // Hương vị
    private int hygieneRating;  // Vệ sinh
    private int serviceRating;  // Phục vụ (Tốc độ, thái độ)
    private int priceRating;    // Giá cả/Định lượng

    // --- 3. NỘI DUNG & HÌNH ẢNH ---
    @Column(columnDefinition = "TEXT")
    private String comment;

    // (Mới) Danh sách URL ảnh review
    // @ElementCollection giúp lưu List<String> vào bảng phụ review_images
    @ElementCollection 
    @CollectionTable(name = "review_images", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "image_url")
    private List<String> images; 

    // --- 4. TƯƠNG TÁC ---
    // (Mới) Chủ quán trả lời review
    @Column(columnDefinition = "TEXT")
    private String reply; 

    private LocalDateTime replyAt; // Thời gian chủ quán trả lời

    // (Mới) Số người thấy review này hữu ích (Like)
    private int helpfulCount = 0; 

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        // Nếu không có điểm thành phần, mặc định lấy rating tổng
        if (tasteRating == 0) tasteRating = (int) rating;
        if (serviceRating == 0) serviceRating = (int) rating;
    }
}