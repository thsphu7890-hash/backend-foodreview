package com.example.foodreview.dto;

import lombok.Data;

@Data
public class MissionDTO {
    private Long id;
    private String title;
    private String description;
    private int targetValue;
    private int currentValue;  // Tiến độ hiện tại của user
    private boolean isCompleted;
    private boolean isClaimed;
    private String rewardVoucherCode;
    private String icon;
}