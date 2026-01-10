package com.example.foodreview.repository;// 👈 Quan trọng: Phải nằm trong gói .sql

import com.example.foodreview.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    // Tìm danh mục theo tên (Hỗ trợ tìm kiếm)
    List<Category> findByNameContainingIgnoreCase(String name);
    
    // Kiểm tra tên danh mục đã tồn tại chưa (Dùng khi Admin tạo mới)
    boolean existsByName(String name);
}