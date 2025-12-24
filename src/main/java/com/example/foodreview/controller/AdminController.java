package com.example.foodreview.controller;

import com.example.foodreview.model.Order;
import com.example.foodreview.model.User;
import com.example.foodreview.repository.OrderRepository; // Bạn tự tạo interface này nhé (extends JpaRepository)
import com.example.foodreview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    // 1. API lấy tổng số người dùng
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    // 2. API lấy danh sách đơn hàng
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }
}