package com.example.foodreview.repository;

import com.example.foodreview.model.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    // Tìm theo Restaurant ID (Thêm _ cho chuẩn, hoặc giữ nguyên nếu Entity Restaurant có field id)
    List<Food> findByRestaurant_Id(Long restaurantId);

    // --- SỬA CÁC HÀM NÀY (Thêm dấu _ trước Id) ---

    // 1. Tìm theo Category ID
    List<Food> findByCategory_Id(Long categoryId);

    // 2. Tìm theo Tên
    List<Food> findByNameContainingIgnoreCase(String name);

    // 3. Tìm kết hợp Category + Tên
    List<Food> findByCategory_IdAndNameContainingIgnoreCase(Long categoryId, String name);

    // 4. Admin Phân trang
    Page<Food> findByNameContainingIgnoreCase(String name, Pageable pageable);
}