package com.example.foodreview.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*; 
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders") // Tên bảng là số nhiều để tránh từ khóa SQL 'ORDER'
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // SỬA 1: Nên dùng LAZY để tối ưu hiệu năng. User luôn tồn tại nên nullable = false (tùy logic)
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "user_id") 
    private User user; 
    
    private String customerName;
    private String phone;
    private String address;
    private Double totalAmount;
    
    private String paymentMethod; 
    private String status;
    private String note;
    
    // --- QUAN HỆ DRIVER (QUAN TRỌNG NHẤT) ---
    // SỬA 2: Thêm nullable = true.
    // Lý do: Khi mới đặt hàng (PENDING), CHƯA CÓ tài xế nhận đơn.
    // Nếu không có dòng này (hoặc DB set Not Null), việc lưu đơn sẽ bị lỗi 500 ngay.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = true)
    private Driver driver; 
    // -------------------

    private LocalDateTime createdAt;

    // SỬA 3: CascadeType.ALL là CHÍNH XÁC. 
    // Nó giúp khi bạn lưu Order, nó tự động lưu luôn các OrderItem bên trong.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference 
    private List<OrderItem> items;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) status = "PENDING";
    }
}