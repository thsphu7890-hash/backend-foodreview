package com.example.foodreview.mapper;

import com.example.foodreview.dto.ReviewDTO;
import com.example.foodreview.model.Food;
import com.example.foodreview.model.Review;
import com.example.foodreview.model.User;

// 👇 SỬA LẠI: Thêm chữ .sql vào đường dẫn import
import com.example.foodreview.repository.FoodRepository;
import com.example.foodreview.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class ReviewMapper {

    private final UserRepository userRepository;
    private final FoodRepository foodRepository;

    public ReviewDTO toDTO(Review review) {
        if (review == null) {
            return null;
        }

        ReviewDTO dto = new ReviewDTO();
        
        // --- 1. Map các trường cơ bản ---
        dto.setId(review.getId());
        dto.setComment(review.getComment());
        dto.setRating(review.getRating());
        dto.setCreatedAt(review.getCreatedAt());

        // Xử lý ảnh: String -> List<String>
        if (review.getImage() != null && !review.getImage().isEmpty()) {
            dto.setImages(Collections.singletonList(review.getImage()));
        }

        dto.setOrderId(review.getOrderId());
        dto.setHelpfulCount(review.getHelpfulCount());
        
        // Map điểm chi tiết
        dto.setTasteRating(review.getTasteRating());
        dto.setHygieneRating(review.getHygieneRating());
        dto.setServiceRating(review.getServiceRating());
        dto.setPriceRating(review.getPriceRating());

        // Map phản hồi
        dto.setReply(review.getReply());
        dto.setReplyAt(review.getReplyAt());

        // --- 2. Lấy thông tin User từ MySQL ---
        if (review.getUserId() != null) {
            dto.setUserId(review.getUserId());
            User user = userRepository.findById(review.getUserId()).orElse(null);
            if (user != null) {
                String displayName = (user.getFullName() != null && !user.getFullName().isEmpty()) 
                                     ? user.getFullName() 
                                     : user.getUsername();
                dto.setUsername(displayName);
                dto.setUserAvatar(user.getAvatar());
            } else {
                dto.setUsername("Người dùng không tồn tại");
            }
        } else {
            dto.setUsername("Ẩn danh");
        }

        // --- 3. Lấy thông tin Món ăn từ MySQL ---
        if (review.getFoodId() != null) {
            dto.setFoodId(review.getFoodId());
            Food food = foodRepository.findById(review.getFoodId()).orElse(null);
            if (food != null) {
                dto.setFoodName(food.getName());
            } else {
                dto.setFoodName("Món ăn đã bị xóa");
            }
        }

        return dto;
    }
}