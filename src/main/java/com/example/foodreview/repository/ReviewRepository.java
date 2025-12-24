package com.example.foodreview.repository;

import com.example.foodreview.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Tìm tất cả review của 1 món ăn, sắp xếp mới nhất lên đầu
    List<Review> findByFoodIdOrderByCreatedAtDesc(Long foodId);
}