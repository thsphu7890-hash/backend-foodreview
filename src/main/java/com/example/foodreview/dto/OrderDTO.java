package com.example.foodreview.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    
    // --- THÔNG TIN NGƯỜI ĐẶT ---
    private Long userId;
    private String customerName;
    private String phone;
    private String address;
    
    // --- THÔNG TIN ĐƠN HÀNG ---
    private Double totalAmount;
    private String paymentMethod;
    private String status;
    private LocalDateTime createdAt;
    
    // Danh sách món ăn
    private List<OrderItemDTO> items;

    // --- 👇 THÔNG TIN TÀI XẾ (MỚI) 👇 ---
    // Chỉ hiển thị khi đơn hàng đã có tài xế nhận (status != PENDING)
    private Long driverId;
    private String driverName;
    private String driverPhone;
    private String driverPlate; // Biển số xe
    private String driverAvatar; // Thêm avatar tài xế cho xịn
}