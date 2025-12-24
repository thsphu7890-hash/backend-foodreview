package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rating; // 1 đến 5 sao
    
    @Column(columnDefinition = "TEXT")
    private String comment;

    private LocalDateTime createdAt;

    // Quan hệ với User (Ai đánh giá)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Quan hệ với Food (Đánh giá món nào)
    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}