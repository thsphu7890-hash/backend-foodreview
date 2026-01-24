package com.example.foodreview.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String code;

    private Double percent;
    private Double maxDiscount;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate; 

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    // 👇 SỬA Ở ĐÂY: Thêm length = 50 để tránh lỗi "Data truncated"
    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50) 
    private VoucherType type;

    private Double conditionValue;
    
    private Boolean active = true;
}