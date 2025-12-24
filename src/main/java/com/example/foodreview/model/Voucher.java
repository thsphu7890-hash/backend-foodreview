package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

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

    private LocalDate startDate; 

    private LocalDate endDate;

    // --- CÁC TRƯỜNG MỚI ĐỂ HỖ TRỢ NGHIỆP VỤ ---

    @Enumerated(EnumType.STRING)
    private VoucherType type; 
    // Các loại: PUBLIC, REWARD_ORDER, GAME_REWARD, POINT_EXCHANGE, EVENT

    private Double conditionValue; 
    // Giá trị điều kiện. Ví dụ: 
    // - Nếu là REWARD_ORDER: Đây là số tiền đơn hàng tối thiểu để được tặng (VD: 500k)
    // - Nếu là POINT_EXCHANGE: Đây là số điểm cần để đổi (VD: 100 điểm)
    // - Nếu là PUBLIC/GAME/EVENT: Có thể để null hoặc 0
}