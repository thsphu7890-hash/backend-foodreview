package com.example.foodreview.dto;

import lombok.Data;
import java.util.List;

@Data
public class FoodDTO {
    private Long id;
    private String name;
    private Double price;
    private String image;
    private String description;
    private String video;

    // Thông tin nhà hàng
    private Long restaurantId;
    private String restaurantName;

    // Danh sách ID danh mục (React gửi lên dạng: [1, 2, 5])
    private List<Long> categoryIds; 
}