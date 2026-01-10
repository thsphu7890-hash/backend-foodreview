package com.example.foodreview.controller;

import com.example.foodreview.model.Food;
import com.example.foodreview.repository.FoodRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bot")
@RequiredArgsConstructor // Dùng lombok cho gọn, thay vì @Autowired
@CrossOrigin(origins = "*")
public class BotAPIController {

    private final FoodRepository foodRepository;

    // API cho n8n gọi: GET http://localhost:8080/api/bot/search?query=phở
    @GetMapping("/search")
    public List<String> searchForBot(@RequestParam(defaultValue = "") String query) {
        
        // 1. Validate đầu vào
        if (query == null || query.trim().isEmpty()) {
            return Collections.singletonList("Vui lòng nhập tên món ăn cần tìm.");
        }

        // 2. Giới hạn 5 kết quả để AI xử lý nhanh
        Pageable limit = PageRequest.of(0, 5);

        // 3. Gọi Repository
        List<Food> foods = foodRepository.searchFoodForBot(query.trim(), limit);

        // 4. Xử lý khi không có kết quả
        if (foods.isEmpty()) {
            return Collections.singletonList("Xin lỗi, tôi không tìm thấy món nào có tên là \"" + query + "\" trong hệ thống.");
        }

        // 5. Format dữ liệu dạng text để n8n gửi thẳng cho AI đọc
        return foods.stream()
                .map(f -> {
                    String videoUrl = (f.getVideo() != null && !f.getVideo().isEmpty()) ? f.getVideo() : "Không có video";
                    String desc = (f.getDescription() != null && !f.getDescription().isEmpty()) ? f.getDescription() : "Đang cập nhật...";
                    
                    // Format tiền Việt có dấu chấm (VD: 50.000)
                    String priceFormatted = String.format("%,.0f", f.getPrice()).replace(",", ".");

                    return String.format(
                        "---------\n" +
                        "🍲 Món: %s\n" +
                        "💰 Giá: %s VNĐ\n" +
                        "📝 Mô tả: %s\n" +
                        "🎥 Review: %s",
                        f.getName(),
                        priceFormatted,
                        desc,
                        videoUrl
                    );
                })
                .collect(Collectors.toList());
    }
}