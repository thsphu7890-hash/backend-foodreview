package com.example.foodreview.repository;

import com.example.foodreview.model.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Import Query
import org.springframework.data.repository.query.Param; // Import Param
import org.springframework.stereotype.Repository;

import java.util.List; // Import List

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    
    // --- 1. CÁC HÀM CHO APP (REACT) ---

    // Tìm theo Tên
    Page<Food> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Tìm theo Danh mục (Lưu ý: Categories số nhiều do quan hệ Many-to-Many)
    Page<Food> findByCategories_Id(Long categoryId, Pageable pageable);

    // Tìm theo Danh mục VÀ Tên
    Page<Food> findByCategories_IdAndNameContainingIgnoreCase(Long categoryId, String name, Pageable pageable);
    
    // Tìm theo Nhà hàng
    Page<Food> findByRestaurant_Id(Long restaurantId, Pageable pageable);

    // --- 2. HÀM CHO BOT (n8n/AI) ---
    // Hàm này giúp Bot tìm kiếm gần đúng theo tên món ăn
    // Chúng ta dùng @Query để tối ưu và không bị phụ thuộc vào tên hàm dài ngoằng
    @Query("SELECT f FROM Food f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Food> searchFoodForBot(@Param("keyword") String keyword, Pageable pageable);
}