package com.example.foodreview.service;

import dev.langchain4j.service.SystemMessage;

// Đây là Interface để LangChain4j tự động tạo proxy
public interface FoodAssistant {
    
    @SystemMessage("Bạn là một trợ lý ảo chuyên về ẩm thực. Hãy trả lời thân thiện và ngắn gọn.")
    String chat(String userMessage);
}