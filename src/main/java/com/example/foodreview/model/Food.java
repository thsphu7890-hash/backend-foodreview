package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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

    // Quan hệ với Nhà hàng
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    // --- QUAN TRỌNG: MỐI QUAN HỆ MANY-TO-MANY ---
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "food_category",
        joinColumns = @JoinColumn(name = "food_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>(); 
}