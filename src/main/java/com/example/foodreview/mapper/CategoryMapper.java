package com.example.foodreview.mapper;

import com.example.foodreview.dto.CategoryDTO;
// SỬA TẠI ĐÂY: Import đúng Model Category của bạn
import com.example.foodreview.model.Category; 
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    // Chuyển từ Entity sang DTO để trả về cho Client
    public CategoryDTO toDTO(Category entity) {
        if (entity == null) return null;

        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    // Chuyển từ DTO sang Entity để lưu vào Database
    public Category toEntity(CategoryDTO dto) {
        if (dto == null) return null;

        Category entity = new Category();
        // Lưu ý: Thường ID sẽ tự tăng nên không set ID khi tạo mới
        entity.setName(dto.getName());
        return entity;
    }

    // Cập nhật dữ liệu từ DTO vào Entity đã tồn tại
    public void updateEntity(Category entity, CategoryDTO dto) {
        if (entity == null || dto == null) return;

        entity.setName(dto.getName());
    }
}