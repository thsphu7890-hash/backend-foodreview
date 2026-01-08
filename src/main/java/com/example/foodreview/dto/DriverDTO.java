package com.example.foodreview.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DriverDTO {
    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String idCardNumber;
    private String vehicleType;
    private String licensePlate;
    
    // Link ảnh (nếu cần hiển thị)
    private String idCardFrontImage;
    private String idCardBackImage;
    
    private String status;
    private LocalDateTime createdAt;
}