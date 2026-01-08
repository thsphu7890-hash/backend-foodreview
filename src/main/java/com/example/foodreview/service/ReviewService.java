package com.example.foodreview.service;

import com.example.foodreview.dto.ReviewDTO;
import com.example.foodreview.dto.ReviewRequest;
import com.example.foodreview.mapper.ReviewMapper;
import com.example.foodreview.model.Order;
import com.example.foodreview.model.Review;
import com.example.foodreview.repository.FoodRepository;
import com.example.foodreview.repository.OrderRepository;
import com.example.foodreview.repository.ReviewRepository;
import com.example.foodreview.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private FoodRepository foodRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ReviewMapper reviewMapper;

    // --- 1. TẠO REVIEW MỚI ---
    @Transactional
    public ReviewDTO createReview(Long userId, ReviewRequest req) {
        // A. Kiểm tra đơn hàng có tồn tại không
        Order order = orderRepository.findById(req.getOrderId())
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        // B. Kiểm tra đơn hàng có phải của user này không
        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền đánh giá đơn hàng này");
        }

        // C. Kiểm tra xem đã review chưa (Chống spam)
        if (reviewRepository.existsByOrderIdAndFoodId(req.getOrderId(), req.getFoodId())) {
             throw new RuntimeException("Bạn đã đánh giá món này rồi!");
        }

        // D. Tính điểm trung bình (Cộng 4 tiêu chí / 4)
        double avgRating = (req.getTasteRating() + req.getHygieneRating() + 
                            req.getServiceRating() + req.getPriceRating()) / 4.0;

        // E. Tạo Entity
        Review review = new Review();
        review.setUser(userRepository.getReferenceById(userId));
        review.setFood(foodRepository.getReferenceById(req.getFoodId()));
        review.setOrderId(req.getOrderId());
        
        // Lưu điểm
        review.setRating(avgRating);
        review.setTasteRating(req.getTasteRating());
        review.setHygieneRating(req.getHygieneRating());
        review.setServiceRating(req.getServiceRating());
        review.setPriceRating(req.getPriceRating());
        
        review.setComment(req.getComment());
        review.setImages(req.getImages()); // Lưu list ảnh

        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toDTO(savedReview);
    }

    // --- 2. LẤY DANH SÁCH REVIEW THEO MÓN ĂN ---
    public List<ReviewDTO> getReviewsByFoodId(Long foodId) {
        List<Review> reviews = reviewRepository.findByFoodIdOrderByCreatedAtDesc(foodId);
        return reviews.stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }
}