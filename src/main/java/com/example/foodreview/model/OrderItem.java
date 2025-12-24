package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Kết nối ngược lại với bảng Order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    // Kết nối với bảng món ăn
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private Food food;

    private Integer quantity; // Số lượng món này trong đơn hàng
    private Double price;    // Giá bán tại thời điểm đặt hàng (để lưu vết nếu sau này Food đổi giá)
}