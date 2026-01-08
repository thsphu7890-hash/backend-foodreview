package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet; // Import thêm
import java.util.Set;     // Import thêm

@Entity
@Data
@Table(name = "food")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;
    private String image;
    
    @Column(length = 500) 
    private String video; 

    @Column(length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    // --- 👇 THAY ĐỔI QUAN TRỌNG: MANY-TO-MANY 👇 ---
    // Xóa dòng @ManyToOne category cũ đi và thay bằng đoạn này:
    
    @ManyToMany
    @JoinTable(
        name = "food_category", // Tên bảng trung gian
        joinColumns = @JoinColumn(name = "food_id"), // Khóa ngoại trỏ về bảng Food
        inverseJoinColumns = @JoinColumn(name = "category_id") // Khóa ngoại trỏ về bảng Category
    )
    private Set<Category> categories = new HashSet<>();
    // -----------------------------------------------
}