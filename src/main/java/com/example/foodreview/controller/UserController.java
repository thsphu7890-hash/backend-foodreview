package com.example.foodreview.controller;

import com.example.foodreview.dto.UserDTO;
import com.example.foodreview.model.User;
import com.example.foodreview.repository.UserRepository;
import com.example.foodreview.mapper.UserMapper; 
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // 👈 QUAN TRỌNG: Import cái này để phân quyền
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List; // 👈 QUAN TRỌNG: Import List
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // 1. API lấy thông tin Profile của người đang đăng nhập (Code cũ của bạn)
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Chưa đăng nhập");
        }

        String username;
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy user"));

        return ResponseEntity.ok(userMapper.toDTO(user));
    }

    // 2. 👇 ĐÂY LÀ HÀM BẠN ĐANG THIẾU (Để sửa lỗi UserManager.jsx)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Chỉ cho phép ADMIN truy cập
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        // Lấy tất cả user từ database
        List<User> users = userRepository.findAll();
        
        // Chuyển đổi sang DTO để trả về Frontend
        List<UserDTO> userDTOS = users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(userDTOS);
    }
}