package com.example.foodreview.controller;

import com.example.foodreview.dto.OrderDTO;
import com.example.foodreview.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin // Cho phép Frontend gọi API
public class OrderController {

    private final OrderService orderService;

    // --- DÀNH CHO NGƯỜI DÙNG (USER) ---

    // 1. Tạo đơn hàng mới
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.createOrder(orderDTO));
    }

    // 2. Lấy lịch sử đơn hàng của một người dùng
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    // 3. Người dùng tự hủy đơn (Chỉ khi đang PENDING)
    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }


    // --- DÀNH CHO QUẢN TRỊ VIÊN (ADMIN) ---

    // 4. Lấy tất cả đơn hàng hệ thống (Hiển thị trong OrderManager.jsx)
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // 5. Cập nhật trạng thái đơn hàng (Duyệt, Giao hàng, Hoàn thành)
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateStatus(
            @PathVariable Long id, 
            @RequestParam String status
    ) {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }

    // 🔥 6. ADMIN CHỈ ĐỊNH TÀI XẾ CHO ĐƠN HÀNG (MỚI THÊM) 🔥
    // API: PUT /api/orders/{orderId}/assign-driver/{driverId}
    @PutMapping("/{orderId}/assign-driver/{driverId}")
    public ResponseEntity<OrderDTO> assignDriver(
            @PathVariable Long orderId, 
            @PathVariable Long driverId
    ) {
        return ResponseEntity.ok(orderService.assignDriver(orderId, driverId));
    }
}