package com.example.foodreview.dto;

import lombok.Data;
import java.util.List;

@Data
public class FoodDTO {
    private Long id;
    private String name;
    private Double price;
    private String description;
    private String image;
    private String video;

    // Nhà hàng (Vẫn giữ nguyên Many-to-One)
    private Long restaurantId;
    private String restaurantName;

    // --- 👇 SỬA ĐỔI QUAN TRỌNG (MANY-TO-MANY) 👇 ---
    // Trước đây là: private Long categoryId;
    // Bây giờ phải là List:
    private List<Long> categoryIds;      // Danh sách ID gửi lên khi Tạo/Sửa
    private List<String> categoryNames;  // Danh sách Tên trả về để hiển thị
}