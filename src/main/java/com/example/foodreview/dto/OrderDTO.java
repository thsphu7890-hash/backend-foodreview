package com.example.foodreview.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;

    // --- 1. THÊM THÔNG TIN NGƯỜI ĐẶT (USER) ---
    private Long userId; // Quan trọng: Để Backend biết ai đang đặt đơn này
    
    private String customerName;
    private String phone;
    private String address;
    
    // --- 2. THÊM THÔNG TIN TÀI XẾ (DRIVER) ---
    // Để khi khách xem đơn, biết ai đang giao và gọi điện được
    private Long driverId;
    private String driverName;
    private String driverPhone;

    // --- 3. THÔNG TIN QUÁN (Tiện cho App Tài xế hiển thị) ---
    // Dù OrderItem có thông tin món, nhưng để hiển thị nhanh ở danh sách đơn, nên có tên quán
    private String restaurantName; 
    private String restaurantAddress;

    // Thông tin đơn hàng
    private Double totalAmount;
    private String paymentMethod; 
    private String status;
    private String note; // Ghi chú của khách (VD: "Ít cay, nhiều tương")
    private LocalDateTime createdAt;

    // Danh sách món
    private List<OrderItemDTO> items;
}