package com.example.foodreview.controller;

import com.example.foodreview.dto.ReviewDTO;
import com.example.foodreview.dto.ReviewRequest;
import com.example.foodreview.model.User;
import com.example.foodreview.service.ReviewService;
import com.example.foodreview.service.UserService;
import lombok.RequiredArgsConstructor; // 👇 Dùng cái này thay cho @Autowired
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor // 👇 Tự động tạo Constructor cho các biến final
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService; 

    // 1. Gửi đánh giá mới (POST)
    @PostMapping
    @PreAuthorize("hasRole('USER')") // Chỉ USER mới được đánh giá
    public ResponseEntity<?> createReview(@RequestBody ReviewRequest request, Authentication authentication) {
        // Lấy username người đang đăng nhập
        String username = authentication.getName();
        
        // 👇 Lưu ý: UserService phải có hàm findByUsername trả về Optional<User>
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        ReviewDTO createdReview = reviewService.createReview(user.getId(), request);
        return ResponseEntity.ok(createdReview);
    }

    // 2. Xem danh sách đánh giá của một món ăn (Public - Ai cũng xem được)
    @GetMapping("/food/{foodId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByFood(@PathVariable Long foodId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByFoodId(foodId);
        return ResponseEntity.ok(reviews);
    }
}