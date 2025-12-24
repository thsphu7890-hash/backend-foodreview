package com.example.foodreview.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long id;
    
    // ID món ăn khách chọn
    private Long foodId;
    
    // Tên món và ảnh (Dùng để hiển thị lại ở trang Lịch sử đơn hàng mà không cần join nhiều bảng)
    private String foodName;
    private String foodImage;
    
    private Integer quantity; // Số lượng món khách đặt
    private Double price;    // Giá tại thời điểm đặt (để lưu vết nếu sau này món ăn tăng/giảm giá)
}