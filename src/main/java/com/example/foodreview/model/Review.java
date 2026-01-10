package com.example.foodreview.model;

import jakarta.persistence.*; // 👈 Import của MySQL (JPA)
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity // 👈 Đánh dấu là bảng MySQL
@Table(name = "reviews")
public class Review {

    // 1. ID tự tăng (MySQL dùng Long, không dùng String)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 2. Các khóa ngoại (Lưu ID tham chiếu)
    private Long userId;
    private Long foodId;
    private Long orderId; // Lưu ID đơn hàng để check đã mua hay chưa
    private Long restaurantId;

    // 3. Điểm số (Khớp với DTO và Mapper)
    private int rating;         // Điểm tổng (VD: 5)
    
    // Các điểm thành phần
    private Double tasteRating;    
    private Double hygieneRating;  
    private Double serviceRating;  
    private Double priceRating;    

    // 4. Nội dung
    @Column(columnDefinition = "TEXT") // Cho phép lưu văn bản dài
    private String comment;

    // 5. Ảnh (QUAN TRỌNG)
    // MySQL khó lưu List trực tiếp, ta lưu 1 chuỗi (String) đường dẫn ảnh.
    // Mapper sẽ tự động bọc nó vào List khi trả về cho Frontend.
    @Column(columnDefinition = "TEXT")
    private String image; 

    // 6. Tương tác
    @Column(columnDefinition = "TEXT")
    private String reply;          // Chủ quán trả lời
    
    private LocalDateTime replyAt; // Thời gian trả lời
    private int helpfulCount = 0;

    private LocalDateTime createdAt = LocalDateTime.now();
}