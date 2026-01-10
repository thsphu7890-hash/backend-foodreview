package com.example.foodreview.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    
    private String customerName;
    private String phone;
    private String address;
    
    private Double totalAmount;
    private String status;         // PENDING, SHIPPING, COMPLETED, CANCELLED
    private String paymentMethod;  // COD, PAYPAL...
    private LocalDateTime createdAt;

    // Thông tin tài xế (nếu có)
    private Long driverId;
    private String driverName;
    private String driverPhone;

    // 👇 QUAN TRỌNG: Danh sách món ăn
    private List<OrderItemDTO> items;
}