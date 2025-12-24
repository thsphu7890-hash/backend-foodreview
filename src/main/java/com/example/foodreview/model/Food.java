package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.Data;

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
    
    @Column(length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    // --- 👇 BỔ SUNG LIÊN KẾT DANH MỤC (CATEGORY) 👇 ---
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category; 
    // --------------------------------------------------
}