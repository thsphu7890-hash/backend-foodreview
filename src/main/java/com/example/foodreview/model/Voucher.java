package com.example.foodreview.model;

import com.fasterxml.jackson.annotation.JsonFormat; // Thêm import này
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
    
    // Định dạng yyyy-MM-dd sẽ tự động khớp với chuỗi từ Frontend
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate; 

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private VoucherType type;

    private Double conditionValue;
    
    private Boolean active = true;
}