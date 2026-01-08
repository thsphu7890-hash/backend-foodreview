package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "drivers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- 1. LIÊN KẾT TÀI KHOẢN (Để đăng nhập) ---
    // Khi đăng ký xong & Admin duyệt -> sẽ tạo 1 User và gán vào đây
    @OneToOne
    @JoinColumn(name = "user_id") 
    private User user; 

    // --- 2. THÔNG TIN CÁ NHÂN (Snapshot lúc đăng ký) ---
    private String fullName;
    private String phone;
    private String email;
    private String address;
    
    // --- 3. THÔNG TIN XE & CCCD ---
    private String idCardNumber;      // Số CCCD
    private String vehicleType;       // MOTORBIKE / CAR
    private String licensePlate;      // Biển số xe

    // --- 4. HÌNH ẢNH XÁC THỰC ---
    private String idCardFrontImage;  // Ảnh mặt trước CCCD
    private String idCardBackImage;   // Ảnh mặt sau CCCD
    private String avatar;            // Ảnh chân dung tài xế

    // --- 5. TRẠNG THÁI & VÍ ---
    // PENDING (Chờ duyệt), ACTIVE (Đã duyệt/Đang hoạt động), 
    // OFFLINE (Tắt app), BLOCKED (Bị khóa)
    private String status;

    private Double walletBalance = 0.0; // Ví tiền (Để trừ chiết khấu hoặc nhận tiền online)

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "PENDING"; // Mặc định là chờ duyệt
    }
}