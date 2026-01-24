package com.example.foodreview.controller;

import com.example.foodreview.dto.ReviewDTO;
import com.example.foodreview.dto.ReviewRequest;
import com.example.foodreview.model.User;
import com.example.foodreview.service.ReviewService;
import com.example.foodreview.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    // 1. Lấy TẤT CẢ đánh giá (Đây là đoạn code bạn đang thiếu gây ra lỗi 405)
    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    // 2. Gửi đánh giá mới (POST)
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createReview(@RequestBody ReviewRequest request, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        ReviewDTO createdReview = reviewService.createReview(user.getId(), request);
        return ResponseEntity.ok(createdReview);
    }

    // 3. Xem đánh giá theo món ăn
    @GetMapping("/food/{foodId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByFood(@PathVariable Long foodId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByFoodId(foodId);
        return ResponseEntity.ok(reviews);
    }
}