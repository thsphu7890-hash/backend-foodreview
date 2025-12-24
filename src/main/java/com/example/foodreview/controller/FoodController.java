package com.example.foodreview.controller;

import com.example.foodreview.dto.FoodDTO;
import com.example.foodreview.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
@CrossOrigin // Cho phép Frontend gọi API
public class FoodController {

    private final FoodService foodService;

    // 1. API cho ADMIN (Phân trang, quản lý)
    @GetMapping
    public ResponseEntity<Page<FoodDTO>> getAll(
            @RequestParam(required = false, defaultValue = "") String search,
            Pageable pageable
    ) {
        return ResponseEntity.ok(foodService.getAllFoods(search, pageable));
    }

    // --- 👇 2. API CHO USER SEARCH (MỚI THÊM) 👇 ---
    // URL: /api/foods/search?cat=1&q=pho
    @GetMapping("/search")
    public ResponseEntity<List<FoodDTO>> searchFoods(
            @RequestParam(required = false) Long cat,  // Nhận param 'cat' từ URL
            @RequestParam(required = false) String q   // Nhận param 'q' từ URL
    ) {
        return ResponseEntity.ok(foodService.searchFoods(cat, q));
    }
    // ------------------------------------------------

    @GetMapping("/{id}")
    public ResponseEntity<FoodDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(foodService.getById(id));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<FoodDTO>> getByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(foodService.getByRestaurant(restaurantId));
    }

    @PostMapping
    public ResponseEntity<FoodDTO> create(@RequestBody FoodDTO dto) {
        return ResponseEntity.ok(foodService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodDTO> update(@PathVariable Long id, @RequestBody FoodDTO dto) {
        return ResponseEntity.ok(foodService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        foodService.delete(id);
        return ResponseEntity.ok().build();
    }
}