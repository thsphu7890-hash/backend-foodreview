package com.example.foodreview.controller;

import com.example.foodreview.model.User;
import com.example.foodreview.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // --- 👇 QUAN TRỌNG: THÊM HÀM NÀY ĐỂ SỬA LỖI 404 👇 ---
    // Hàm này sẽ chạy khi React gọi GET /api/users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        // Trả về tất cả user (cả khách lẫn tài xế) cho Admin xem
        return ResponseEntity.ok(userRepository.findAll());
    }
    // ----------------------------------------------------

    // 1. Lấy danh sách Tài xế (Dành cho tab Drivers riêng nếu cần)
    @GetMapping("/drivers")
    public ResponseEntity<List<User>> getAllDrivers() {
        return ResponseEntity.ok(userRepository.findByRole("ROLE_DRIVER"));
    }
    
    // 2. Lấy danh sách Khách hàng
    @GetMapping("/customers")
    public ResponseEntity<List<User>> getAllCustomers() {
        return ResponseEntity.ok(userRepository.findByRole("ROLE_USER"));
    }

    // 3. Khóa/Mở khóa tài khoản
    @PutMapping("/{id}/toggle-lock")
    public ResponseEntity<?> toggleLock(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        
        // Đảo ngược trạng thái khóa
        if (user.getLocked() == null) user.setLocked(false);
        user.setLocked(!user.getLocked());
        
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }
    
    // 4. API Upload Avatar (Thêm cái này để tránh lỗi 404 ở trang Profile)
    // Nếu bạn chưa làm Service upload thì tạm thời trả về thông báo
    @PostMapping("/{id}/avatar")
    public ResponseEntity<?> uploadAvatar(@PathVariable Long id) {
        return ResponseEntity.ok("Tính năng đang phát triển");
    }
}