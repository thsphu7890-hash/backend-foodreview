package com.example.foodreview.mapper;

import com.example.foodreview.dto.FoodDTO;
import com.example.foodreview.model.Category;
import com.example.foodreview.model.Food;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FoodMapper {

    // 1. Chuyển từ Entity -> DTO (Gửi cho Frontend hiển thị)
    public FoodDTO toDTO(Food food) {
        if (food == null) {
            return null;
        }

        FoodDTO dto = new FoodDTO();
        dto.setId(food.getId());
        dto.setName(food.getName());
        dto.setPrice(food.getPrice());
        dto.setDescription(food.getDescription());
        dto.setImage(food.getImage());
        dto.setVideo(food.getVideo()); // Link video

        // Map thông tin Nhà hàng
        if (food.getRestaurant() != null) {
            dto.setRestaurantId(food.getRestaurant().getId());
            dto.setRestaurantName(food.getRestaurant().getName());
        }

        // --- 👇 SỬA ĐỔI CHO MANY-TO-MANY 👇 ---
        // Thay vì map 1 category, ta map danh sách categories
        if (food.getCategories() != null && !food.getCategories().isEmpty()) {
            // Lấy danh sách ID
            dto.setCategoryIds(food.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toList()));

            // Lấy danh sách Tên (để hiển thị badge trên thẻ Card)
            dto.setCategoryNames(food.getCategories().stream()
                    .map(Category::getName)
                    .collect(Collectors.toList()));
        }
        // ---------------------------------------

        return dto;
    }

    // 2. Chuyển từ DTO -> Entity (Lưu vào DB)
    public Food toEntity(FoodDTO dto) {
        if (dto == null) {
            return null;
        }

        Food food = new Food();
        food.setName(dto.getName());
        food.setPrice(dto.getPrice());
        food.setDescription(dto.getDescription());
        food.setImage(dto.getImage());
        food.setVideo(dto.getVideo());

        // LƯU Ý: Việc map danh sách Category từ `dto.categoryIds` sang `Set<Category>`
        // cần gọi đến Repository, nên sẽ được thực hiện trong FoodService
        // chứ không làm ở Mapper này để tránh lỗi logic và phụ thuộc vòng.
        
        return food;
    }
}