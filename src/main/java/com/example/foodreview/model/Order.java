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

    // 1. NGƯỜI ĐẶT (KHÁCH HÀNG) -> Vẫn giữ là User
    @ManyToOne 
    @JoinColumn(name = "user_id") 
    private User user; 
    
    private String customerName;
    private String phone;
    private String address;
    private Double totalAmount;
    
    private String paymentMethod; 
    private String status;
    
    // --- 👇 SỬA ĐOẠN NÀY 👇 ---
    // Thay vì "private User driver", hãy đổi thành "private Driver driver"
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver; 
    // ---------------------------

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}