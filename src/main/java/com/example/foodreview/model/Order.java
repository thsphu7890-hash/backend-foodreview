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

    // Thông tin người đặt (Khách hàng)
    @ManyToOne 
    @JoinColumn(name = "user_id") // Nên liên kết với bảng User để biết ai đặt
    private User user; 
    
    private String customerName;
    private String phone;
    private String address;
    private Double totalAmount;
    
    private String paymentMethod; // "CASH", "BANK_TRANSFER", "MOMO"
    private String status;        // "PENDING", "CONFIRMED", "SHIPPING", "COMPLETED", "CANCELLED"
    
    // --- 👇 BẠN CẦN THÊM ĐOẠN NÀY CHO MODULE TÀI XẾ 👇 ---
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private User driver; // Lưu thông tin Tài xế nhận đơn
    // -----------------------------------------------------

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}