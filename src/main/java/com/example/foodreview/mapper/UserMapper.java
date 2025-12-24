package com.example.foodreview.mapper;

import com.example.foodreview.dto.RegisterRequest;
import com.example.foodreview.dto.UserDTO;
import com.example.foodreview.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Chuyển từ Request đăng ký sang Entity
    public User toEntity(RegisterRequest request) {
        if (request == null) return null;
        User user = new User();
        user.setUsername(request.getUsername());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        return user;
    }

    // Chuyển từ Entity sang DTO (Trả về Frontend)
    public UserDTO toDTO(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        
        // --- MAP THÊM ROLE ---
        dto.setRole(user.getRole());
        
        return dto;
    }
}