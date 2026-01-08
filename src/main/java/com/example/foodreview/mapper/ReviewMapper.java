package com.example.foodreview.mapper;

import com.example.foodreview.dto.ReviewDTO;
import com.example.foodreview.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    // 1. Chuyển từ Entity -> DTO (Full Option)
    public ReviewDTO toDTO(Review review) {
        if (review == null) {
            return null;
        }

        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setComment(review.getComment());
        dto.setRating(review.getRating());
        dto.setCreatedAt(review.getCreatedAt());

        // --- MAPPING NÂNG CẤP ---
        dto.setImages(review.getImages());
        dto.setOrderId(review.getOrderId());
        dto.setHelpfulCount(review.getHelpfulCount());
        
        // Điểm chi tiết
        dto.setTasteRating(review.getTasteRating());
        dto.setHygieneRating(review.getHygieneRating());
        dto.setServiceRating(review.getServiceRating());
        dto.setPriceRating(review.getPriceRating());

        // Phản hồi
        dto.setReply(review.getReply());
        dto.setReplyAt(review.getReplyAt());

        // --- MAP USER (Kèm Avatar) ---
        if (review.getUser() != null) {
            dto.setUserId(review.getUser().getId());
            
            // Lấy tên hiển thị
            String displayName = review.getUser().getFullName();
            if (displayName == null || displayName.isEmpty()) {
                displayName = review.getUser().getUsername();
            }
            dto.setUsername(displayName);
            
            // Lấy Avatar
            dto.setUserAvatar(review.getUser().getAvatar());
        } else {
            dto.setUsername("Người dùng ẩn danh");
        }

        // --- MAP FOOD ---
        if (review.getFood() != null) {
            dto.setFoodId(review.getFood().getId());
            dto.setFoodName(review.getFood().getName());
        }

        return dto;
    }
}