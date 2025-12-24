package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "user_missions")
public class UserMission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long missionId;
    
    // 👇 THAY ĐỔI: Lưu ngày nhận thưởng để check Daily
    private LocalDate lastClaimedDate; 
}