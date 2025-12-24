package com.example.foodreview.service;

import com.example.foodreview.dto.ReviewDTO;
import com.example.foodreview.model.Food;
import com.example.foodreview.model.Review;
import com.example.foodreview.model.User;
import com.example.foodreview.repository.FoodRepository;
import com.example.foodreview.repository.ReviewRepository;
import com.example.foodreview.repository.UserRepository;
import com.example.foodreview.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepo;
    private final FoodRepository foodRepo;
    private final UserRepository userRepo;
    private final ReviewMapper reviewMapper;

    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByFood(Long foodId) {
        return reviewRepo.findByFoodIdOrderByCreatedAtDesc(foodId).stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    // --- SỬA LẠI HÀM NÀY ĐỂ TRÁNH LỖI 500 ---
    @Transactional
    public ReviewDTO createReview(ReviewDTO dto) {
        // 1. Kiểm tra dữ liệu đầu vào
        if (dto.getUserId() == null) throw new RuntimeException("User ID không được để trống");
        if (dto.getFoodId() == null) throw new RuntimeException("Food ID không được để trống");

        // 2. Tìm User và Food
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User không tồn tại với ID: " + dto.getUserId()));
        
        Food food = foodRepo.findById(dto.getFoodId())
                .orElseThrow(() -> new RuntimeException("Món ăn không tồn tại với ID: " + dto.getFoodId()));

        // 3. Tạo Entity thủ công (An toàn hơn dùng Mapper lúc tạo mới)
        Review review = new Review();
        review.setUser(user);
        review.setFood(food);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        
        // 4. Lưu xuống DB
        Review savedReview = reviewRepo.save(review);

        // 5. Trả về DTO
        return reviewMapper.toDTO(savedReview);
    }
}