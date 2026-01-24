package com.example.foodreview.mapper;

import com.example.foodreview.dto.FoodDTO;
import com.example.foodreview.model.Category;
import com.example.foodreview.model.Food;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class FoodMapper {

    // 1. Chuyển từ Entity (Database) -> DTO (Gửi cho React)
    public FoodDTO toDTO(Food food) {
        if (food == null) {
            return null;
        }

        FoodDTO dto = new FoodDTO();
        dto.setId(food.getId());
        dto.setName(food.getName());
        dto.setPrice(food.getPrice());
        dto.setImage(food.getImage());
        dto.setDescription(food.getDescription());
        dto.setVideo(food.getVideo());
        
        // Thêm tính toán khoảng cách/thời gian giả lập nếu cần (hoặc để null)
        // dto.setTime(...);
        // dto.setDistance(...);

        // --- MAP RESTAURANT ---
        if (food.getRestaurant() != null) {
            dto.setRestaurantId(food.getRestaurant().getId());
            dto.setRestaurantName(food.getRestaurant().getName());
        }

        // --- MAP CATEGORIES (Quan trọng để React hiển thị đúng danh mục) ---
        if (food.getCategories() != null) {
            dto.setCategoryIds(
                food.getCategories().stream()
                    .map(Category::getId)       // Lấy ID của từng category
                    .collect(Collectors.toList()) // Gom lại thành List<Long>
            );
        } else {
            dto.setCategoryIds(new ArrayList<>());
        }

        return dto;
    }

    // 2. Chuyển từ DTO (React gửi lên) -> Entity (Lưu Database)
    public Food toEntity(FoodDTO dto) {
        if (dto == null) {
            return null;
        }

        Food food = new Food();
        // Lưu ý: ID thường tự sinh, update thì set sau
        food.setId(dto.getId()); 
        food.setName(dto.getName());
        food.setPrice(dto.getPrice());
        food.setImage(dto.getImage());
        food.setDescription(dto.getDescription());
        food.setVideo(dto.getVideo());

        // LƯU Ý: 
        // Restaurant và Categories sẽ được set bên Service 
        // (vì cần repo để tìm Entity từ ID) nên ở đây ta bỏ qua.
        
        return food;
    }
}