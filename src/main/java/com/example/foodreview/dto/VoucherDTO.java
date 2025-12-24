package com.example.foodreview.dto;

import com.example.foodreview.model.VoucherType; // Import Enum
import lombok.Data;
import java.time.LocalDate;

@Data
public class VoucherDTO {
    private Long id;
    private String code;
    private Double percent;
    private Double maxDiscount;
    private LocalDate startDate;
    private LocalDate endDate;

    // --- Thêm mới ---
    private VoucherType type;
    private Double conditionValue;
}