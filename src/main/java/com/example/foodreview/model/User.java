package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;
    private String avatar;
    
    // --- 👇 QUAN TRỌNG: THÊM CÁC TRƯỜNG NÀY ĐỂ HẾT LỖI ORDER SERVICE 👇 ---
    private String fullName; // Họ tên đầy đủ
    private String phone;    // Số điện thoại (OrderService đang gọi cái này)
    private String address;  // Địa chỉ mặc định (OrderService đang gọi cái này)
    // ----------------------------------------------------------------------

    private String role; // "ROLE_USER", "ROLE_ADMIN", "ROLE_DRIVER"
    
    @Column(columnDefinition = "boolean default false")
    private Boolean locked;

    // --- 👇 ĐIỂM TÍCH LŨY (Cho tính năng Voucher/Game) 👇 ---
    @Column(columnDefinition = "integer default 0")
    private int points = 0; 
    // --------------------------------------------------------

    @PrePersist
    public void prePersist() {
        if (this.role == null) this.role = "ROLE_USER";
        if (this.locked == null) this.locked = false;
        // Đảm bảo điểm không âm
        if (this.points < 0) this.points = 0; 
    }
}