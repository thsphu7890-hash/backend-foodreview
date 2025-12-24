package com.example.foodreview.controller;

import com.example.foodreview.dto.VoucherDTO;
import com.example.foodreview.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vouchers")
@CrossOrigin(origins = "*")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    // 1. Lấy danh sách
    @GetMapping
    public List<VoucherDTO> getAllVouchers() {
        return voucherService.getAllVouchers();
    }

    // 2. Tạo mới (Có bắt lỗi Validate từ Service)
    @PostMapping
    public ResponseEntity<?> createVoucher(@RequestBody VoucherDTO dto) {
        try {
            return ResponseEntity.ok(voucherService.createVoucher(dto));
        } catch (RuntimeException e) {
            // Trả về lỗi 400 kèm message (VD: "Loại Voucher này yêu cầu nhập giá trị điều kiện...")
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3. Cập nhật (ĐÃ SỬA: Thêm try-catch để bắt lỗi Validate khi sửa)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateVoucher(@PathVariable Long id, @RequestBody VoucherDTO dto) {
        try {
            return ResponseEntity.ok(voucherService.updateVoucher(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 4. Xóa
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVoucher(@PathVariable Long id) {
        try {
            voucherService.deleteVoucher(id);
            return ResponseEntity.ok("Xóa thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}