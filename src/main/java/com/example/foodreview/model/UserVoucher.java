package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "user_vouchers")
public class UserVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    private boolean isUsed = false; // True = Đã xài rồi

    private LocalDate receivedDate; // Ngày nhận được voucher
}