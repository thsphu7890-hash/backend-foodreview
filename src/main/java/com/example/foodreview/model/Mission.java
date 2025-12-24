package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "missions")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    
    // "ORDER_COUNT" hoặc "SPEND_TOTAL"
    private String type;         
    
    private int targetValue;
    private Long rewardVoucherId;
    private String icon;

    // 👇 THÊM TRƯỜNG NÀY: "ONCE" (Làm 1 lần) hoặc "DAILY" (Làm lại mỗi ngày)
    private String frequency; 
}