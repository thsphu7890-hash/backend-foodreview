package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "food")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private double price;
    private String image; 
    private String video; 

    // Quan hệ với Nhà hàng - Dùng EAGER để tránh lỗi Lazy 500
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    // Quan hệ với Danh mục
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "food_categories",
        joinColumns = @JoinColumn(name = "food_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();
}