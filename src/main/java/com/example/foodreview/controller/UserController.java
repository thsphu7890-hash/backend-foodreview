package com.example.foodreview.controller;

import com.example.foodreview.dto.UserDTO;
import com.example.foodreview.model.User;
import com.example.foodreview.repository.UserRepository;
import com.example.foodreview.mapper.UserMapper; 
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Quan trọng để check Role
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*; // Import gộp
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users") // Đảm bảo đúng prefix /api/users
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // ==================================================================
    // 1. API CÁ NHÂN (Ai đăng nhập cũng dùng được)
    // ==================================================================

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

    // ==================================================================
    // 2. API QUẢN TRỊ (Chỉ ADMIN mới dùng được)
    // ==================================================================

    // Lấy danh sách tất cả users (Cho trang UserManager)
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasRole('ADMIN')") // Check quyền Admin
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
        
        // Convert List<User> -> List<UserDTO>
        List<UserDTO> userDTOS = users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(userDTOS);
    }

    // Xóa user theo ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User không tồn tại");
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}