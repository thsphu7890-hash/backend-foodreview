package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "foods")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    
    private String image; // Link ảnh
    
    // 👇 THÊM DÒNG NÀY ĐỂ HẾT LỖI 👇
    private String video; // Link video (Youtube/TikTok...)

    // Quan hệ với Nhà hàng
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    // Quan hệ với Danh mục
    @ManyToMany
    @JoinTable(
        name = "food_categories",
        joinColumns = @JoinColumn(name = "food_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;
}