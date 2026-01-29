package com.example.foodreview.controller;

import com.example.foodreview.dto.OrderDTO;
import com.example.foodreview.model.User;
import com.example.foodreview.service.OrderService;
import com.example.foodreview.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Cho phép Frontend gọi API
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final UserService userService;

    // 1. USER: Lấy đơn của mình
    @GetMapping("/my-orders")
    @PreAuthorize("hasAnyRole('USER', 'DRIVER', 'ADMIN')")
    public ResponseEntity<List<OrderDTO>> getMyOrders() {
        // Service đã tự xử lý việc lấy User từ Token, Controller chỉ cần chuyển tiếp
        return ResponseEntity.ok(orderService.getMyOrders());
    }

    // 2. USER: Tạo đơn
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderRequest, Authentication authentication) {
        try {
            logger.info("--- BẮT ĐẦU TẠO ĐƠN HÀNG ---");

            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vui lòng đăng nhập!");
            }

            // Lấy User từ Token để đảm bảo bảo mật (Không tin tưởng userId từ frontend gửi lên)
            String username = authentication.getName();
            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User không tồn tại: " + username));

            // Gán đè UserId từ Token vào Request (Chống hack đặt đơn hộ người khác)
            orderRequest.setUserId(user.getId());

            // Gọi Service
            OrderDTO createdOrder = orderService.createOrder(orderRequest);

            logger.info("Tạo đơn thành công: ID " + createdOrder.getId());
            return ResponseEntity.ok(createdOrder);

        } catch (Exception e) {
            logger.error("LỖI TẠO ĐƠN: ", e);
            // Trả về JSON lỗi để Frontend hiển thị toast
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Lỗi Server", "message", e.getMessage()));
        }
    }

    // 3. ADMIN: Lấy tất cả
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // 4. CHI TIẾT ĐƠN HÀNG
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'DRIVER', 'ADMIN')")
    public ResponseEntity<OrderDTO> getById(@PathVariable Long id) {
        // Không cần try-catch, nếu không tìm thấy Service sẽ ném ResourceNotFoundException
        // và GlobalExceptionHandler sẽ xử lý trả về 404
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // 5. HỦY ĐƠN
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderDTO> cancel(@PathVariable Long id) {
        // Service sẽ ném lỗi nếu không hủy được, GlobalExceptionHandler sẽ bắt
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }
}