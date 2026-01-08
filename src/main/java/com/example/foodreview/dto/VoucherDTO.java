package com.example.foodreview.dto;

import com.example.foodreview.model.VoucherType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VoucherDTO {
    private Long id;
    private String code;
    private Double percent;
    private Double maxDiscount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private VoucherType type;
    private Double conditionValue;
}