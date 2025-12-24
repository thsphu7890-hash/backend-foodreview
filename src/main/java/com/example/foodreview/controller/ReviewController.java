package com.example.foodreview.controller;

import com.example.foodreview.dto.ReviewDTO;
import com.example.foodreview.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // --- 1. API LẤY REVIEW THEO MÓN ĂN (Frontend đang gọi cái này) ---
    // URL: GET /api/reviews/food/10
    @GetMapping("/food/{foodId}")
    public ResponseEntity<List<ReviewDTO>> getByFood(@PathVariable Long foodId) {
        return ResponseEntity.ok(reviewService.getReviewsByFood(foodId));
    }

    // --- 2. API TẠO REVIEW MỚI (Frontend đang gọi cái này) ---
    // URL: POST /api/reviews
    @PostMapping
    public ResponseEntity<ReviewDTO> create(@RequestBody ReviewDTO dto) {
        return ResponseEntity.ok(reviewService.createReview(dto));
    }

    // --- 3. API LẤY REVIEW THEO NHÀ HÀNG (Cũ - Giữ lại nếu cần) ---
    // URL: GET /api/reviews/restaurant/5
    // Lưu ý: Đảm bảo ReviewService phải có hàm getByRestaurant tương ứng
    /*
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<ReviewDTO>> getByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(reviewService.getByRestaurant(restaurantId));
    }
    */
}