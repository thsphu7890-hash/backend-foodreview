package com.example.foodreview.repository; // 👈 Gói .sql

import com.example.foodreview.model.Review;
import org.springframework.data.jpa.repository.JpaRepository; // 👈 Dùng JPA cho MySQL
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> { 
    // 👆 Lưu ý: ID là Long (không phải String)
    
    // 1. Lấy danh sách review của món ăn
    List<Review> findByFoodIdOrderByCreatedAtDesc(Long foodId);
    
    // 2. Kiểm tra User đã review món này chưa (Tránh spam)
    boolean existsByUserIdAndFoodId(Long userId, Long foodId);

    // 3. 👇 THÊM HÀM NÀY (Để sửa lỗi gạch đỏ trong OrderService)
    boolean existsByOrderIdAndFoodId(Long orderId, Long foodId);
}