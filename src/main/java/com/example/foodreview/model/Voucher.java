package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "vouchers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;            // Mã giảm giá

    // 👇 CÁC TRƯỜNG KHỚP VỚI MAPPER CỦA BẠN
    private Double percent;         // Phần trăm giảm (VD: 10 = 10%)
    private Double maxDiscount;     // Giảm tối đa (VD: Giảm 10% nhưng tối đa 50k)
    
    private LocalDateTime startDate; // Ngày bắt đầu
    private LocalDateTime endDate;   // Ngày kết thúc (Thay cho expirationDate cũ)

    @Enumerated(EnumType.STRING)
    private VoucherType type;       // Loại voucher

    private Double conditionValue;  // Điều kiện (VD: Đơn tối thiểu 200k)
    
    private Boolean active = true;  // Trạng thái kích hoạt
}