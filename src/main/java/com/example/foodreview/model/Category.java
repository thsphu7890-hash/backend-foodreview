package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;

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

    // --- 👇 SỬA LẠI ĐOẠN NÀY 👇 ---
    
    // 1. Đổi @OneToMany thành @ManyToMany
    // 2. Đổi mappedBy = "category" thành "categories" (để khớp với biến bên Food.java)
    @ManyToMany(mappedBy = "categories")
    @JsonIgnore 
    private Set<Food> foods = new HashSet<>(); 
    // Dùng Set thay vì List để tối ưu cho ManyToMany
}