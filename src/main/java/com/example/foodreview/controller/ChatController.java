package com.example.foodreview.controller;

import com.example.foodreview.service.FoodAssistant;
import org.springframework.web.bind.annotation.*;

import java.util.Map; // Import Map

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:5173") // Đảm bảo đúng cổng React của bạn
public class ChatController {

    private final FoodAssistant foodAssistant;

    public ChatController(FoodAssistant foodAssistant) {
        this.foodAssistant = foodAssistant;
    }

    @PostMapping
    // 1. Nhận vào Map (JSON) thay vì String
    public Map<String, String> chat(@RequestBody Map<String, String> payload) {
        try {
            // Lấy tin nhắn từ key "message" mà React gửi lên
            String userMessage = payload.get("message");
            
            // Gọi AI xử lý
            String aiReply = foodAssistant.chat(userMessage);

            // 2. Trả về Map (JSON) có key là "reply" để React đọc được
            return Map.of("reply", aiReply);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("reply", "Xin lỗi, hệ thống đang bận: " + e.getMessage());
        }
    }
}