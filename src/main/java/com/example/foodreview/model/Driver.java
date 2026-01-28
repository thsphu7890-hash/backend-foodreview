package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal; // Dùng cái này cho tiền tệ mới chuẩn
import java.time.LocalDateTime;

@Entity
@Table(name = "drivers")
@Getter
@Setter // ⚠️ Thay @Data bằng @Getter @Setter để tránh vòng lặp vô tận toString()
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- 1. LIÊN KẾT TÀI KHOẢN ---
    // User là "chủ sở hữu" của thông tin đăng nhập
    @OneToOne(fetch = FetchType.EAGER) // Eager để lấy luôn thông tin user khi query driver
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // --- 2. THÔNG TIN CÁ NHÂN ---
    private String fullName;
    private String phone;
    private String email;
    private String address;
    
    // --- 3. THÔNG TIN XE & CCCD ---
    private String idCardNumber;      
    private String vehicleType;       
    private String licensePlate;      

    // --- 4. HÌNH ẢNH ---
    private String idCardFrontImage;  
    private String idCardBackImage;   
    private String avatar;            

    // --- 5. TRẠNG THÁI & VÍ ---
    // PENDING, ACTIVE, OFFLINE, BLOCKED
    private String status;

    // ⚠️ QUAN TRỌNG: Dùng BigDecimal cho tiền để tránh lỗi làm tròn số thực
    @Column(precision = 19, scale = 2) // Ví dụ: 123456789.00
    private BigDecimal walletBalance; 

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "PENDING";
        if (this.walletBalance == null) this.walletBalance = BigDecimal.ZERO;
    }
}