package com.example.foodreview.mapper;

import com.example.foodreview.dto.FoodDTO;
import com.example.foodreview.model.Food;
import org.springframework.stereotype.Component;

@Component // <-- Dùng cái này để Spring quản lý (thay vì @Mapper)
public class FoodMapper {

    // 1. Chuyển từ Entity -> DTO (Gửi cho Frontend)
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

        // Map thông tin Nhà hàng
        if (food.getRestaurant() != null) {
            dto.setRestaurantId(food.getRestaurant().getId());
            dto.setRestaurantName(food.getRestaurant().getName());
        }

        // 👇 QUAN TRỌNG: Map Category ID 👇
        if (food.getCategory() != null) {
            dto.setCategoryId(food.getCategory().getId());
        }

        return dto;
    }

    // 2. Chuyển từ DTO -> Entity (Lưu vào DB)
    public Food toEntity(FoodDTO dto) {
        if (dto == null) {
            return null;
        }

        Food food = new Food();
        // Chỉ map các trường cơ bản, còn Restaurant và Category 
        // sẽ được set trong Service (như code Service bạn đang có)
        food.setName(dto.getName());
        food.setPrice(dto.getPrice());
        food.setDescription(dto.getDescription());
        food.setImage(dto.getImage());
        
        return food;
    }
}