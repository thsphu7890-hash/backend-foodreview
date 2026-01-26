package com.example.foodreview.controller;

import com.example.foodreview.dto.OrderDTO;
import com.example.foodreview.model.User;
import com.example.foodreview.service.OrderService;
import com.example.foodreview.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    // 1. USER: Lấy đơn của mình
    @GetMapping("/my-orders")
    @PreAuthorize("hasAnyRole('USER', 'DRIVER', 'ADMIN')")
    public ResponseEntity<List<OrderDTO>> getMyOrders() {
        return ResponseEntity.ok(orderService.getMyOrders());
    }

    // 2. USER: Tạo đơn
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderRequest, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username).orElseThrow();
        orderRequest.setUserId(user.getId());
        return ResponseEntity.ok(orderService.createOrder(orderRequest));
    }

    // 3. ADMIN: Lấy tất cả
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
    
    // 4. USER/ADMIN: Xem chi tiết
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'DRIVER', 'ADMIN')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
    
    // 5. USER: Hủy đơn
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }
}