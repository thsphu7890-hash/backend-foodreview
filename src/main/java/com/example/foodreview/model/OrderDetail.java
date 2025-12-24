package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ N-1 với Order
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // Quan hệ N-1 với Food (Món ăn)
    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    private Integer quantity;
    private Double price; // Lưu giá tại thời điểm mua (tránh việc sau này giá món tăng làm sai lịch sử)
}