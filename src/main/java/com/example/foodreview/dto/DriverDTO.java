package com.example.foodreview.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DriverDTO {
    private Long id;
    
    // --- THÔNG TIN USER LIÊN KẾT ---
    private Long userId;     // ID tài khoản User
    private String username; // Tên đăng nhập (Tiện cho Admin tra cứu)

    // --- THÔNG TIN CÁ NHÂN ---
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String avatar; // 👇 Thêm cái này (Quan trọng để hiện ảnh tài xế)

    // --- THÔNG TIN XE & CCCD ---
    private String idCardNumber;
    private String vehicleType;
    private String licensePlate;
    
    // Link ảnh CCCD (Thường chỉ Admin hoặc chính tài xế mới thấy)
    private String idCardFrontImage;
    private String idCardBackImage;
    
    // --- TRẠNG THÁI & VÍ ---
    private String status;
    private Double walletBalance; // 👇 Thêm cái này (Để tài xế xem thu nhập)

    private LocalDateTime createdAt;
}