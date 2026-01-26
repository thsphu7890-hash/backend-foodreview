package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList; // Đổi import
import java.util.List;      // Đổi import

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // mappedBy trỏ đến biến "categories" trong lớp Food.java
    @ManyToMany(mappedBy = "categories")
    @JsonIgnore 
    private List<Food> foods = new ArrayList<>(); // 👇 ĐÃ SỬA: Set -> List
}