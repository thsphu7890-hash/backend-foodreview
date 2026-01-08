package com.example.foodreview.mapper;

import com.example.foodreview.dto.CategoryDTO;
import com.example.foodreview.model.Category;
import org.springframework.stereotype.Component;

@Component // Đánh dấu để Spring Boot tự động nạp (Inject) vào Service
public class CategoryMapper {

    // 1. Chuyển từ Entity -> DTO (Để trả về cho Frontend)
    public CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }

        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        // Map thêm các trường khác nếu có
        return dto;
    }

    // 2. Chuyển từ DTO -> Entity (Để tạo mới)
    public Category toEntity(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }

        Category category = new Category();
        // Không set ID vì khi tạo mới ID tự tăng
        category.setName(dto.getName());
        return category;
    }

    // 3. Update dữ liệu từ DTO vào Entity có sẵn (Dùng cho hàm Update)
    public void updateEntity(Category entity, CategoryDTO dto) {
        if (dto == null || entity == null) {
            return;
        }

        // Chỉ cập nhật các trường thông tin, TUYỆT ĐỐI KHÔNG cập nhật ID
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
    }
}