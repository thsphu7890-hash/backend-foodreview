package com.example.foodreview.controller;

import com.example.foodreview.dto.OrderDTO;
import com.example.foodreview.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor
@CrossOrigin
public class DriverController {

    private final DriverService driverService;

    // 1. Lấy danh sách đơn chưa ai nhận (Tab: Săn đơn)
    @GetMapping("/available-orders")
    public ResponseEntity<List<OrderDTO>> getAvailableOrders() {
        return ResponseEntity.ok(driverService.getAvailableOrders());
    }

    // 2. Tài xế bấm nút "Nhận đơn"
    @PostMapping("/accept")
    public ResponseEntity<String> acceptOrder(@RequestParam Long orderId, @RequestParam Long driverId) {
        try {
            driverService.acceptOrder(orderId, driverId);
            return ResponseEntity.ok("Nhận đơn thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3. Lấy đơn đang giao (Tab: Đang giao)
    @GetMapping("/my-current-order/{driverId}")
    public ResponseEntity<List<OrderDTO>> getCurrentOrder(@PathVariable Long driverId) {
        return ResponseEntity.ok(driverService.getCurrentOrder(driverId));
    }

    // 4. Bấm nút "Hoàn thành"
    @PostMapping("/complete")
    public ResponseEntity<String> completeOrder(@RequestParam Long orderId) {
        driverService.completeOrder(orderId);
        return ResponseEntity.ok("Đã giao hàng thành công!");
    }
    
    // 5. Lịch sử
    @GetMapping("/history/{driverId}")
    public ResponseEntity<List<OrderDTO>> getHistory(@PathVariable Long driverId) {
        return ResponseEntity.ok(driverService.getHistory(driverId));
    }
}