package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String image; // URL ảnh

    private LocalDate date; // Ngày diễn ra (Lưu dạng Date để dễ sắp xếp)

    private String time; // Ví dụ: "09:00 - 22:00"

    private String location;

    @Enumerated(EnumType.STRING)
    private EventStatus status; // HAPPENING, UPCOMING, ENDED

    @Column(columnDefinition = "TEXT")
    private String description; // Mô tả chi tiết (Frontend gọi là 'desc')

    private Integer interested; // Số người quan tâm
}