package com.example.foodreview.mapper;

import com.example.foodreview.dto.RestaurantDTO;
import com.example.foodreview.model.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {

    // Chuyển Entity sang DTO (Để trả về cho Frontend xem)
    public RestaurantDTO toDTO(Restaurant entity) {
        if (entity == null) return null;
        RestaurantDTO dto = new RestaurantDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAddress(entity.getAddress());
        dto.setImage(entity.getImage());
        
        // --- THÊM DÒNG NÀY ---
        dto.setDescription(entity.getDescription()); 

        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
            // Lưu ý: Đảm bảo bên DTO bạn có trường tên là 'categoryName' hoặc 'category'
            // Nếu bên DTO là 'private String category;' thì dùng dto.setCategory(...)
            dto.setCategory(entity.getCategory().getName()); 
        }
        return dto;
    }

    // Chuyển DTO sang Entity (Để lưu vào Database khi Thêm/Sửa)
    public Restaurant toEntity(RestaurantDTO dto) {
        if (dto == null) return null;
        Restaurant restaurant = new Restaurant();
        // ID thường tự sinh nên không set ID ở đây khi tạo mới
        restaurant.setName(dto.getName());
        restaurant.setAddress(dto.getAddress());
        restaurant.setImage(dto.getImage());
        
        // --- THÊM DÒNG NÀY ---
        restaurant.setDescription(dto.getDescription());
        
        return restaurant;
    }
}