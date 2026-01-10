package com.example.foodreview.service;

import com.example.foodreview.dto.ReviewDTO;
import com.example.foodreview.dto.ReviewRequest;
import com.example.foodreview.mapper.ReviewMapper; // 👇 Dùng Mapper xịn
import com.example.foodreview.model.Review;
// 👇 Sửa Import về gói sql
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
    private final ReviewMapper reviewMapper; // 👇 Inject Mapper

    // 1. Tạo đánh giá mới
    public ReviewDTO createReview(Long userId, ReviewRequest request) {
        
        // Kiểm tra xem đơn hàng này đã được đánh giá chưa (nếu có orderId)
        if (request.getOrderId() != null && 
            reviewRepository.existsByOrderIdAndFoodId(request.getOrderId(), request.getFoodId())) {
             throw new RuntimeException("Bạn đã đánh giá món ăn trong đơn hàng này rồi!");
        }

        Review review = new Review();
        review.setUserId(userId);
        review.setFoodId(request.getFoodId());
        review.setOrderId(request.getOrderId()); // 👇 Lưu OrderId
        
        // ⚠️ Không set Username/Avatar vào Entity nữa (Mapper sẽ tự lấy từ bảng User)
        
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setImage(request.getImage()); // Lưu ảnh (String)
        
        // Mặc định các điểm chi tiết bằng điểm tổng (nếu Request chưa có)
        // Bạn có thể mở rộng ReviewRequest để nhận thêm các điểm này sau
        review.setTasteRating((double) request.getRating());
        review.setHygieneRating((double) request.getRating());
        review.setServiceRating((double) request.getRating());
        review.setPriceRating((double) request.getRating());

        review.setCreatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);
        
        // 👇 Dùng Mapper để chuyển đổi (Nó sẽ tự query lấy tên User/Food)
        return reviewMapper.toDTO(savedReview); 
    }

    // 2. Lấy danh sách đánh giá của món ăn
    public List<ReviewDTO> getReviewsByFoodId(Long foodId) {
        return reviewRepository.findByFoodIdOrderByCreatedAtDesc(foodId).stream()
                .map(reviewMapper::toDTO) // 👇 Code gọn hơn hẳn nhờ Mapper
                .collect(Collectors.toList());
    }
}