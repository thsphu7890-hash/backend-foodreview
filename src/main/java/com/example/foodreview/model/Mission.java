package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "missions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // Tên nhiệm vụ
    
    private String description; // Mô tả

    // Loại: ORDER_COUNT (Đếm đơn), SPEND_TOTAL (Tổng tiền)
    private String type; 

    // Mục tiêu: VD 5 (đơn) hoặc 500000 (vnđ)
    private Integer targetValue;

    // Tần suất: ONCE (1 lần), DAILY (Hằng ngày)
    private String frequency;

    // Icon: gift, zap, target
    private String icon;

    // ID Voucher phần thưởng
    private Long rewardVoucherId;
}