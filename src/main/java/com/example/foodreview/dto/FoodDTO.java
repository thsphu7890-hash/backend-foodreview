package com.example.foodreview.dto;

import lombok.Data;
import java.util.List; // Nhớ import List

@Data
public class FoodDTO {
    private Long id;
    private String name;
    private Double price;
    private String description;
    private String image;
    private String video;

    private Long restaurantId;
    private String restaurantName;
    
    // --- 👇 SỬA ĐỔI QUAN TRỌNG CHO MANY-TO-MANY 👇 ---
    
    // Thay vì 1 ID, giờ chúng ta nhận một danh sách ID từ Frontend
    private List<Long> categoryIds; 
    
    // Trả về danh sách tên để hiển thị (VD: ["Món chính", "Hải sản"])
    private List<String> categoryNames;
    
    // -------------------------------------------------
}