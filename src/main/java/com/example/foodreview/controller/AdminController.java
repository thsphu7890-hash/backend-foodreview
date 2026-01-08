package com.example.foodreview.controller;

import com.example.foodreview.model.Order;
import com.example.foodreview.model.User;
import com.example.foodreview.repository.OrderRepository;
import com.example.foodreview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
// 👇 QUAN TRỌNG: Phải có cái này thì React mới gọi được API (tránh lỗi CORS)
@CrossOrigin(origins = "http://localhost:5173") 
public class AdminController {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    // 1. API lấy danh sách tất cả người dùng
    @GetMapping("/users")
    // 👇 QUAN TRỌNG: Chỉ cho phép tài khoản quyền ADMIN truy cập
    @PreAuthorize("hasRole('ADMIN')") 
    public ResponseEntity<List<User>> getAllUsers() {
        // Lưu ý: User entity đã có @JsonIgnore ở password nên an toàn
        return ResponseEntity.ok(userRepository.findAll());
    }

    // 2. API lấy danh sách tất cả đơn hàng (Quản lý đơn)
    @GetMapping("/orders")
    // 👇 QUAN TRỌNG: Chỉ cho phép tài khoản quyền ADMIN truy cập
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }
}