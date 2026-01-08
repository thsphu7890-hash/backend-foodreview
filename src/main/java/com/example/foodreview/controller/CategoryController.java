package com.example.foodreview.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Import gộp cho gọn

import com.example.foodreview.dto.CategoryDTO;
import com.example.foodreview.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // Nên chỉ định rõ nguồn Frontend (React)
public class CategoryController {

    private final CategoryService service;

    // 1. Lấy danh sách (GET)
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // 2. Tạo mới (POST)
    @PostMapping
    public ResponseEntity<CategoryDTO> create(@RequestBody CategoryDTO dto) {
        // Trả về mã 201 Created
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    // 3. Cập nhật (PUT) - Bổ sung thêm
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    // 4. Xóa (DELETE) - Bổ sung thêm
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        // Trả về mã 204 No Content (Thành công nhưng không có body trả về)
        return ResponseEntity.noContent().build();
    }
}