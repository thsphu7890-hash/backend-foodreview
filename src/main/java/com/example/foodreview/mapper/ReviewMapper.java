package com.example.foodreview.mapper;

import com.example.foodreview.dto.ReviewDTO;
import com.example.foodreview.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    // 1. Chuyển từ Entity -> DTO (Để trả về Frontend)
    public ReviewDTO toDTO(Review review) {
        if (review == null) {
            return null;
        }

        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        
        // Sửa: Dùng 'comment' cho khớp với Entity/DTO cũ
        dto.setComment(review.getComment()); 
        dto.setRating(review.getRating());
        dto.setCreatedAt(review.getCreatedAt());

        // --- MAP FOOD (Món ăn) ---
        if (review.getFood() != null) {
            dto.setFoodId(review.getFood().getId());
        }

        // --- MAP USER (Người đánh giá) ---
        if (review.getUser() != null) {
            dto.setUserId(review.getUser().getId());
            // Ưu tiên lấy tên đầy đủ, nếu không có thì lấy username
            String displayName = review.getUser().getFullName() != null 
                               ? review.getUser().getFullName() 
                               : review.getUser().getUsername();
            dto.setUsername(displayName); // Frontend đang dùng field 'username' để hiển thị
        } else {
            dto.setUsername("Ẩn danh");
        }

        return dto;
    }

    // 2. Chuyển từ DTO -> Entity (Để lưu xuống DB)
    public Review toEntity(ReviewDTO dto) {
        if (dto == null) {
            return null;
        }

        Review review = new Review();
        // Không set ID (tự sinh)
        review.setComment(dto.getComment());
        review.setRating(dto.getRating());
        
        // Lưu ý: User và Food sẽ được set trong Service (vì cần query DB để lấy object)
        return review;
    }
}