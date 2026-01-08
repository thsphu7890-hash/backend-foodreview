package com.example.foodreview.repository;

import com.example.foodreview.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    // 1. Tìm tất cả review của 1 món ăn (Sắp xếp mới nhất lên đầu)
    List<Review> findByFoodIdOrderByCreatedAtDesc(Long foodId);

    // 2. Kiểm tra xem user đã review món này trong đơn hàng này chưa (tránh spam)
    boolean existsByOrderIdAndFoodId(Long orderId, Long foodId);
}