package com.example.foodreview.service;

import com.example.foodreview.dto.ReviewDTO;
import com.example.foodreview.dto.ReviewRequest;
import com.example.foodreview.mapper.ReviewMapper;
import com.example.foodreview.model.Review;
import com.example.foodreview.repository.ReviewRepository;
import com.example.foodreview.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    // 1. Tạo đánh giá mới
    public ReviewDTO createReview(Long userId, ReviewRequest request) {
        
        // Kiểm tra xem đơn hàng này đã được đánh giá chưa
        if (request.getOrderId() != null && 
            reviewRepository.existsByOrderIdAndFoodId(request.getOrderId(), request.getFoodId())) {
             throw new RuntimeException("Bạn đã đánh giá món ăn trong đơn hàng này rồi!");
        }

        Review review = new Review();
        review.setUserId(userId);
        review.setFoodId(request.getFoodId());
        review.setOrderId(request.getOrderId());
        
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setImage(request.getImage());
        
        // Mặc định các điểm chi tiết bằng điểm tổng
        review.setTasteRating((double) request.getRating());
        review.setHygieneRating((double) request.getRating());
        review.setServiceRating((double) request.getRating());
        review.setPriceRating((double) request.getRating());

        review.setCreatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);
        
        return reviewMapper.toDTO(savedReview); 
    }

    // 2. Lấy danh sách đánh giá của một món ăn cụ thể
    public List<ReviewDTO> getReviewsByFoodId(Long foodId) {
        return reviewRepository.findByFoodIdOrderByCreatedAtDesc(foodId).stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ======================================================================
    // 👇 3. Lấy TẤT CẢ đánh giá (Đây là hàm bạn đang THIẾU để sửa lỗi 405)
    // ======================================================================
    public List<ReviewDTO> getAllReviews() {
        // Lấy tất cả từ DB và map sang DTO
        // Nếu muốn sắp xếp mới nhất lên đầu, bạn có thể dùng Sort hoặc query order by trong Repository
        return reviewRepository.findAll().stream() 
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }
}