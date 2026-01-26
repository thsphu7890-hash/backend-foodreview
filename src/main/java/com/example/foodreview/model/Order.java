package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) // Thêm EAGER để lấy luôn thông tin User
    @JoinColumn(name = "user_id") 
    private User user; 
    
    private String customerName;
    private String phone;
    private String address;
    private Double totalAmount;
    
    private String paymentMethod; 
    private String status;
    
    // --- SỬA TẠI ĐÂY ---
    // 1. Dùng Driver thay vì User
    // 2. Thêm FetchType.EAGER để tránh lỗi "no session"
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver; 
    // -------------------

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}