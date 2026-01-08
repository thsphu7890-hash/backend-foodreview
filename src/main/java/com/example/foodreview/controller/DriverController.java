package com.example.foodreview.controller;

import com.example.foodreview.dto.DriverDTO;
import com.example.foodreview.dto.OrderDTO;
import com.example.foodreview.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/driver") // Base URL là /api/driver
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DriverController {

    private final DriverService driverService;

    // --- KHU VỰC CHO APP TÀI XẾ (MOBILE) ---

    @PostMapping("/login")
    public ResponseEntity<?> loginDriver(@RequestParam String phone) {
        try {
            return ResponseEntity.ok(driverService.loginDriver(phone));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerDriver(
            @RequestParam("fullName") String fullName,
            @RequestParam("phone") String phone,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam("idCardNumber") String idCardNumber,
            @RequestParam("vehicleType") String vehicleType,
            @RequestParam("licensePlate") String licensePlate,
            @RequestParam("idCardFront") MultipartFile idCardFront,
            @RequestParam(value = "idCardBack", required = false) MultipartFile idCardBack
    ) {
        try {
            return ResponseEntity.ok(driverService.registerDriver(
                fullName, phone, email, address, idCardNumber, vehicleType, licensePlate, idCardFront, idCardBack
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/available-orders")
    public ResponseEntity<List<OrderDTO>> getAvailableOrders() {
        return ResponseEntity.ok(driverService.getAvailableOrders());
    }

    @PutMapping("/accept/{orderId}/{driverId}")
    public ResponseEntity<?> acceptOrder(@PathVariable Long orderId, @PathVariable Long driverId) {
        try {
            driverService.acceptOrder(orderId, driverId);
            return ResponseEntity.ok("Nhận đơn thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/my-current-order/{driverId}")
    public ResponseEntity<List<OrderDTO>> getCurrentOrder(@PathVariable Long driverId) {
        return ResponseEntity.ok(driverService.getCurrentOrder(driverId));
    }

    @PutMapping("/complete/{orderId}")
    public ResponseEntity<?> completeOrder(@PathVariable Long orderId) {
        try {
            driverService.completeOrder(orderId);
            return ResponseEntity.ok("Đã giao hàng thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/history/{driverId}")
    public ResponseEntity<List<OrderDTO>> getHistory(@PathVariable Long driverId) {
        return ResponseEntity.ok(driverService.getHistory(driverId));
    }

    // --- KHU VỰC CHO ADMIN DASHBOARD (WEB) ---

    // 1. Lấy danh sách tất cả tài xế
    @GetMapping("/all")
    public ResponseEntity<List<DriverDTO>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    // 2. Duyệt tài xế
    @PutMapping("/approve/{id}")
    public ResponseEntity<String> approveDriver(@PathVariable Long id) {
        try {
            driverService.updateStatus(id, "ACTIVE");
            return ResponseEntity.ok("Đã duyệt tài xế thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3. Khóa tài xế
    @PutMapping("/block/{id}")
    public ResponseEntity<String> blockDriver(@PathVariable Long id) {
        try {
            driverService.updateStatus(id, "BLOCKED");
            return ResponseEntity.ok("Đã khóa tài khoản tài xế!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}