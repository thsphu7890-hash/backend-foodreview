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

    // --- 1. LẤY LỊCH SỬ CỦA TÔI (User thường dùng) ---
    @GetMapping("/my-orders")
    @PreAuthorize("hasAnyRole('USER', 'DRIVER', 'ADMIN')")
    public ResponseEntity<List<OrderDTO>> getMyOrders(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return ResponseEntity.ok(orderService.getOrdersByUser(user.getId()));
    }

    // --- 2. XEM CHI TIẾT ---
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'DRIVER', 'ADMIN')")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.getOrderById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Không tìm thấy đơn hàng");
        }
    }

    // --- 3. TẠO ĐƠN ---
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderRequest, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        orderRequest.setUserId(user.getId());
        return ResponseEntity.ok(orderService.createOrder(orderRequest));
    }

    // --- 4. HỦY ĐƠN ---
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }

    // --- 5. 👇 QUAN TRỌNG: LẤY TẤT CẢ ĐƠN (Dành cho Admin/Manager) ---
    // Đây là cái bạn đang thiếu!
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')") // Chỉ Admin hoặc Driver mới được xem hết
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}