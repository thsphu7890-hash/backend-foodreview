package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore; // Import cái này để tránh lỗi vòng lặp
import java.util.List;

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

    // 👇 THÊM PHẦN NÀY (Tùy chọn) 👇
    // mappedBy = "category": Tên biến category bên file Food.java
    @OneToMany(mappedBy = "category")
    @JsonIgnore // Quan trọng: Ngăn không cho load dữ liệu lặp vô tận (Category -> Food -> Category...)
    private List<Food> foods;
}